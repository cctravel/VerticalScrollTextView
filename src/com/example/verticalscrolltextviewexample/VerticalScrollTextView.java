package com.example.verticalscrolltextviewexample;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Ricci
 * @version 2016-6-24上午10:42:08
 * 
 */
public class VerticalScrollTextView extends TextView {

	private Paint mPaint;
	/**
	 * 滚动速度
	 */
	private long scrollspeed;
	/**
	 * 能否暂停
	 */
	private boolean stopable;
	private boolean isStopScroll = false;
	/**
	 * 暂停时间
	 */
	private long stoptime;
	/**
	 * 总滚动圈数
	 */
	private int scrollTimes;
	/**
	 * 当前滚动圈数计时
	 */
	private int countTimes = 0;
	/**
	 * 行间距
	 */
	private int verticalSpace;
	private float textSize;
	private List<String> stringList = new ArrayList<String>();
	/**
	 * 位移增量
	 */
	private float step = 0.0f;
	private int mHeight;

	public VerticalScrollTextView(Context context) {
		super(context);
		mPaint = this.getPaint();
	}

	public VerticalScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = this.getPaint();
		textSize = mPaint.getTextSize();
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.VerticalScrollTextView);
		scrollspeed = typedArray.getInteger(
				R.styleable.VerticalScrollTextView_scrollspeed, 0);
		stopable = typedArray.getBoolean(
				R.styleable.VerticalScrollTextView_stopable, false);
		stoptime = typedArray.getInteger(
				R.styleable.VerticalScrollTextView_stoptime, 0);
		scrollTimes = typedArray.getInteger(
				R.styleable.VerticalScrollTextView_scrolltimes, -1);
		verticalSpace = typedArray.getInteger(
				R.styleable.VerticalScrollTextView_verticalspace, 0);
		typedArray.recycle();
		mHeight = this.getHeight();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = 0;
		int height = 0;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		}
		if (widthMode == MeasureSpec.AT_MOST) {
			if (stringList != null && stringList.size() > 0) {
				width = (int) Math.max(getText().length() * textSize,
						stringList.get(0).length() * textSize);
			} else {
				width = (int) (getText().length() * textSize);
			}
		}
		if (heightMode == MeasureSpec.AT_MOST) {
			height = (int) textSize + 16;
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int compoundPaddingLeft = getCompoundPaddingLeft();
		if (step >= mHeight + (stringList.size() + 2) * textSize
				+ stringList.size() * verticalSpace) {
			countTimes++;
			step = 0.0f;
		}
		if (scrollTimes != -1 && countTimes >= scrollTimes) {
			isStopScroll = true;
			if (TextUtils.isEmpty(getText()) && stringList != null
					&& stringList.size() > 0) {
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < stringList.size(); i++) {
					stringBuilder.append(stringList.get(i));
				}
				this.setText(stringBuilder.toString());
			}
			super.onDraw(canvas);
		}
		if (stringList != null && stringList.size() > 0) {
			int length = stringList.size();
			int verticalOffset;
			int currentLineHeight;
			for (int i = 0; i < length; i++) {
				verticalOffset = i * verticalSpace;
				currentLineHeight = (int) ((i + 2) * textSize);
				canvas.drawText(stringList.get(i), compoundPaddingLeft,
						this.getHeight() + (i + 1) * textSize - step
								+ verticalOffset, mPaint);
				if ((mHeight + currentLineHeight - step - (textSize / 2)
						+ scrollspeed + verticalOffset) > (mHeight / 2)
						&& (mHeight + currentLineHeight - step - (textSize / 2)
								- scrollspeed + verticalOffset) < (mHeight / 2)
						&& (mHeight + currentLineHeight - step - (textSize / 2) + verticalOffset) < (mHeight / 2)
						&& stopable) {
					isStopScroll = true;
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							invalidate();
							isStopScroll = false;
						}
					}, stoptime);
				}
			}
		}
		step = step + scrollspeed;
		if (!isStopScroll) {
			invalidate();
		}
	}

	public void setStringList(List<String> sList) {
		if (this.stringList != null || this.stringList.size() >= 0) {
			this.stringList.clear();
		}
		this.stringList.addAll(sList);
		step = 0.0f;
		invalidate();
	}

}
