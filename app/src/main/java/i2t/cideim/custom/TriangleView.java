package i2t.cideim.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import i2t.cideim.R;

/**
 * Created by Leonardo.
 * Custom triangle view, widely used across the app.
 */

public class TriangleView extends View {

    private int mColor = Color.TRANSPARENT;
    private float mMargin = 0;
    private String mOrientation = "up";
    private boolean mOnlyShowWhenSelected = false; // See setSelected()
    private Paint mPaint;
    private Path path;

    public TriangleView(Context context) {
        super(context);
        init(null, 0);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /* Loads the attributes and sets the paths configuration to draw */
    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TriangleView, defStyle, 0);

        mColor = a.getColor(R.styleable.TriangleView_color, mColor);
        mMargin = a.getDimension(R.styleable.TriangleView_margin, mMargin);
        mOrientation = a.getString(R.styleable.TriangleView_orientation);
        mOnlyShowWhenSelected = a.getBoolean(R.styleable.TriangleView_only_show_when_selected, mOnlyShowWhenSelected);
        a.recycle();

        path = new Path();
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /* Draws the triangle inside the container */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int contentWidth = getWidth();
        int contentHeight = getHeight();

        path.reset();

        if (mOrientation.equals("right")) {
            path.moveTo(0 + mMargin, 0 + mMargin);
            path.lineTo(contentWidth - mMargin, contentHeight / 2);
            path.lineTo(0 + mMargin, contentHeight - mMargin);
            path.moveTo(0 + mMargin, contentHeight - mMargin);
            path.lineTo(contentWidth - mMargin, contentHeight / 2);
        } else if (mOrientation.equals("down")) {
            path.moveTo(contentWidth / 2, contentHeight - mMargin);
            path.lineTo(0 + mMargin, 0 + mMargin);
            path.lineTo(contentWidth - mMargin, 0 + mMargin);
            path.moveTo(contentWidth - mMargin, 0 + mMargin);
            path.lineTo(0 + mMargin, 0 + mMargin);
        } else if (mOrientation.equals("left")) {
            path.moveTo(0 + mMargin, contentHeight / 2);
            path.lineTo(contentWidth - mMargin, 0 + mMargin);
            path.lineTo(contentWidth - mMargin, contentHeight - mMargin);
            path.moveTo(contentWidth - mMargin, contentHeight - mMargin);
            path.lineTo(contentWidth - mMargin, 0 + mMargin);
        } else {
            path.moveTo(contentWidth / 2, 0 + mMargin);
            path.lineTo(0 + mMargin, contentHeight - mMargin);
            path.moveTo(contentWidth - mMargin, contentHeight - mMargin);
            path.lineTo(contentWidth / 2, 0 + mMargin);
            path.lineTo(0 + mMargin, contentHeight - mMargin);
        }

        path.close();

        canvas.drawPath(path, mPaint);
    }

    /*
     * The visibility changes depending of the container's state. If the container's state changes
     * to 'selected', the triangle is shown, otherwise it is invisible.
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mOnlyShowWhenSelected) {
            if (selected) {
                this.setVisibility(VISIBLE);
            } else {
                this.setVisibility(INVISIBLE);
            }
        }
    }
}
