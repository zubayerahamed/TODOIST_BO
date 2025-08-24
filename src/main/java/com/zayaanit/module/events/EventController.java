package com.zayaanit.module.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;

import jakarta.validation.Valid;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
@RestApiController
@RequestMapping("/api/v1/events")
public class EventController {

	@Autowired
	private EventService eventService;

	@GetMapping("/all/{projectId}")
	public ResponseEntity<SuccessResponse<List<EventResDto>>> getAll(@PathVariable Long projectId){
		List<EventResDto> resData = eventService.getAllByProjectId(projectId);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<SuccessResponse<EventResDto>> find(@PathVariable Long id){
		EventResDto resData = eventService.findById(id);
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	} 

	@PostMapping
	public ResponseEntity<SuccessResponse<EventResDto>> create(@Valid @RequestBody CreateEventReqDto reqDto){
		EventResDto resData = eventService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@PutMapping
	public ResponseEntity<SuccessResponse<EventResDto>> update(@Valid @RequestBody UpdateEventReqDto reqDto){
		EventResDto resData = eventService.update(reqDto);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<EventResDto>> delete(@PathVariable Long id){
		eventService.delete(id);
		return ResponseBuilder.build(ResponseStatusType.DELETE_NO_CONTENT, null);
	}

	@PutMapping("/complete/{id}")
	public ResponseEntity<SuccessResponse<EventResDto>> complete(@PathVariable Long id){
		EventResDto resData = eventService.complete(id);
		return ResponseBuilder.build(ResponseStatusType.UPDATE_SUCCESS, resData);
	}

	@GetMapping("/today")
	public ResponseEntity<SuccessResponse<List<EventResDto>>> getTodayEvents() {
		List<EventResDto> resData = eventService.getTodayEvents();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}

	@GetMapping("/upcoming")
	public ResponseEntity<SuccessResponse<List<EventResDto>>> getUpcomingEvents() {
		List<EventResDto> resData = eventService.getUpcomingEvents();
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, resData);
	}
}
