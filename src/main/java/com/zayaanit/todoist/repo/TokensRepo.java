package com.zayaanit.todoist.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.Tokens;

@Repository
public interface TokensRepo extends JpaRepository<Tokens, Long> {

	List<Tokens> findAllByUserIdAndRevokedAndExpired(Long userId, boolean revoked, boolean expired);
	Optional<Tokens> findByToken(String token);
}
