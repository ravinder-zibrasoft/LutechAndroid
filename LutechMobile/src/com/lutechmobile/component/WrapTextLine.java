package com.lutechmobile.component;

import java.util.Vector;

import android.graphics.Paint;

/**
 * @author Ravindra.Prajapati
 *
 */
public class WrapTextLine {
	/**
	 * @param text
	 * @param width
	 * @param height
	 * @param paint
	 * @return  wrap the long text
	 */
	public static Vector wrapLine(String text, int width,int height, Paint paint) {
		Vector result = new Vector();
		if (text == null)
			return result;

		boolean hasMore = true;

		// The current index of the cursor
		int current = 0;

		// The next line break index
		int lineBreak = -1;

		// The space after line break
		int nextSpace = -1;
		
		int  maxLine=(int) (height/paint.getTextSize());

		while (hasMore) {
			// Find the line break
			while (true) {
				lineBreak = nextSpace;
				if (lineBreak == text.length() - 1) {
					// We have reached the last line
					hasMore = false;
					break;
				} else {
					nextSpace = text.indexOf(' ', lineBreak + 1);
					nextSpace = text.indexOf('\n', lineBreak + 1);
					if (nextSpace == -1)
						nextSpace = text.length() - 1;
					int linewidth = (int) paint.measureText(text);//font.getAdvance(text, current, nextSpace- current);
					// If too long, break out of the find loop
					if (linewidth > width)
						break;
				}
			}
			String line = text.substring(current, lineBreak + 1);
			result.addElement(line);
			current = lineBreak + 1;
		}
		if(result.size()>maxLine)
			result=new Vector(result.subList(0, maxLine));
		return result;
	}
}
