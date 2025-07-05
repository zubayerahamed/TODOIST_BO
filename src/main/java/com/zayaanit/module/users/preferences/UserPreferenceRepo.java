package com.zayaanit.module.users.preferences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Zubayer Ahamed
 * @since Jul 5, 2025
 */
@Repository
public interface UserPreferenceRepo extends JpaRepository<UserPreference, Long> {

}
