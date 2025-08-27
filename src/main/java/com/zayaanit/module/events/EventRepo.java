package com.zayaanit.module.events;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
	List<Event> findByEventDateAfter(LocalDate eventDate);


}
