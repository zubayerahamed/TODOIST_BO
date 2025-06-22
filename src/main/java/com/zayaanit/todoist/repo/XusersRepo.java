package com.zayaanit.todoist.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.Xusers;
import com.zayaanit.todoist.entity.pk.XusersPK;

@Repository
public interface XusersRepo extends JpaRepository<Xusers, XusersPK> {

	Optional<Xusers> findByZemail(String zemail);
}
