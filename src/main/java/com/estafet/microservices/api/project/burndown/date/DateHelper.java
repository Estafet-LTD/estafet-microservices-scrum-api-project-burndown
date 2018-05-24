package com.estafet.microservices.api.project.burndown.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateHelper {

	public static Calendar toCalendar(String calendarString) {
		try {
			Calendar cal = DateHelper.newCalendar();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(calendarString));
			return cal;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toCalendarString(Calendar calendar) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	public static Calendar newCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static boolean isWorkingDay(Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
			return false;
		return true;
	}

	public static Calendar getWorkingDay(Calendar day) {
		if (isWorkingDay(day)) {
			return day;
		} else {
			day.add(Calendar.DAY_OF_MONTH, 1);
			return getWorkingDay(day);
		}
	}

	public static String getNextWorkingDay(String day) {
		return toCalendarString(getWorkingDay(toCalendar(day)));
	}

	public static String increment(String day) {
		Calendar cal = toCalendar(day);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return toCalendarString(cal);
	}
	
	public static List<String> getSprintDays(String startDate, Integer noDays) {
		List<String> workingDays = new ArrayList<String>(noDays);
		String day = getNextWorkingDay(startDate);
		int i = 0;
		while (i < noDays) {
			workingDays.add(day);
			day = getNextWorkingDay(DateHelper.increment(day));
			i++;
		}
		return workingDays;
	}
	
	public static String getSprintDay(String startDate, Integer noDays) {
		String today = toCalendarString(DateHelper.newCalendar());
		for (String day : getSprintDays(startDate, noDays)) {
			if (day.equals(today)) {
				return today;
			}
		}
		return getSprintDays(startDate, noDays).get(0);
	}

	public static List<CalculatedSprint> calculateSprints(String today, int noDays, int noSprints) {
		CalculatedSprint calculatedSprint = new CalculatedSprint(today, noDays);
		for (int i = 1; i < noSprints; i++) {
			calculatedSprint.addSprint();
		}
		return calculatedSprint.toList();
	}

	public static List<CalculatedSprint> calculateSprints(int noDays, int noSprints) {
		return calculateSprints(toCalendarString(newCalendar()), noDays, noSprints);
	}

}
