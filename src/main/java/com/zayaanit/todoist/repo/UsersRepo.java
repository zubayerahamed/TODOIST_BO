package com.zayaanit.todoist.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

	Optional<Users> findByEmail(String email);
}
