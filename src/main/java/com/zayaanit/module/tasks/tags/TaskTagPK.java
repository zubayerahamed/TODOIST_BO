package com.zayaanit.module.tasks.tags;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 14, 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTagPK implements Serializable {

	private static final long serialVersionUID = 4285821841405222545L;

	private Long taskId;
	private Long tagId;
}
