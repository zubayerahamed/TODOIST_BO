package com.zayaanit.module.events.repeaters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepeaterRepo extends JpaRepository<EventRepeater, Long> {

}
