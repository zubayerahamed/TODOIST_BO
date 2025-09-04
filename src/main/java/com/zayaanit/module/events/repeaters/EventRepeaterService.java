package com.zayaanit.module.events.repeaters;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.zayaanit.exception.CustomException;
import com.zayaanit.module.BaseService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Zubayer Ahamed
 * @since Sep 4, 2025
 */
@Slf4j
@Service
public class EventRepeaterService extends BaseService {

	@Autowired private EventRepeaterRepo erRepo;

	@Transactional
	public EventRepeaterResDto create(CreateEventRepeaterReqDto reqDto) throws CustomException {
		// Validation
		if(reqDto.getStartDate() == null) {
			throw new CustomException("Starting from date required", HttpStatus.BAD_REQUEST);
		}

		if(reqDto.getStartDate().isBefore(LocalDate.now())) {
			throw new CustomException("Starting date can't be past date", HttpStatus.BAD_REQUEST);
		}

		if(reqDto.getEndDate() != null) {
			if(!reqDto.getEndDate().isAfter(reqDto.getStartDate())) {
				throw new CustomException("End date must be after start date", HttpStatus.BAD_REQUEST);
			}
		}

		if(reqDto.getRepeatEvery() == null || reqDto.getRepeatEvery() < 0) {
			throw new CustomException("Invalid repeat every value.", HttpStatus.BAD_REQUEST);
		}

		if(reqDto.getRepeatType() == null) {
			throw new CustomException("Repeat type required", HttpStatus.BAD_REQUEST);
		}

		if(EventRepeatType.DAY.equals(reqDto.getRepeatType())) {
			resetWeeksValues(reqDto);
			resetMonthsValues(reqDto);
			resetYearsValues(reqDto);
			reqDto.setWeekDayPosition(null);
			reqDto.setWeekDay(null);
			reqDto.setDataType(null);
		}

		if(EventRepeatType.WEEK.equals(reqDto.getRepeatType())) {
			resetDaysValues(reqDto);
			resetMonthsValues(reqDto);
			resetYearsValues(reqDto);
			reqDto.setWeekDayPosition(null);
			reqDto.setWeekDay(null);
			reqDto.setDataType(null);

			if(
				Boolean.FALSE.equals(reqDto.getDaysSat())
				&& Boolean.FALSE.equals(reqDto.getDaysSun())
				&& Boolean.FALSE.equals(reqDto.getDaysMon())
				&& Boolean.FALSE.equals(reqDto.getDaysTue())
				&& Boolean.FALSE.equals(reqDto.getDaysWed())
				&& Boolean.FALSE.equals(reqDto.getDaysThu())
				&& Boolean.FALSE.equals(reqDto.getDaysFri())
			) {
				throw new CustomException("Atleast one days of week required", HttpStatus.BAD_REQUEST);
			}
		}

		if(EventRepeatType.MONTH.equals(reqDto.getRepeatType())) {
			resetDaysValues(reqDto);
			resetWeeksValues(reqDto);
			resetYearsValues(reqDto);

			if(reqDto.getDataType() == null) {
				throw new CustomException("Data type selection required", HttpStatus.BAD_REQUEST);
			}

			if(DataType.FIXED_DATE.equals(reqDto.getDataType())) {
				if(StringUtils.isBlank(reqDto.getFixedDates())) {
					throw new CustomException("Atleast one date selection required", HttpStatus.BAD_REQUEST);
				}
			}

			if(DataType.WEEK_DAY_OF_MONTH.equals(reqDto.getDataType())) {
				if(reqDto.getWeekDayPosition() == null) {
					throw new CustomException("Week day postition required", HttpStatus.BAD_REQUEST);
				}
				if(reqDto.getWeekDay() == null) {
					throw new CustomException("Week day selection required", HttpStatus.BAD_REQUEST);
				}
			}
		}

		if(EventRepeatType.YEAR.equals(reqDto.getRepeatType())) {
			resetDaysValues(reqDto);
			resetWeeksValues(reqDto);
			resetMonthsValues(reqDto);

			if(reqDto.getDataType() == null) {
				throw new CustomException("Data type selection required", HttpStatus.BAD_REQUEST);
			}

			if(DataType.FIXED_DATE.equals(reqDto.getDataType())) {
				reqDto.setWeekDayPosition(null);
				reqDto.setWeekDay(null);

				if(StringUtils.isBlank(reqDto.getFixedDate())) {
					throw new CustomException("Fixed date required", HttpStatus.BAD_REQUEST);
				}
				if(StringUtils.isBlank(reqDto.getFixedMonth())) {
					throw new CustomException("Fixed month selection required", HttpStatus.BAD_REQUEST);
				}
			}

			if(DataType.WEEK_DAY_OF_YEAR.equals(reqDto.getDataType())) {
				reqDto.setFixedDate(null);
				reqDto.setFixedMonth(null);

				if(reqDto.getWeekDayPosition() == null) {
					throw new CustomException("Week day postition required", HttpStatus.BAD_REQUEST);
				}
				if(reqDto.getWeekDay() == null) {
					throw new CustomException("Week day selection required", HttpStatus.BAD_REQUEST);
				}
				if(reqDto.getWeekDayOfMonth() == null) {
					throw new CustomException("Week day of month selection required", HttpStatus.BAD_REQUEST);
				}
			}
		}

		EventRepeater er = reqDto.getBean();
		er = erRepo.save(er);

		return EventRepeaterResDto.from(er);
	}

	private void resetDaysValues(CreateEventRepeaterReqDto reqDto) {
		reqDto.setSkipWeekends(false);
	}

	private void resetWeeksValues(CreateEventRepeaterReqDto reqDto) {
		reqDto.setDaysSat(false);
		reqDto.setDaysSun(false);
		reqDto.setDaysMon(false);
		reqDto.setDaysTue(false);
		reqDto.setDaysWed(false);
		reqDto.setDaysThu(false);
		reqDto.setDaysFri(false);
	}

	private void resetMonthsValues(CreateEventRepeaterReqDto reqDto) {
		reqDto.setFixedDates(null);
	}

	private void resetYearsValues(CreateEventRepeaterReqDto reqDto) {
		reqDto.setFixedDate(null);
		reqDto.setFixedMonth(null);
		reqDto.setWeekDayOfMonth(null);
	}
}
