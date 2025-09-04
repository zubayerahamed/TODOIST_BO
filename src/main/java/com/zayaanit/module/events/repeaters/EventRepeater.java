package com.zayaanit.module.events.repeaters;

import java.time.LocalDate;

import com.zayaanit.model.AbstractModel;

import jakarta.persistence.Column;
/**
 * Zubayer Ahamed
 * @since Jul 3, 2025
 */
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_repeater")
@EqualsAndHashCode(callSuper = true)
public class EventRepeater extends AbstractModel<Long> {

	private static final long serialVersionUID = 2572842083641054917L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "parent_event_id")
	private Long parentEventId;    // if it is null, then this record shoud remove through schedular

	@Column(name = "repeat_every", length = 20)
	private Integer repeatEvery;  // 1, 2, 3, 4

	@Enumerated(EnumType.STRING)
	@Column(name = "repeat_type", length = 20)
	private EventRepeatType repeatType;   // Day, Week, Month, Year

	@Column(name = "skip_weekends", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean skipWeekends;

	@Column(name = "days_sat", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysSat;
	@Column(name = "days_sun", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysSun;
	@Column(name = "days_mon", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysMon;
	@Column(name = "days_tue", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysTue;
	@Column(name = "days_wed", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysWed;
	@Column(name = "days_thu", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysThu;
	@Column(name = "days_fri", length = 1, nullable = false, columnDefinition = "BIT DEFAULT 0")
	private Boolean daysFri;

	@Enumerated(EnumType.STRING)
	@Column(name = "data_type", length = 20)
	private DataType dataType;   // used only for month, year

	@Column(name = "fixed_dates", length = 100)
	private String fixedDates;
	@Enumerated(EnumType.STRING)
	@Column(name = "week_day_position", length = 10)
	private WeekDayPosition weekDayPosition;   // First, Second, Third, Fourth, Fifth, Last
	@Enumerated(EnumType.STRING)
	@Column(name = "week_day", length = 10)
	private WeekDay weekDay;   // SAt, Sun, Mon, Tue, Wed, Thu, Fri

	@Column(name = "fixed_date", length = 10)
	private String fixedDate;
	@Column(name = "fixed_month", length = 10)
	private String fixedMonth;
	@Enumerated(EnumType.STRING)
	@Column(name = "week_day_of_month", length = 10)
	private WeekDayOfMonth weekDayOfMonth;  // JAN, FEB, MAR, APR

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;
}