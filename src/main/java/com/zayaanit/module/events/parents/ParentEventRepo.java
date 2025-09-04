package com.zayaanit.module.events.parents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentEventRepo extends JpaRepository<ParentEvent, Long> {

}
