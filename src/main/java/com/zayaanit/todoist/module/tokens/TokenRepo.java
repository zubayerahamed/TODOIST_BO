package com.zayaanit.todoist.module.tokens;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

	List<Token> findAllByUserIdAndRevokedAndExpired(Long userId, boolean revoked, boolean expired);
	Optional<Token> findByToken(String token);
}
