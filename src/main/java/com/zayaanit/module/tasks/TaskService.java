package com.zayaanit.module.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.mail.MailReqDto;
import com.zayaanit.mail.MailService;
import com.zayaanit.mail.MailType;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.reminder.ReminderService;
import com.zayaanit.module.users.UserRepo;
import com.zayaanit.module.users.UserService;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Service
public class TaskService extends BaseService {

	@Autowired private TaskRepo taskRepo;
	@Autowired private ReminderService reminderService;
	@Autowired private MailService mailService;
	@Autowired private UserRepo userRepo;

	public List<TaskResDto> getAllByProjectId(Long projectId) {
		List<Task> taskList = taskRepo.findAllByProjectId(projectId);
		return taskList.stream().map(TaskResDto::new).collect(Collectors.toList());
	}

	public TaskResDto  findById(Long id) throws CustomException {
		Optional<Task> taskOp = taskRepo.findById(id);
		if(!taskOp.isPresent()) throw new CustomException("Task not found", HttpStatus.NOT_FOUND);
		return new TaskResDto(taskOp.get());
	}

	@Transactional
	public TaskResDto create(CreateTaskReqDto reqDto) throws CustomException {
		Task task = reqDto.getBean();
		task.setIsCompleted(false);
		task.setIsPartiallyCompleted(false);
		task.setIsReminderSent(false);
		Task finaltask = taskRepo.save(task);

		// TODO: add task assignee

		// After creating task, if it is event type, then schedule it for reminder
		setTaskForReminder(finaltask);

		return new TaskResDto(finaltask);
	}

	private void setTaskForReminder(final Task task) {
		reminderService.scheduleReminder(task, () -> {
			System.out.println("Reminder for task : " + task.getTitle());

			Map<String, Object> contextData = new HashMap<>();
			contextData.put("userName", "Zubayer Ahamed");
			contextData.put("task", task);

			MailReqDto reqDto = MailReqDto.builder()
					.from("zubayerahamed.freelancer@gmail.com")
					.to("zubayerahamed.freelancer@gmail.com")
					.subject("Test Email")
					.mailType(MailType.EVENT_REMINDER)
					.body("Nothing to say")
					.contextData(contextData)
					.build();
			try {
				mailService.sendMail(reqDto);
			} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | CustomException
					| MessagingException | IOException e) {
				throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Updat the task reminder status to be sent already
			task.setIsReminderSent(true);
			taskRepo.save(task);
		});
	}

	@Transactional
	public TaskResDto update(UpdateTaskReqDto reqDto) throws CustomException {
		Optional<Task> taskOp = taskRepo.findById(reqDto.getId());
		if(!taskOp.isPresent()) throw new CustomException("Task not found", HttpStatus.NOT_FOUND);

		Task existObj = taskOp.get();
		BeanUtils.copyProperties(reqDto, existObj);
		Task finaltask = taskRepo.save(existObj);

		// After creating task, if it is event type, then schedule it for reminder
		setTaskForReminder(finaltask);

		return new TaskResDto(finaltask);
	}

	@Transactional
	public void delete(Long id) {
		Optional<Task> taskOp = taskRepo.findById(id);
		if(!taskOp.isPresent()) throw new CustomException("Task not found", HttpStatus.NOT_FOUND);

		reminderService.cancelScheduledReminder(taskOp.get().getId());
		taskRepo.delete(taskOp.get());
	}

	public void rescheduleAllReminders() {
		List<Task> futureTasks = taskRepo.findAllPendingReminders();
		futureTasks.forEach(t -> {
			setTaskForReminder(t);
		});
	}
}
