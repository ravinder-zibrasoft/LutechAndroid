package com.lutechmobile.view;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.lutechmobile.bean.EventListBean;
import com.lutechmobile.bean.TimeListBean;
import com.lutechmobile.component.EventWriter;
import com.lutechmobile.ui.CategoryViewScreen;
import com.lutechmobile.util.Constants;
import com.lutechmobile.util.TimezoneUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class EventView extends View implements Constants {
	private Paint paint;
	private Vector vctObject;
	private int eventCellH;
	private Context context;
	private Hashtable<String, Vector<EventListBean>> hashEvent;
	private Vector<TimeListBean> vctDateToShowFormat;
	private Activity timezoActivity;
    private Vector<EventWriter> vctEventWriter ;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public EventView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paint = new Paint();
		requestFocus();
		setFocusableInTouchMode(true);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param obj
	 *            Data from callling class
	 */
	public void setResource(Object obj, Activity timezoActivity) {
		this.timezoActivity = timezoActivity;
		if (obj != null) {
			vctObject = (Vector) obj;
			eventCellH = (Integer) vctObject.elementAt(0);
			hashEvent = (Hashtable<String, Vector<EventListBean>>) vctObject
					.elementAt(1);

			vctDateToShowFormat = (Vector<TimeListBean>) vctObject.elementAt(2);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		drawBackground(canvas);
		
		drawEvents(canvas);
	}

	private void drawEvents(Canvas canvas) {
		Vector<EventWriter> vctEventWriter =  new Vector<EventWriter>();
		int textMargin=TimezoneUtil.convertPxtoDip(2, context);
		for (int i = 0; i < time_Hours_ResId.length; i++) {

			if (vctDateToShowFormat != null && vctDateToShowFormat.size() > 0) {
				TimeListBean bean = vctDateToShowFormat.elementAt(i);
				
				if (hashEvent.containsKey(("" + bean.getEventTime()))) {
					Vector<EventListBean> vctEvents = hashEvent.get(""
							+ bean.getEventTime());
					if (vctEvents != null) {
						int noOfEvents = vctEvents.size();
						int startX = getWidth() / noOfEvents;
						
						for (int line = 0; line < noOfEvents; line++) {
							int rectX =  (((getWidth()-1) / noOfEvents)*line);
							EventListBean evListBean = vctEvents
									.elementAt(line);
							
							canvas.drawLine(startX-textMargin, i * eventCellH, startX-textMargin, i
									* eventCellH + eventCellH, paint);

							/*Log.i("line", ""+line+"   "+"startX == "+startX+"  start yy  "+(i * eventCellH)+"    heihjt of line  == "+(i
									* eventCellH + eventCellH));  */ 
							
							long timeDiff = (evListBean.getEventEndTime() - evListBean.getEventStartTime())/(60*1000);
							
							float unitOfRectH = ((float)eventCellH)/60;
							float rectH = timeDiff *unitOfRectH;
			                
							//Log.i("TAG cell","cell height : "+ eventCellH+"Time diff : " +timeDiff +"unit of rect height : "+ unitOfRectH +" RectH : "+rectH +" time end point : "+(int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin())));
							
							Rect rect = new Rect(rectX, 
									(int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin())) ,
									rectX+((int) getWidth() / noOfEvents)-1, 
									(int)rectH+(int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin())));
							
							RectF rec=new RectF(rectX, 
									(int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin())) ,
									rectX+((int) getWidth() / noOfEvents)-1, 
									(int)rectH+(int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin())));
							float radius=8;
							//Log.i("Rect", "startX == "+rect.left+"  top  ==  "+((int)((i * eventCellH)+(unitOfRectH *evListBean.getStartMin()))+" right  ==  "+((int) getWidth() / noOfEvents)+"  height  == "+rectH+"  eventCellH  ==  "+eventCellH+"  timeDiff  ==  "+timeDiff+"  unitOfRectH   ==  "+unitOfRectH));
							 paint.setColor(0x35000000);
						        
							
							    
						      	canvas.drawRoundRect(rec, radius, radius, paint);
						      	paint.setColor(Color.WHITE);
						      	EventWriter evWriter = new EventWriter(
										evListBean.getEventName(), (getWidth()
												/ noOfEvents)-2*textMargin, eventCellH-textMargin,
										((getWidth() / noOfEvents) * line)+textMargin, rect.top+textMargin, paint,context);
								evWriter.onDraw(canvas);
								evWriter.setRect(rect);
								evWriter.setEventListBean(evListBean);
								vctEventWriter.add(evWriter);
								startX = startX + (getWidth() / noOfEvents);
						}
					}//
				}
			}

		}
		setVctEventWriter(vctEventWriter);
	}

	private void drawBackground(Canvas canvas) {
		paint.setColor(0x10000000);// ///////// set transparent background color
		// code
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		
		canvas.drawLine(0, 0, 0, getHeight(), paint);
		canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
		 paint.setColor(Color.WHITE);
			canvas.drawLine(0, 0, getWidth(), 0,
					paint);
		
		for (int i = 1; i <=time_Hours_ResId.length; i++) {
			    paint.setColor(Color.WHITE);
			    if(i == 24)
			    	canvas.drawLine(0, (eventCellH * i) -1, getWidth(), (eventCellH * i) -1,
							paint);
			    else
			    	canvas.drawLine(0, eventCellH * i, getWidth(), eventCellH * i,
							paint);
			    
				
		}
	
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = (event.getAction() & MotionEvent.ACTION_MASK);
		switch (action) {

		
		case MotionEvent.ACTION_UP:
			int Xpos = (int) event.getX();
			int Ypos = (int) event.getY();
			Vector<EventWriter> vctEvents  = getVctEventWriter();
			if(vctEvents.size() > 0){
				EventWriter evWriter = null;
				boolean isMatch = false ;
				for (int i = vctEvents.size(); i >0; i--) {
					 evWriter = vctEvents.elementAt(i-1);
					Rect rect = evWriter.getRect();
					isMatch = isIntersect(rect,Xpos, Ypos);
					if(isMatch){
						break;
					}
				}
				if(isMatch == true){
					((CategoryViewScreen) timezoActivity).editCalendarEvent(evWriter.getEventListBean());
					//Toast.makeText(context, "edit" , Toast.LENGTH_SHORT).show();
				}else{
					int cellNo = Ypos / eventCellH;
					((CategoryViewScreen) timezoActivity).addNewEvent(cellNo);
					//Toast.makeText(context, "add" , Toast.LENGTH_SHORT).show();
				}
			}else{
				int cellNo = Ypos / eventCellH;
				((CategoryViewScreen) timezoActivity).addNewEvent(cellNo);
			}
			
			break;

		default:
			break;
		}

		invalidate();
		return true;

	}
	/**
	 * @return
	 */
	public Vector<EventWriter> getVctEventWriter() {
		return vctEventWriter;
	}

	/**
	 * @param vctEventWriter
	 */
	public void setVctEventWriter(Vector<EventWriter> vctEventWriter) {
		this.vctEventWriter = vctEventWriter;
	}
	public boolean isIntersect(Rect rect,int pointXX,int pointYY) {
		boolean temp = false;
		Log.i("isIntersect", "pointXX == "+pointXX+"  pointYY == "+pointYY);
		Log.i("isIntersect", "x == "+rect.left+"  y == "+rect.top+"  width == "+rect.right+"   height  ==  "+rect.bottom);
		if(rect.contains(pointXX, pointYY)){
			temp = true;
		}
		return temp;
	}
}
