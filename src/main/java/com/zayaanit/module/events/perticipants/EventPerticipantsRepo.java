package com.zayaanit.module.events.perticipants;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPerticipantsRepo extends JpaRepository<EventPerticipants, EventPerticipantsPK> {

	List<EventPerticipants> findAllByEventId(Long eventId);

	void deleteAllByEventId(Long eventId);
}
