package com.zayaanit.module.notification;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Slf4j
@Service
public class AsyncNotificationService {

	@Value("${vapid.public-key}")
	private String publicKey;
	@Value("${vapid.private-key}")
	private String privateKey;
	@Value("${vapid.subject}")
	private String subject;

	@Autowired private UserRepo userRepo;
	@Autowired private UserPreferenceRepo userPrefenceRepo;
	@Autowired private MailService mailService;
	@Autowired private SimpMessagingTemplate messagingTemplate;
	@Autowired private EventPerticipantsRepo epRepo;
	@Autowired private EventRepo eventRepo;
	@Autowired private WebPushSubscriptionRepo webPushSubscriptionRepo;

	@Transactional
	public WebPushSubscriptionResDto saveWebPushSubscription(Long userId, Subscription subscription) throws CustomException {
		WebPushSubscription wps = WebPushSubscription.builder()
				.userId(userId)
				.endpoint(subscription.endpoint)
				.publicKey(subscription.keys.p256dh)
				.auth(subscription.keys.auth)
				.build();

		wps = webPushSubscriptionRepo.save(wps);

		return new WebPushSubscriptionResDto(wps);
	}

	@Async
	public void sendBrowserPushNotification(Event event, List<Long> participentsId, NotificationType notificationtype) {
		if(participentsId == null || participentsId.isEmpty()) return;

		List<User> users = userRepo.findAllByIdIn(participentsId);

		// Check all the perticipants, who is eligible for push notification
		List<User> eligiblePerticipants = new ArrayList<>();
		users.stream().forEach(p -> {
			Optional<UserPreference> upOp = userPrefenceRepo.findById(p.getId());
			if(upOp.isPresent() && Boolean.TRUE.equals(upOp.get().getEnabledBrowserNoti())) {
				eligiblePerticipants.add(p);
			}
		});

		if(eligiblePerticipants.isEmpty()) return;

		// Now send push notification to all eligible participants
		Notification noti = Notification.builder()
				.status(notificationtype)
				.title(notificationtype.getTitle())
				.message("==<| " + event.getTitle() + " |>==\nDate: " + event.getEventDate() + "\nTime: " + event.getStartTime() + " - " + event.getEndTime() + "\nLocation: " + event.getLocation())
				.build();

		eligiblePerticipants.stream().forEach(user -> {
			List<WebPushSubscription> wpsList = webPushSubscriptionRepo.findAllByUserId(user.getId());
			for(WebPushSubscription wps : wpsList) {
				Subscription sub = wps.getSubscription();

				if (sub != null) {
					log.info("====> Sending web push notification to {}", user.getFirstName() + " " + user.getLastName());

					try {
						nl.martijndwars.webpush.Notification notification = new nl.martijndwars.webpush.Notification(sub, createPayload(noti));

						PushService pushService = new PushService();
						pushService.setPublicKey(publicKey);
						pushService.setPrivateKey(privateKey);
						pushService.setSubject(subject);

						pushService.send(notification);

					} catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}
				}
			}
		});

	}

	private String createPayload(Notification notification) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(notification);
	}

	@Async
	public void sendSocketPushNotification(Event event, List<Long> participentsId, NotificationType notificationtype) {
		if(participentsId == null || participentsId.isEmpty()) return;

		List<User> users = userRepo.findAllByIdIn(participentsId);

		// Check all the perticipants, who is eligible for push notification
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
			messagingTemplate.convertAndSendToUser(user.getId().toString(), "/notification", notification);
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
