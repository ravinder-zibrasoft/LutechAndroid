package com.lutechmobile.component;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.lutechmobile.bean.EventListBean;
import com.lutechmobile.util.TimezoneUtil;

/**
 * @author Ravindra.Prajapati
 * 
 */
public class EventWriter {
	private String eventTitle;
	private int canvasW;
	private int canvasH;
	private Vector<String> vctEventText;
	private Paint paint;
	private int xPos;
	private int ypos;

	private Rect rect;
	private EventListBean eventListBean;

	Context context;

	/**
	 * @param eventTitle
	 * @param canvasW
	 * @param canvasH
	 */
	public EventWriter(String eventTitle, int canvasW, int canvasH,int xPos,int yPos,Paint paint,Context ctx) {
		this.eventTitle = eventTitle;
		this.canvasW = canvasW-1;
		this.canvasH = canvasH;
		this.paint = paint;
		this.paint.setTextSize(TimezoneUtil.pxTosp(16, ctx));
		context=ctx;
		
		vctEventText = WrapTextLine.wrapLine(eventTitle, canvasW,canvasH, this.paint);
		this.xPos = xPos;
		this.ypos = yPos;//(int) (ypos+this.paint.getTextSize());
		Log.i("text vector size", ""+vctEventText.size());
	}

	/**
	 * @param canvas
	 */
	public void onDraw(Canvas canvas) {
		

			
	//	StaticLayout layout=new StaticLayout(eventTitle, 0, 0, new TextPaint(paint), canvasW, android.text.Layout.Alignment.ALIGN_NORMAL, 1.3f, 0,false,  TextUtils.TruncateAt.END, );
			canvas.save();
			TextPaint p=new TextPaint(paint);
			p.setTextSize(TimezoneUtil.pxTosp(14, context));
			p.measureText("w");
			int line=(int) ((int) (canvasH/p.getTextSize())*canvasW/p.measureText("w"));
			if(eventTitle.length()>line){
				eventTitle=eventTitle.substring(0, line);
				eventTitle=eventTitle.substring(0, eventTitle.length() - 3)+"...";
			}
			StaticLayout layout = new StaticLayout(eventTitle, p, canvasW, android.text.Layout.Alignment.ALIGN_NORMAL, 0.75f, 0f, false);
			canvas.translate(xPos+1, ypos);
			layout.draw(canvas);
			canvas.restore();
//			canvas.drawText(text,xPos+2, ypos, paint);
//			ypos=(int) (ypos+(paint.getTextSize()*i));
//			
//		}
			
			
//			TextView tv = new TextView(context);
//			tv.setText(eventTitle);
//			tv.setTextColor(Color.BLACK);
//			tv.setDrawingCacheEnabled(true);
//			tv.measure(MeasureSpec.makeMeasureSpec(canvasW, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(canvasH, MeasureSpec.EXACTLY));
//			tv.layout(xPos, ypos, 0, 0);
//			tv.buildDrawingCache(true);
//			Bitmap b=tv.getDrawingCache();
//			if(b!=null){
//				b=b.copy(b.getConfig(), false);
//				canvas.drawBitmap(b, xPos, ypos, paint);
//			}
//			else 
//				Log.i("Bitmap null", "************");
//			tv.setDrawingCacheEnabled(false);
			// tv.setDrawingCacheEnabled(false);
	}
	/**
	 * @return rectangle instance 
	 */
	public Rect getRect() {
		return rect;
	}

	/**
	 * @param rect
	 */
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	/**
	 * @return
	 */
	public EventListBean getEventListBean() {
		return eventListBean;
	}

	/**
	 * @param eventListBean
	 */
	public void setEventListBean(EventListBean eventListBean) {
		this.eventListBean = eventListBean;
	}
 	
}
