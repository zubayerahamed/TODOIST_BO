package com.zayaanit.module.events;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.zayaanit.enums.ResponseStatusType;
import com.zayaanit.model.ResponseBuilder;
import com.zayaanit.model.SuccessResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

	@Query(value = """
			SELECT * 
			FROM events 
			WHERE is_reminder_sent = 0 
			  AND DATEADD(SECOND, 
			              DATEDIFF(SECOND, 0, start_time), 
			              CAST(event_date AS DATETIME)
			          ) >= GETDATE()
		""", nativeQuery = true)
	List<Event> findAllPendingReminders();

	List<Event> findAllByProjectId(Long projectId);
	List<Event> findByEventDate(LocalDate eventDate);

}
