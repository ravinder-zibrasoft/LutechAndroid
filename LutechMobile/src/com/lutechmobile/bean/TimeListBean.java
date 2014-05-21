package com.lutechmobile.bean;

import java.util.Calendar;

import android.provider.CalendarContract.Calendars;

/**
 * @author Ravindra.Prajapati
 * 
 */
public class TimeListBean {
	String hours;
	int bgcellId;
	int textColor;
	int eventTime;
	Calendar calendar;
	long calendarTimeInMillisecond;

	/**
	 * @return get added city time in millisecond
	 */
	public long getCalendarTimeInMillisecond() {
		return calendarTimeInMillisecond;
	}

	/**
	 * @return set added city time in millisecond
	 */
	public void setCalendarTimeInMillisecond(long calendarTimeInMillisecond) {
		this.calendarTimeInMillisecond = calendarTimeInMillisecond;
	}

	/**
	 * @return get added city calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * @return set added city calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return get event time
	 */
	public int getEventTime() {
		return eventTime;
	}

	/**
	 * @return set event time
	 */
	public void setEventTime(int eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * @return get hours cell color
	 */
	public int getTextColor() {
		return textColor;
	}

	/**
	 * @return set hours cell color
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return get hours
	 */
	public String getHours() {
		return hours;
	}

	/**
	 * @return set hours
	 */
	public void setHours(String hours) {
		this.hours = hours;
	}

	/**
	 * @return get unique id of cell
	 */
	public int getBgcellId() {
		return bgcellId;
	}

	/**
	 * @return set unique id of cell
	 */
	public void setBgcellId(int bgcellId) {
		this.bgcellId = bgcellId;
	}

}
