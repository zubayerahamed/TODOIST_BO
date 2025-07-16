package com.zayaanit.module.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.enums.PerticipantType;
import com.zayaanit.exception.CustomException;
import com.zayaanit.mail.MailType;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.documents.Document;
import com.zayaanit.module.documents.DocumentRepo;
import com.zayaanit.module.documents.DocumentService;
import com.zayaanit.module.events.perticipants.EventPerticipants;
import com.zayaanit.module.events.perticipants.EventPerticipantsRepo;
import com.zayaanit.module.notification.AsyncNotificationService;
import com.zayaanit.module.notification.NotificationType;
import com.zayaanit.module.reminder.ReminderService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Slf4j
@Service
public class EventService extends BaseService {

	@Autowired private DocumentRepo documentRepo;
	@Autowired private DocumentService documentService;
	@Autowired private EventRepo eventRepo;
	@Autowired private ReminderService reminderService;
	@Autowired private EventPerticipantsRepo epRepo;
	@Autowired private AsyncNotificationService asyncNotificationService;

	public List<EventResDto> getAllByProjectId(Long projectId) {
		List<Event> events = eventRepo.findAllByProjectId(projectId);
		return events.stream().map(EventResDto::new).collect(Collectors.toList());
	}

	public EventResDto  findById(Long id) throws CustomException {
		Optional<Event> taskOp = eventRepo.findById(id);
		if(!taskOp.isPresent()) throw new CustomException("Event not found", HttpStatus.NOT_FOUND);
		return new EventResDto(taskOp.get());
	}

	@Transactional
	public EventResDto create(CreateEventReqDto reqDto) throws CustomException {
		List<Long> eventCreatorId = new ArrayList<>();
		List<Long> allPerticipantsId = new ArrayList<>();

		Event event = reqDto.getBean();
		event.setIsCompleted(false);
		event.setIsReminderSent(false);
		Event finalEvent = eventRepo.save(event);

		// Add documents reference id with event
		reqDto.getDocuments().stream().forEach(d -> {
			Optional<Document> documentOp = documentRepo.findById(d);
			if(documentOp.isPresent()) {
				Document document = documentOp.get();
				document.setReferenceId(finalEvent.getId());
				documentRepo.save(document);
			}
		});

		// Add event creator
		EventPerticipants ep = EventPerticipants.builder()
				.userId(loggedinUser().getUserId())
				.eventId(finalEvent.getId())
				.isReminderSent(Boolean.FALSE)
				.perticipantType(PerticipantType.CREATOR)
				.build();
		ep = epRepo.save(ep);
		eventCreatorId.add(ep.getUserId());

		// Add other perticipants
		List<Long> perticipants = reqDto.getPerticipants().stream().filter(p -> !p.equals(loggedinUser().getUserId())).collect(Collectors.toList());
		perticipants.stream().forEach(p -> {
			EventPerticipants eventPerticipant = EventPerticipants.builder()
					.userId(p)
					.eventId(finalEvent.getId())
					.isReminderSent(Boolean.FALSE)
					.perticipantType(PerticipantType.CONTRIBUTOR)
					.build();
			epRepo.save(eventPerticipant);
			allPerticipantsId.add(p);
		});

		// Schedule it for reminder
		scheduleEventReminder(finalEvent);

		// Send Email Notification
		asyncNotificationService.sendEmailNotification(finalEvent, eventCreatorId, MailType.EVENT_CREATED);
		asyncNotificationService.sendEmailNotification(finalEvent, allPerticipantsId, MailType.EVENT_ASSIGNED);

		// Send socket push notification
		asyncNotificationService.sendSocketPushNotification(finalEvent, eventCreatorId, NotificationType.EVENT_CREATED);
		asyncNotificationService.sendSocketPushNotification(finalEvent, allPerticipantsId, NotificationType.EVENT_ASSIGNED);

		// Send web push notification
		asyncNotificationService.sendBrowserPushNotification(finalEvent, eventCreatorId, NotificationType.EVENT_CREATED);
		asyncNotificationService.sendBrowserPushNotification(finalEvent, allPerticipantsId, NotificationType.EVENT_ASSIGNED);

		// TODO: send sms notification

		return new EventResDto(finalEvent);
	}

	@Transactional
	public EventResDto complete(Long id) throws CustomException {
		Optional<Event> eventOp = eventRepo.findById(id);
		if(!eventOp.isPresent()) throw new CustomException("Event not found", HttpStatus.NOT_FOUND);

		Event existObj = eventOp.get();
		existObj.setIsCompleted(true);
		Event finalEvent = eventRepo.save(existObj);

		// Find All the event perticipants
		List<EventPerticipants> perticipants = epRepo.findAllByEventId(id);
		if(!perticipants.isEmpty()) {
			List<Long> perticipantsId = perticipants.stream().map(EventPerticipants::getUserId).collect(Collectors.toList());

			// Send Email Notification
			asyncNotificationService.sendEmailNotification(finalEvent, perticipantsId, MailType.EVENT_COMPLETED);

			// Send Push Notification
			asyncNotificationService.sendSocketPushNotification(finalEvent, perticipantsId, NotificationType.EVENT_COMPLETED);

			// Send browser push notification
			asyncNotificationService.sendBrowserPushNotification(finalEvent, perticipantsId, NotificationType.EVENT_COMPLETED);

			// TODO: send sms notification
		}

		return new EventResDto(finalEvent);
	}

	@Transactional
	public EventResDto update(UpdateEventReqDto reqDto) throws CustomException {
		List<Long> existingPerticipantsIdForMail = new ArrayList<>();
		List<Long> newPerticipantsIdForMail = new ArrayList<>();
		List<Long> removedPerticipantsIdForMail = new ArrayList<>();

		Optional<Event> eventOp = eventRepo.findById(reqDto.getId());
		if(!eventOp.isPresent()) throw new CustomException("Event not found", HttpStatus.NOT_FOUND);

		Event existObj = eventOp.get();
		BeanUtils.copyProperties(reqDto, existObj);
		existObj.setIsReminderSent(false);  // Reset Reminder Status
		Event finalEvent = eventRepo.save(existObj);

		// Update documents reference
		// Remove existing documents which are not available in this request
		// Add new documents which is newly added
		List<Document> existingDocuments = documentRepo.findAllByReferenceId(finalEvent.getId());
		List<Document> deletableDocuments = existingDocuments.stream().filter(d -> !reqDto.getDocuments().contains(d.getId())).collect(Collectors.toList());

		List<Long> existingDocIds = existingDocuments.stream().map(Document::getId).collect(Collectors.toList());
		List<Long> newDocumentIds = reqDto.getDocuments().stream()
				.filter(id -> !existingDocIds.contains(id))
				.collect(Collectors.toList());

		deletableDocuments.stream().forEach(d -> {
			documentService.delete(d.getId());
		});

		newDocumentIds.stream().forEach(nd -> {
			Optional<Document> documentOp = documentRepo.findById(nd);
			if(documentOp.isPresent()) {
				Document document = documentOp.get();
				document.setReferenceId(finalEvent.getId());
				documentRepo.save(document);
			}
		});

		// Update all perticipants reminder status
		List<EventPerticipants> existingPerticipants = epRepo.findAllByEventId(finalEvent.getId());
		List<EventPerticipants> deletablePerticipants = existingPerticipants.stream()
				.filter(d -> !reqDto.getPerticipants().contains(d.getUserId()))
				.filter(d -> !PerticipantType.CREATOR.equals(d.getPerticipantType()))
				.collect(Collectors.toList());

		List<Long> existingPerticipantsIds = existingPerticipants.stream().map(EventPerticipants::getUserId).collect(Collectors.toList());
		List<Long> newPerticipantsIds = reqDto.getPerticipants().stream()
				.filter(id -> !existingPerticipantsIds.contains(id))
				.collect(Collectors.toList());

		existingPerticipants.stream().forEach(p -> {
			p.setIsReminderSent(false);  // Reset all existing participants reminder status
			epRepo.save(p);
			existingPerticipantsIdForMail.add(p.getUserId());
		});

		deletablePerticipants.stream().forEach(d -> {
			epRepo.delete(d);
			removedPerticipantsIdForMail.add(d.getUserId());
		});

		newPerticipantsIds.stream().forEach(n -> {
			EventPerticipants eventPerticipant = EventPerticipants.builder()
					.userId(n)
					.eventId(finalEvent.getId())
					.isReminderSent(Boolean.FALSE)
					.perticipantType(PerticipantType.CONTRIBUTOR)
					.build();
			epRepo.save(eventPerticipant);
			newPerticipantsIdForMail.add(n);
		});

		// After creating event, schedule it for reminder
		scheduleEventReminder(finalEvent);

		// Send Email Notification
		asyncNotificationService.sendEmailNotification(finalEvent, existingPerticipantsIdForMail.stream().filter(e -> !removedPerticipantsIdForMail.contains(e)).collect(Collectors.toList()), MailType.EVENT_UPDATED);
		asyncNotificationService.sendEmailNotification(finalEvent, removedPerticipantsIdForMail, MailType.EVENT_UNASSIGNED);
		asyncNotificationService.sendEmailNotification(finalEvent, newPerticipantsIdForMail, MailType.EVENT_ASSIGNED);

		// Send push notification
		asyncNotificationService.sendSocketPushNotification(finalEvent, existingPerticipantsIdForMail.stream().filter(e -> !removedPerticipantsIdForMail.contains(e)).collect(Collectors.toList()), NotificationType.EVENT_UPDATED);
		asyncNotificationService.sendSocketPushNotification(finalEvent, removedPerticipantsIdForMail, NotificationType.EVENT_UNASSIGNED);
		asyncNotificationService.sendSocketPushNotification(finalEvent, newPerticipantsIdForMail, NotificationType.EVENT_ASSIGNED);

		// Send browser push notification
		asyncNotificationService.sendBrowserPushNotification(finalEvent, existingPerticipantsIdForMail.stream().filter(e -> !removedPerticipantsIdForMail.contains(e)).collect(Collectors.toList()), NotificationType.EVENT_UPDATED);
		asyncNotificationService.sendBrowserPushNotification(finalEvent, removedPerticipantsIdForMail, NotificationType.EVENT_UNASSIGNED);
		asyncNotificationService.sendBrowserPushNotification(finalEvent, newPerticipantsIdForMail, NotificationType.EVENT_ASSIGNED);

		// TODO: send sms notification

		return new EventResDto(finalEvent);
	}

	@Transactional
	public void delete(Long id) {
		Optional<Event> eventOp = eventRepo.findById(id);
		if(!eventOp.isPresent()) throw new CustomException("Event not found", HttpStatus.NOT_FOUND);

		// Cancel Reminder Schedule
		reminderService.cancelScheduledReminder(id);

		// Delete all attachments by event id
		documentService.deleteAllByReferenceId(id);

		// Delete all perticipants relations
		List<Long> allPerticipants = epRepo.findAllByEventId(id).stream().map(EventPerticipants::getUserId).collect(Collectors.toList());
		epRepo.deleteAllByEventId(id);

		// Delete task
		Event copy = SerializationUtils.clone(eventOp.get());
		eventRepo.delete(eventOp.get());

		// Send Email Notification
		asyncNotificationService.sendEmailNotification(copy, allPerticipants, MailType.EVENT_DELETED);

		// Send push notification
		asyncNotificationService.sendSocketPushNotification(copy, allPerticipants, NotificationType.EVENT_DELETED);

		// Send browser push notification
		asyncNotificationService.sendBrowserPushNotification(copy, allPerticipants, NotificationType.EVENT_DELETED);

		// TODO: send sms notification
	}

	private void scheduleEventReminder(final Event event) {
		if(Boolean.TRUE.equals(event.getIsCompleted())) return;
		if(Boolean.FALSE.equals(event.getIsReminderEnabled())) return;
		if(Boolean.TRUE.equals(event.getIsReminderSent())) return;

		reminderService.scheduleEventReminder(event, () -> {
			log.info("Sending Reminder for Event : " + event.getTitle());

			// Find All the event perticipants
			List<EventPerticipants> perticipants = epRepo.findAllByEventId(event.getId());
			if(perticipants.isEmpty()) return;

			List<Long> perticipantsId = perticipants.stream().map(EventPerticipants::getUserId).collect(Collectors.toList());

			// Send Email Notification
			asyncNotificationService.sendEmailNotification(event, perticipantsId, MailType.EVENT_REMINDER);

			// Send Push Notification
			asyncNotificationService.sendSocketPushNotification(event, perticipantsId, NotificationType.EVENT_REMINDER);

			// Send Push Notification
			asyncNotificationService.sendBrowserPushNotification(event, perticipantsId, NotificationType.EVENT_REMINDER);
		});
	}

	public void rescheduleAllEventReminders() {
		List<Event> futureEvents = eventRepo.findAllPendingReminders();
		futureEvents.forEach(t -> {
			scheduleEventReminder(t);
		});
	}
}
