package com.zayaanit.module.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.mail.MailReqDto;
import com.zayaanit.mail.MailService;
import com.zayaanit.mail.MailType;
import com.zayaanit.module.events.Event;
import com.zayaanit.module.events.EventRepo;
import com.zayaanit.module.events.perticipants.EventPerticipants;
import com.zayaanit.module.events.perticipants.EventPerticipantsPK;
import com.zayaanit.module.events.perticipants.EventPerticipantsRepo;
import com.zayaanit.module.users.User;
import com.zayaanit.module.users.UserRepo;
import com.zayaanit.module.users.preferences.UserPreference;
import com.zayaanit.module.users.preferences.UserPreferenceRepo;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Slf4j
@Service
public class AsyncNotificationService {

	@Autowired private UserRepo userRepo;
	@Autowired private UserPreferenceRepo userPrefenceRepo;
	@Autowired private MailService mailService;
	@Autowired private SimpMessagingTemplate messagingTemplate;
	@Autowired private EventPerticipantsRepo epRepo;
	@Autowired private EventRepo eventRepo;

	@Async
	public void sendPushNotification(Event event, List<Long> participentsId, NotificationType notificationtype) {
		if(participentsId == null || participentsId.isEmpty()) return;

		List<User> users = userRepo.findAllByIdIn(participentsId);

		// Check all the perticipants, who is eligible for email push notification
		List<User> eligiblePerticipants = new ArrayList<>();
		users.stream().forEach(p -> {
			Optional<UserPreference> upOp = userPrefenceRepo.findById(p.getId());
			if(upOp.isPresent() && Boolean.TRUE.equals(upOp.get().getEnabledPushNoti())) {
				eligiblePerticipants.add(p);
			}
		});

		if(eligiblePerticipants.isEmpty()) return;

		StringBuilder message = new StringBuilder();
		message.append("\nTitle: " + event.getTitle() + "\n")
		.append("Date: " + event.getEventDate().toString() + "\n")
		.append("Time: " + event.getStartTime() + "-" + event.getEndTime() + "\n")
		.append("Location: " + event.getLocation() + "\n");

		// Now send push notification to all eligible participants
		Notification notification = Notification.builder()
				.status(notificationtype)
				.title(notificationtype.getTitle())
				.message(message.toString())
				.build();

		eligiblePerticipants.stream().forEach(user -> {
			log.info("====> Sending push notification to {} with message {}", user.getFirstName() + " " + user.getLastName(), message.toString());
			messagingTemplate.convertAndSendToUser(user.getId().toString(), "/notifications", notification);
		});
	}

	@Async
	public void sendEmailNotification(Event event, List<Long> participentsId, MailType mailType) {
		if(participentsId == null || participentsId.isEmpty()) return;

		List<User> users = userRepo.findAllByIdIn(participentsId);

		// Check all the perticipants, who is eligible for email push notification
		List<User> eligiblePerticipants = new ArrayList<>();
		users.stream().forEach(p -> {
			Optional<UserPreference> upOp = userPrefenceRepo.findById(p.getId());
			if(upOp.isPresent() && Boolean.TRUE.equals(upOp.get().getEnabledEmailNoti())) {
				eligiblePerticipants.add(p);
			}
		});

		if(eligiblePerticipants.isEmpty()) return;

		// Now send email to all eligible perticipants
		for(User user : eligiblePerticipants) {
			Map<String, Object> contextData = new HashMap<>();
			contextData.put("userName", user.getFirstName() + " " + user.getLastName());
			contextData.put("event", event);

			MailReqDto reqDto = MailReqDto.builder()
					.from("tasksnest@gmail.com")
					.to(user.getEmail())
					.subject(mailType.getSubject())
					.mailType(mailType)
					.body("")
					.contextData(contextData)
					.build();

			try {

				// Update the reminder status to event perticipants
				if(MailType.EVENT_REMINDER.equals(mailType)) {
					Optional<EventPerticipants> epOp = epRepo.findById(new EventPerticipantsPK(event.getId(), user.getId()));
					if(epOp.isPresent()) {
						EventPerticipants ep = epOp.get();
						if(Boolean.FALSE.equals(ep.getIsReminderSent())) {
							mailService.sendMail(reqDto);
							ep.setIsReminderSent(true);
							epRepo.save(ep);
						} else {
							log.info("Email reminder already send for this user + " + user.getEmail());
						}
					}
				} else {
					mailService.sendMail(reqDto);
				}

			} catch (ParseErrorException | MethodInvocationException 
					| ResourceNotFoundException | CustomException
					| MessagingException | IOException e) {
				log.error(e.getMessage());
				throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// Update the event reminder sent status to true
		if(MailType.EVENT_REMINDER.equals(mailType)) {
			event.setIsReminderSent(true);
			eventRepo.save(event);
		}
	}
}
