package com.zayaanit.todoist.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.todoist.entity.XusersZbusiness;
import com.zayaanit.todoist.entity.pk.XusersZbusinessPK;

@Repository
public interface XusersZbusinessRepo extends JpaRepository<XusersZbusiness, XusersZbusinessPK> {

	List<XusersZbusiness> findAllByZuser(Long zuser);
	Optional<XusersZbusiness> findByZuserAndZprimary(Long zuser, Boolean zprimary);
	Optional<XusersZbusiness> findByZuserAndZadmin(Long zuser, Boolean zadmin);
	Optional<XusersZbusiness> findByZuserAndZcollaborator(Long zuser, Boolean zcollaborator);
}
