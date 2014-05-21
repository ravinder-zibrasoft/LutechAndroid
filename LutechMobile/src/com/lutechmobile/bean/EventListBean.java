package com.lutechmobile.bean;

/**
 * @author Ravindra.Prajapati
 * 
 */
public class EventListBean {
	String eventName;
	String EventTime;
	String eventId;
	long eventStartTime;
	long eventEndTime;
	int startMin;
	/**
	 * @return get event starting minute
	 */
	public int getStartMin() {
		return startMin;
	}
	/**
	 * @return set event starting minute
	 */
	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}
	/**
	 * @return get event ending  minute
	 */
	public int getEndMin() {
		return endMin;
	}
	/**
	 * @return set event ending  minute
	 */
	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}
	int endMin;

	/**
	 * @return
	 * get added event start time 
	 */
	public long getEventStartTime() {
		return eventStartTime;
	}
	/**
	 * @return
	 * set added event start time 
	 */
	public void setEventStartTime(long eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	/**
	 * @return
	 * get added event end time 
	 */
	public long getEventEndTime() {
		return eventEndTime;
	}
	/**
	 * @return
	 * set added event start time 
	 */
	public void setEventEndTime(long eventEndTime) {
		this.eventEndTime = eventEndTime;
	}
	/**
	 * @return
	 * get added event unique id
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @return
	 * set added event unique id
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return
	 * get added event name
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @return
	 * set added event name
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * @return
	 * get added start hour 
	 */
	public String getEventTime() {
		return EventTime;
	}
	/**
	 * @return
	 * set added start hour 
	 */
	public void setEventTime(String eventTime) {
		EventTime = eventTime;
	}

}
