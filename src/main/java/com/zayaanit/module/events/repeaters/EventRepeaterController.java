package com.zayaanit.module.events.repeaters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

/**
 * Zubayer Ahamed
 * @since Sep 4, 2025
 */
@RestApiController
@RequestMapping("/api/v1/event-repeaters")
public class EventRepeaterController {

	@Autowired private EventRepeaterService eventRepeaterService;

	@PostMapping
	public ResponseEntity<SuccessResponse<EventRepeaterResDto>> create(@Valid @RequestBody CreateEventRepeaterReqDto reqDto){
		EventRepeaterResDto resData = eventRepeaterService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}
}
