package com.lutechmobile.bean;

import java.util.Calendar;

/**
 * @author Ravindra.Prajapati
 * 
 */
public class TimeZoneCalendarBean {
	Calendar calendar;
	String cityName;
	String GMTTime;
	boolean isHalfCellAddedFirst;
	boolean isHalfCellAddedLast;
	boolean isHalfCell;
	long timeDiff;

	/**
	 * @return get the current city and other city time difference
	 */
	public long getTimeDiff() {
		return timeDiff;
	}
	/**
	 * @return set the current city and other city time difference
	 */
	public void setTimeDiff(long timeDiff) {
		this.timeDiff = timeDiff;
	}

	/**
	 * @return if return true then half cell added in starting point
	 */
	public boolean isHalfCellAddedFirst() {
		return isHalfCellAddedFirst;
	}

	/**
	 * @return set half cell in first
	 */
	public void setHalfCellAddedFirst(boolean isHalfCellAddedFirst) {
		this.isHalfCellAddedFirst = isHalfCellAddedFirst;
	}

	/**
	 * @return if return true then half cell added in ending point
	 */
	public boolean isHalfCellAddedLast() {
		return isHalfCellAddedLast;
	}

	/**
	 * @return set half cell in last
	 */
	public void setHalfCellAddedLast(boolean isHalfCellAddedLast) {
		this.isHalfCellAddedLast = isHalfCellAddedLast;
	}

	/**
	 * @return true then half cell needed
	 */
	public boolean isHalfCell() {
		return isHalfCell;
	}

	/**
	 * @return set half cell needed or not
	 */
	public void setHalfCell(boolean isHalfCell) {
		this.isHalfCell = isHalfCell;
	}

	/**
	 * @return get GMT time of added city
	 */
	public String getGMTTime() {
		return GMTTime;
	}

	/**
	 * @return set GMT time of added city
	 */
	public void setGMTTime(String gMTTime) {
		this.GMTTime = gMTTime;
	}

	/**
	 * @return get calendar of added city
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * @return set calendar of added city
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return get name of added city
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @return set name of added city
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
