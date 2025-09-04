package com.zayaanit.module.events.repeaters;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRepeaterReqDto {

	private Long parentEventId;
	private Integer repeatEvery;  // 1, 2, 3, 4
	private EventRepeatType repeatType;   // Day, Week, Month, Year
	private Boolean skipWeekends;

	// Weeks
	private Boolean daysSat;
	private Boolean daysSun;
	private Boolean daysMon;
	private Boolean daysTue;
	private Boolean daysWed;
	private Boolean daysThu;
	private Boolean daysFri;

	private DataType dataType;   // used only for month, year

	// Months
	private String fixedDates;
	private WeekDayPosition weekDayPosition;   // First, Second, Third, Fourth, Fifth, Last
	private WeekDay weekDay;   // SAt, Sun, Mon, Tue, Wed, Thu, Fri

	private String fixedDate;
	private String fixedMonth;
	private WeekDayOfMonth weekDayOfMonth;  // JAN, FEB, MAR, APR

	private LocalDate startDate;
	private LocalDate endDate;

	public EventRepeater getBean() {
		return EventRepeater.builder()
				.parentEventId(parentEventId)
				.repeatEvery(repeatEvery)
				.repeatType(repeatType)
				.skipWeekends(skipWeekends)
				.daysSat(daysSat)
				.daysSun(daysSun)
				.daysMon(daysMon)
				.daysTue(daysTue)
				.daysThu(daysThu)
				.daysFri(daysFri)
				.fixedDates(fixedDates)
				.weekDayPosition(weekDayPosition)
				.weekDay(weekDay)
				.fixedDate(fixedDate)
				.fixedMonth(fixedMonth)
				.weekDayOfMonth(weekDayOfMonth)
				.startDate(startDate)
				.endDate(endDate)
				.build();
	}
}