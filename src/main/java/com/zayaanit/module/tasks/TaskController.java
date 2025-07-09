package com.zayaanit.module.tasks;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import com.zayaanit.module.RestApiController;
import com.zayaanit.module.reminder.ReminderService;

import jakarta.validation.Valid;

/**
 * Zubayer Ahamed
 * 
 * @since Jul 3, 2025
 */
@RestApiController
@RequestMapping("/api/v1/tasks")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@PostMapping
	public ResponseEntity<SuccessResponse<CreateTaskResDto>> create(@Valid @RequestBody CreateTaskReqDto reqDto){
		CreateTaskResDto resData = taskService.create(reqDto);
		return ResponseBuilder.build(ResponseStatusType.CREATE_SUCCESS, resData);
	}

	@GetMapping("/sample")
	public ResponseEntity<SuccessResponse<String>> runTask() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime taskTime = now.plusMinutes(6);

//		Task task = Task.builder()
//				.id(Long.valueOf(1))
//				.title("Prepare Meeting Notes")
//				.description("Summarize key points from client call")
//				.email("user@example.com")
//				.taskTime(taskTime)
//				.reminderBefore(5) // 5 minutes before task time
//				.reminderSent(false)
//				.completed(false)
//				.createdAt(now)
//				.updatedAt(now)
//				.build();

//		reminderService.scheduleReminder(task, () -> {
//			// Send reminder through email, push noti, sms
//			System.out.println("===========> Reminder sent for task : " + task.getTitle());
//			// Update DB to set reminderSent = true
//		});
		return ResponseBuilder.build(ResponseStatusType.READ_SUCCESS, "Runnig triggered");
	}
}
