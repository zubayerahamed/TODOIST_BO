package com.zayaanit.module.events.repeaters;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRepeaterResDto {

	private Long id;
	private Long parentEventId;
	private Integer repeatEvery;  // 1, 2, 3, 4
	private EventRepeatType repeatType;   // Day, Week, Month, Year
	private Boolean skipWeekends;

	private Boolean daysSat;
	private Boolean daysSun;
	private Boolean daysMon;
	private Boolean daysTue;
	private Boolean daysWed;
	private Boolean daysThu;
	private Boolean daysFri;

	private DataType dataType;  // used only for month, year

	private String fixedDates;
	private WeekDayPosition weekDayPosition;   // First, Second, Third, Fourth, Fifth, Last
	private WeekDay weekDay;   // SAt, Sun, Mon, Tue, Wed, Thu, Fri

	private String fixedDate;
	private String fixedMonth;
	private WeekDayOfMonth weekDayOfMonth;  // JAN, FEB, MAR, APR

	private LocalDate startDate;
	private LocalDate endDate;

	public static EventRepeaterResDto from(EventRepeater obj) {
		EventRepeaterResDto dto = new EventRepeaterResDto();
		BeanUtils.copyProperties(obj, dto);
		return dto;
	}
}