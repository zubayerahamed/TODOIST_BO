package com.zayaanit.module.page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;
import com.zayaanit.module.events.EventResDto;
import com.zayaanit.module.events.EventService;

/**
 * Zubayer Ahamed
 * @since Aug 28, 2025
 */
@RestApiController
@RequestMapping("/api/v1/pages/completed")
public class CompletedController {

	@Autowired
	private EventService eventService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<EventResDto>>> getAllCompletedEvents(){
		List<EventResDto> resData = eventService.getAllEventsFromAllProjects(null, true);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/count")
	public ResponseEntity<SuccessResponse<Long>> getCountOfAllTodaysEvents(){
		long resData = eventService.getCountOfAllEventsFromAllProjects(null, true);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}
}
