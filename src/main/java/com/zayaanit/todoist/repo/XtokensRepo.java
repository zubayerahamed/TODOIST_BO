package com.zayaanit.todoist.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.Xtokens;

@Repository
public interface XtokensRepo extends JpaRepository<Xtokens, Long> {

	List<Xtokens> findAllByZuserAndXrevokedAndXexpired(Long zuser, boolean xrevoked, boolean xexpired);
	Optional<Xtokens> findByXtoken(String xtoken);
}
