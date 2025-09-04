package com.zayaanit.module.events;

import java.time.LocalDate;
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
import com.zayaanit.module.category.Category;
import com.zayaanit.module.category.CategoryRepo;
import com.zayaanit.module.documents.Document;
import com.zayaanit.module.documents.DocumentRepo;
import com.zayaanit.module.documents.DocumentResDto;
import com.zayaanit.module.documents.DocumentService;
import com.zayaanit.module.events.checklists.EventChecklist;
import com.zayaanit.module.events.checklists.EventChecklistRepo;
import com.zayaanit.module.events.checklists.EventChecklistResDto;
import com.zayaanit.module.events.parents.CreateParentEventReqDto;
import com.zayaanit.module.events.parents.ParentEvent;
import com.zayaanit.module.events.parents.ParentEventRepo;
import com.zayaanit.module.events.perticipants.EventPerticipants;
import com.zayaanit.module.events.perticipants.EventPerticipantsRepo;
import com.zayaanit.module.events.repeaters.EventRepeatType;
import com.zayaanit.module.events.repeaters.EventRepeater;
import com.zayaanit.module.events.repeaters.EventRepeaterRepo;
import com.zayaanit.module.notification.AsyncNotificationService;
import com.zayaanit.module.notification.NotificationType;
import com.zayaanit.module.projects.Project;
import com.zayaanit.module.projects.ProjectRepo;
import com.zayaanit.module.reminder.ReminderService;

import io.jsonwebtoken.lang.Collections;
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
	@Autowired private EventChecklistRepo checklistRepo;
	@Autowired private CategoryRepo categoryRepo;
	@Autowired private ProjectRepo projectRepo;
	@Autowired private ParentEventRepo parentEventRepo;
	@Autowired private EventRepeaterRepo eventRepeaterRepo;

	public long getCountOfAllEventsFromAllProjects(LocalDate date, Boolean isCompleted){
		List<Project> projects = projectRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());
		if(projects.isEmpty()) return 0;

		List<EventResDto> allEventResponseList = new ArrayList<>();

		for(Project project : projects) {
			List<Event> todaysEvents = new ArrayList<>();
			if(date == null) {
				todaysEvents = eventRepo.findAllByProjectIdAndIsCompleted(project.getId(), isCompleted);
			} else {
				if(date.isEqual(LocalDate.now())) {
					todaysEvents = eventRepo.findAllByProjectIdAndEventDateAndIsCompleted(project.getId(), date, isCompleted);
				} else {
					todaysEvents = eventRepo.findAllByProjectIdAndEventDateAfterAndIsCompleted(project.getId(), LocalDate.now(), isCompleted);
				}
			}

			List<EventResDto> eventsResponseList = todaysEvents.stream().map(EventResDto::new).collect(Collectors.toList());

			for(EventResDto event : eventsResponseList) {
				allEventResponseList.add(event);
			}
		}

		return allEventResponseList.size();
	}

	public List<EventResDto> getAllEventsFromAllProjects(LocalDate date, Boolean isCompleted){
		List<Project> projects = projectRepo.findAllByWorkspaceId(loggedinUser().getWorkspace().getId());
		if(projects.isEmpty()) return Collections.emptyList(); 

		List<EventResDto> allEventResponseList = new ArrayList<>();

		for(Project project : projects) {
			List<Event> todaysEvents = new ArrayList<>();
			if(date == null) {
				todaysEvents = eventRepo.findAllByProjectIdAndIsCompleted(project.getId(), isCompleted);
			} else {
				if(date.isEqual(LocalDate.now())) {
					todaysEvents = eventRepo.findAllByProjectIdAndEventDateAndIsCompleted(project.getId(), date, isCompleted);
				} else {
					todaysEvents = eventRepo.findAllByProjectIdAndEventDateAfterAndIsCompleted(project.getId(), LocalDate.now(), isCompleted);
				}
			}

			List<EventResDto> eventsResponseList = todaysEvents.stream().map(EventResDto::new).collect(Collectors.toList());

			for(EventResDto event : eventsResponseList) {
				event.setProjectName(project.getName());

				// Checklist
				event.setChecklists(new ArrayList<>());
				List<EventChecklist> checklists = checklistRepo.findAllByEventId(event.getId());
				if(checklists != null && !checklists.isEmpty()) {
					checklists.stream().forEach(checklist -> {
						event.getChecklists().add(new EventChecklistResDto(checklist));
					});
				}

				// category
				if(event.getCategoryId() != null) {
					Optional<Category> categoryOp = categoryRepo.findById(event.getCategoryId());
					if(categoryOp.isPresent()) {
						event.setCategoryName(categoryOp.get().getName());
					}
				}

				// Documents
				event.setDocuments(new ArrayList<>());
				List<Document> documents = documentRepo.findAllByReferenceId(event.getId());
				if(documents != null && !documents.isEmpty()) {
					for(Document document : documents) {
						event.getDocuments().add(new DocumentResDto(document));
					}
				}

				allEventResponseList.add(event);
			}
		}

		return allEventResponseList;
	}

	public List<EventResDto> getAllByProjectId(Long projectId, Boolean isComplete) {
		List<Event> events = new ArrayList<>();

		if(isComplete == null) {
			events = eventRepo.findAllByProjectId(projectId);
		} else {
			events = eventRepo.findAllByProjectIdAndIsCompleted(projectId, isComplete);
		}

		List<EventResDto> responseData = events.stream().map(EventResDto::new).collect(Collectors.toList());

		// Get all the checklists, projects, categories
		responseData.stream().forEach(event -> {
			// Checklist
			event.setChecklists(new ArrayList<>());
			List<EventChecklist> checklists = checklistRepo.findAllByEventId(event.getId());
			if(checklists != null && !checklists.isEmpty()) {
				checklists.stream().forEach(checklist -> {
					event.getChecklists().add(new EventChecklistResDto(checklist));
				});
			}

			// project
			Optional<Project> projectOp = projectRepo.findById(event.getProjectId());
			if(projectOp.isPresent()) {
				event.setProjectName(projectOp.get().getName());
			}

			// category
			if(event.getCategoryId() != null) {
				Optional<Category> categoryOp = categoryRepo.findById(event.getCategoryId());
				if(categoryOp.isPresent()) {
					event.setCategoryName(categoryOp.get().getName());
				}
			}

			// Documents
			event.setDocuments(new ArrayList<>());
			List<Document> documents = documentRepo.findAllByReferenceId(event.getId());
			if(documents != null && !documents.isEmpty()) {
				for(Document document : documents) {
					event.getDocuments().add(new DocumentResDto(document));
				}
			}
		});

		return responseData;
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

		// if event type is REPEAT , then the process id fefferent
		if(EventType.REPEAT.equals(event.getEventType())) {
			// Create a parent event first
			ParentEvent parentEvent = CreateParentEventReqDto.from(event).getBean();
			parentEvent = parentEventRepo.save(parentEvent);
			if(parentEvent == null || parentEvent.getId() == null) {
				throw new CustomException("Somethin is wrong with creating event", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Update the event repeater with parent event reference
			Optional<EventRepeater> erOp = eventRepeaterRepo.findById(event.getEventRepeaterId());
			if(!erOp.isPresent()) throw new CustomException("Event repeater not found", HttpStatus.NOT_FOUND);

			EventRepeater er = erOp.get();
			er.setParentEventId(parentEvent.getId());
			er = eventRepeaterRepo.save(er);

			// then create final events based on the repeater
			List<Event> createdEvents = createEventsFromEventRepeaterAndParentEvent(er, parentEvent);

			// only checklist and documents will be ignored, participent will be assigned all events
			for(Event ev : createdEvents) {
				// Add event creator
				EventPerticipants ep = EventPerticipants.builder()
						.userId(loggedinUser().getUserId())
						.eventId(ev.getId())
						.isReminderSent(Boolean.FALSE)
						.perticipantType(PerticipantType.CREATOR)
						.build();
				ep = epRepo.save(ep);
				eventCreatorId.add(ep.getUserId());

				// Add other perticipants
				if(reqDto.getPerticipants() != null) {
					List<Long> perticipants = reqDto.getPerticipants().stream().filter(p -> !p.equals(loggedinUser().getUserId())).collect(Collectors.toList());
					perticipants.stream().forEach(p -> {
						EventPerticipants eventPerticipant = EventPerticipants.builder()
								.userId(p)
								.eventId(ev.getId())
								.isReminderSent(Boolean.FALSE)
								.perticipantType(PerticipantType.CONTRIBUTOR)
								.build();
						epRepo.save(eventPerticipant);
						allPerticipantsId.add(p);
					});
				}

				// Schedule it for reminder
				scheduleEventReminder(ev);
			}

			// notification only once for the first event created, rest will be for the reminder
			Event finalEvent = createdEvents.get(0);

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

		Event finalEvent = eventRepo.save(event);

		// Add checklist 
		if(reqDto.getChecklists() != null) {
			reqDto.getChecklists().stream().forEach(c -> {
				EventChecklist checklist = c.getBean();
				checklist.setEventId(finalEvent.getId());
				checklistRepo.save(checklist);
			});
		}

		// Add documents reference id with event
		if(reqDto.getDocuments() != null) {
			reqDto.getDocuments().stream().forEach(d -> {
				Optional<Document> documentOp = documentRepo.findById(d);
				if(documentOp.isPresent()) {
					Document document = documentOp.get();
					document.setReferenceId(finalEvent.getId());
					document.setXtemp(false);
					documentRepo.save(document);
				}
			});
		}

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
		if(reqDto.getPerticipants() != null) {
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
		}

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

	// Limit to 30 event create in advanced
	private List<Event> createEventsFromEventRepeaterAndParentEvent(EventRepeater er, ParentEvent parentEvent){
		List<Event> events = new ArrayList<>();

		// TODO; write all the event create logic here, it may move to another seperate service to use it with event repeater change
		if(EventRepeatType.DAY.equals(er.getRepeatType())) {
			List<LocalDate> dates = new ArrayList<>();
			Integer gap = er.getRepeatEvery();
			LocalDate startDate = er.getStartDate();
			LocalDate endDate = er.getEndDate();

			dates.add(startDate);
			// Limit to 30 event creation
			for(int i = 0; i < 30; i++) {
				startDate = startDate.plusDays(gap);
				
				// TODO: weekday checking: if start date in weekday just skip current startDate and continue to the next
				
				if(endDate != null) {
					if(!startDate.isBefore(endDate)) {
						break;
					}
				}
				dates.add(startDate);
				i++;
			}

			// Now create events based on all the dates found. user parent event for data
			for(LocalDate eventDate : dates) {
				Event event = new Event();
				BeanUtils.copyProperties(parentEvent, event);  // TODO: need to observe
				event.setEventDate(eventDate);
				events.add(event);
			}
		}

		return events;
	}

	@Transactional
	public EventResDto markComplete(Long id) throws CustomException {
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
	public EventChecklistResDto markCheckListComplete(Long checklistId) throws CustomException {
		Optional<EventChecklist> checklistOp = checklistRepo.findById(checklistId);
		if(!checklistOp.isPresent()) throw new CustomException("Checklist item not found", HttpStatus.NOT_FOUND);

		EventChecklist existObj = checklistOp.get();
		existObj.setIsCompleted(true);
		EventChecklist finalChecklist = checklistRepo.save(existObj);

		return new EventChecklistResDto(finalChecklist);
	}

	@Transactional
	public EventChecklistResDto markCheckListInComplete(Long checklistId) throws CustomException {
		Optional<EventChecklist> checklistOp = checklistRepo.findById(checklistId);
		if(!checklistOp.isPresent()) throw new CustomException("Checklist item not found", HttpStatus.NOT_FOUND);

		EventChecklist existObj = checklistOp.get();
		existObj.setIsCompleted(false);
		EventChecklist finalChecklist = checklistRepo.save(existObj);

		return new EventChecklistResDto(finalChecklist);
	}

	@Transactional
	public EventResDto markInComplete(Long id) throws CustomException {
		Optional<Event> eventOp = eventRepo.findById(id);
		if(!eventOp.isPresent()) throw new CustomException("Event not found", HttpStatus.NOT_FOUND);

		Event existObj = eventOp.get();
		existObj.setIsCompleted(false);
		Event finalEvent = eventRepo.save(existObj);

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

		// TODO: checklist update, add new checklist, remove checklist
		

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
				document.setXtemp(false);
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

	public List<EventResDto> getTodayEvents() {
		LocalDate today = LocalDate.now();
		List<Event> events = eventRepo.findByEventDate(today);
		return events.stream().map(EventResDto::new).collect(Collectors.toList());
	}

	public List<EventResDto> getUpcomingEvents() {
		LocalDate today = LocalDate.now();
		List<Event> events = eventRepo.findByEventDateAfter(today);
		return events.stream().map(EventResDto::new).collect(Collectors.toList());
	}


}
