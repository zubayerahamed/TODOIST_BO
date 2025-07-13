package com.zayaanit.mail;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Zubayer Ahamed
 * @since Jul 13, 2025
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailReqDto {

	private String from;
	private String to;
	private String subject;
	private List<String> cc;
	private List<String> bcc;
	private String body;
	private String replyTo;
	private Map<String, Object> contextData;
	private MailType mailType;
}
