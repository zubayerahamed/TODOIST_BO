package com.zayaanit.module.documents;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Zubayer Ahamed
 * @since Jul 13, 2025
 */
@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {

	List<Document> findAllByReferenceId(Long referenceId);
}
