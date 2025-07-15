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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.mail.MailReqDto;
import com.zayaanit.mail.MailService;
import com.zayaanit.mail.MailType;
import com.zayaanit.module.events.Event;
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
		for(User perticipant : eligiblePerticipants) {
			Map<String, Object> contextData = new HashMap<>();
			contextData.put("userName", perticipant.getFirstName() + " " + perticipant.getLastName());
			contextData.put("event", event);

			MailReqDto reqDto = MailReqDto.builder()
					.from("tasksnest@gmail.com")
					.to(perticipant.getEmail())
					.subject(mailType.getSubject())
					.mailType(mailType)
					.body("")
					.contextData(contextData)
					.build();

			try {
				mailService.sendMail(reqDto);
			} catch (ParseErrorException | MethodInvocationException 
					| ResourceNotFoundException | CustomException
					| MessagingException | IOException e) {
				log.error(e.getMessage());
				throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
}
