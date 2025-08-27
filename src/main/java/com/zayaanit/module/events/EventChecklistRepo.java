package com.zayaanit.module.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventChecklistRepo extends JpaRepository<EventChecklist, Long> {

	List<EventChecklist> findAllByEventId(Long eventId);
}
