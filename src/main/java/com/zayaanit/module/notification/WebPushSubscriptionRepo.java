package com.zayaanit.module.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPushSubscriptionRepo extends JpaRepository<WebPushSubscription, Long> {

	public List<WebPushSubscription> findAllByUserId(Long userId);
}
