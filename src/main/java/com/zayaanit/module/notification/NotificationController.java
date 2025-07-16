package com.zayaanit.module.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

/**
 * Zubayer Ahamed
 * @since Jul 16, 2025
 */
@RestApiController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	@Autowired
	private AsyncNotificationService notificationService;

	@PostMapping("/subscribe")
	public ResponseEntity<SuccessResponse<WebPushSubscriptionResDto>> subscribe(@RequestBody PushSubscriptionRequest request){
		WebPushSubscriptionResDto resData = notificationService.saveWebPushSubscription(request.getUserId(), request.getSubscription());
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}
}
