package com.zayaanit.module.tasks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.enums.PerticipantAccess;
import com.zayaanit.enums.PerticipantType;
import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;
import com.zayaanit.module.documents.Document;
import com.zayaanit.module.documents.DocumentRepo;
import com.zayaanit.module.documents.DocumentService;
import com.zayaanit.module.tasks.perticipants.TaskPerticipants;
import com.zayaanit.module.tasks.perticipants.TaskPerticipantsRepo;
import com.zayaanit.module.tasks.subtasks.SubTask;
import com.zayaanit.module.tasks.subtasks.SubTaskRepo;
import com.zayaanit.module.tasks.subtasks.UpdateSubTaskReqDto;
import com.zayaanit.module.tasks.tags.TaskTag;
import com.zayaanit.module.tasks.tags.TaskTagRepo;

import jakarta.transaction.Transactional;

/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
@Service
public class TaskService extends BaseService {

	@Autowired private TaskRepo taskRepo;
	@Autowired private TaskPerticipantsRepo tpRepo;
	@Autowired private TaskTagRepo taskTagRepo;
	@Autowired private DocumentRepo documentRepo;
	@Autowired private SubTaskRepo subTaskRepo;
	@Autowired private DocumentService documentService;

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
		Task finaltask = taskRepo.save(task);

		// Add tags
		reqDto.getTags().stream().forEach(t -> {
			TaskTag tt = TaskTag.builder()
					.taskId(finaltask.getId())
					.tagId(t)
					.build();

			taskTagRepo.save(tt);
		});

		// Update attached documents reference with task id
		reqDto.getDocuments().stream().forEach(d -> {
			Optional<Document> documentOp = documentRepo.findById(d);
			if(documentOp.isPresent()) {
				Document document = documentOp.get();
				document.setReferenceId(finaltask.getId());
				documentRepo.save(document);
			}
		});

		// Add task creator
		TaskPerticipants tp = TaskPerticipants.builder()
				.userId(loggedinUser().getUserId())
				.taskId(finaltask.getId())
				.perticipantType(PerticipantType.CREATOR)
				.perticipantAccess(PerticipantAccess.ALL)
				.build();

		tp = tpRepo.save(tp);

		// Add task collaborators
		reqDto.getPerticipants().stream().forEach(p -> {
			TaskPerticipants collaborator = TaskPerticipants.builder()
					.userId(p)
					.taskId(finaltask.getId())
					.perticipantType(PerticipantType.CONTRIBUTOR)
					.perticipantAccess(PerticipantAccess.READ)
					.build();

			tpRepo.save(collaborator);
		});

		// Create & Add Sub Tasks
		reqDto.getSubTasks().stream().forEach(s -> {
			SubTask subTask = s.getBean();
			subTask.setTaskId(finaltask.getId());
			subTaskRepo.save(subTask);
		});

		// TODO: Send Email to all participants that a task is created and assigned them
		

		return new TaskResDto(finaltask);
	}

	@Transactional
	public TaskResDto update(UpdateTaskReqDto reqDto) throws CustomException {
		Optional<Task> taskOp = taskRepo.findById(reqDto.getId());
		if(!taskOp.isPresent()) throw new CustomException("Task not found", HttpStatus.NOT_FOUND);

		Task existObj = taskOp.get();
		BeanUtils.copyProperties(reqDto, existObj);
		Task finaltask = taskRepo.save(existObj);

		// Update documents reference
		// Remove existing documents which are not available in this request
		// Add new documents which is newly added
		List<Document> existingDocuments = documentRepo.findAllByReferenceId(finaltask.getId());
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
				document.setReferenceId(finaltask.getId());
				documentRepo.save(document);
			}
		});

		// Update tags
		taskTagRepo.deleteAllByTaskId(finaltask.getId());
		reqDto.getTags().stream().forEach(t -> {
			TaskTag tt = TaskTag.builder()
					.taskId(finaltask.getId())
					.tagId(t)
					.build();

			taskTagRepo.save(tt);
		});

		// Update perticipants
		List<TaskPerticipants> existingPerticipants = tpRepo.findAllByTaskId(finaltask.getId());
		List<TaskPerticipants> deletablePerticipants = existingPerticipants.stream().filter(d -> !reqDto.getPerticipants().contains(d.getUserId())).collect(Collectors.toList());

		List<Long> existingPerticipantsIds = existingPerticipants.stream().map(TaskPerticipants::getUserId).collect(Collectors.toList());
		List<Long> newPerticipantsIds = reqDto.getPerticipants().stream()
				.filter(id -> !existingPerticipantsIds.contains(id))
				.collect(Collectors.toList());

		deletablePerticipants.stream().forEach(d -> {
			tpRepo.delete(d);
		});

		newPerticipantsIds.stream().forEach(n -> {
			TaskPerticipants eventPerticipant = TaskPerticipants.builder()
					.userId(n)
					.taskId(finaltask.getId())
					.perticipantType(PerticipantType.CONTRIBUTOR)
					.perticipantAccess(PerticipantAccess.READ)
					.build();
			tpRepo.save(eventPerticipant);
		});

		// Update sub tasks
		List<SubTask> existingSubTasks = subTaskRepo.findAllByTaskId(finaltask.getId());
		List<Long> requestedSubTaskIds = reqDto.getSubTasks().stream().map(UpdateSubTaskReqDto::getId).collect(Collectors.toList());
		List<SubTask> deletableSubTasks = existingSubTasks.stream().filter(f -> !requestedSubTaskIds.contains(f.getId())).collect(Collectors.toList());
		deletableSubTasks.stream().forEach(d -> {
			subTaskRepo.delete(d);
		});

		reqDto.getSubTasks().stream().forEach(s -> {
			if(s.getId() == -1) {  // Newly added subtask
				SubTask subTask = SubTask.builder()
						.taskId(finaltask.getId())
						.userId(s.getUserId())
						.title(s.getTitle())
						.build();
				
				subTaskRepo.save(subTask);
			} else {  // existing subtask need to be updated
				Optional<SubTask> subTaskOp = subTaskRepo.findById(s.getId());
				if(subTaskOp.isPresent()) {
					SubTask existSubTask = subTaskOp.get();
					BeanUtils.copyProperties(s, existingSubTasks);
					subTaskRepo.save(existSubTask);
				}
			}
		});

		// Send email notification about task update 
		
		// Send email to those percipants are removed from task

		return new TaskResDto(finaltask);
	}

	@Transactional
	public void delete(Long id) {
		Optional<Task> taskOp = taskRepo.findById(id);
		if(!taskOp.isPresent()) throw new CustomException("Task not found", HttpStatus.NOT_FOUND);

		// Remove all tags
		taskTagRepo.deleteAllByTaskId(id);

		// Remove all documents
		documentService.deleteAllByReferenceId(id);

		// Delete all perticipants relations
		tpRepo.deleteAllByTaskId(id);

		// Delete all subtasks
		subTaskRepo.deleteAllByTaskId(id);

		// Delete task
		taskRepo.delete(taskOp.get());
	}
}
