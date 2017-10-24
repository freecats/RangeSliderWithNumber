package com.example.freecats.numberselect.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.freecats.numberselect.R;
import com.example.freecats.numberselect.ResUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Slider following Material Design with two movable targets
 * that allow user to select a range of integers.
 */
public class RangeSliderWithNumber extends View {

    public interface RangeSliderListener {
        void onMaxChanged(int newValue);

        void onMinChanged(int newValue);
    }

    //Padding that is always added to both sides of slider, in addition to layout_margin
    private static final int DEFAULT_TOUCH_TARGET_SIZE = Math.round(dp2Px(40));
    private static final int DEFAULT_UNPRESSED_RADIUS = 15;
    private static final int DEFAULT_PRESSED_RADIUS = 40;
    private static final int DEFAULT_INSIDE_RANGE_STROKE_WIDTH = (int) dp2Px(5);
    private static final int DEFAULT_OUTSIDE_RANGE_STROKE_WIDTH = (int) dp2Px(5);
    private static final int DEFAULT_MAX = 100;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int lineStartX;
    private int lineEndX;
    private int lineLength;
    private int minPosition = 0;
    private int minLimit = -1;//最小值选择限定
    private int maxLimit = -1;//最大值选择限定
    private int maxPosition = 0;
    private int middleY = 0;
    //List of event IDs touching targets
    private Set<Integer> isTouchingMinTarget = new HashSet<>();
    private Set<Integer> isTouchingMaxTarget = new HashSet<>();
    private int min = 0;
    private int max = DEFAULT_MAX;
    private int range;
    private float convertFactor;
    private RangeSliderListener rangesliderListener;
    private int targetColor;
    private int insideRangeColor;
    private int outsideRangeColor;
    private int numberTextColor;
    private float numberTextSize;
    private float numberMarginBottom;
    private int colorControlNormal;
    private int colorControlHighlight;
    private float insideRangeLineStrokeWidth;
    private float outsideRangeLineStrokeWidth;
    boolean lastTouchedMin;
    private boolean isTouchingMin;

    private int startingMin = -1;
    private int startingMax = -1;
    boolean isFirstInit = true;
    private boolean isTouching = false;
    private boolean isShowBubble;
    private boolean isLineRound;
    private Bitmap bubbleBitmap;
    private Rect minTextRect = new Rect();
    private Rect maxTextRect = new Rect();
    private Rect rulerTextRect = new Rect();
    private Bitmap circleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_choose_ring);
    private Bitmap circleBitmapFocus = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_choose_ring_focus);

    private boolean isShowRuler;
    private int rulerTextColor = ResUtils.getInstance(getContext()).getColor(R.color.color_gray_66);
    private int rulerColor;
    private float rulerTextSize;
    private int rulerInterval;
    private float rulerMarginTop;
    private float rulerAndTextMargin;
    private float rulerNormalHeight = dp2Px(4);

    public RangeSliderWithNumber(Context context) {
        super(context);
        init(null);
    }

    public RangeSliderWithNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RangeSliderWithNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        getDefaultColors();
        getDefaultMeasurements();

        if (attrs != null) {
            //get attributes passed in XML
            TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs,
                    R.styleable.RangeSliderWithNumber, 0, 0);
            targetColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_insideRangeLineColor,
                    colorControlNormal);
            insideRangeColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_insideRangeLineColor,
                    colorControlNormal);
            outsideRangeColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_outsideRangeLineColor,
                    colorControlHighlight);
            numberTextColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_numberTextColor,
                    colorControlHighlight);
            numberTextSize = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_numberTextSize, sp2Px(12));
            numberMarginBottom = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_numberMarginBottom, dp2Px(5));
            min = styledAttrs.getInt(R.styleable.RangeSliderWithNumber_rsn_min, min);
            max = styledAttrs.getInt(R.styleable.RangeSliderWithNumber_rsn_max, max);

            insideRangeLineStrokeWidth = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_insideRangeLineStrokeWidth, DEFAULT_INSIDE_RANGE_STROKE_WIDTH);
            outsideRangeLineStrokeWidth = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_outsideRangeLineStrokeWidth, DEFAULT_OUTSIDE_RANGE_STROKE_WIDTH);

            isShowBubble = styledAttrs.getBoolean(R.styleable.RangeSliderWithNumber_rsn_isShowBubble, false);
            bubbleBitmap = BitmapFactory.decodeResource(getResources(), styledAttrs.getResourceId(R.styleable.RangeSliderWithNumber_rsn_bubbleResource, R.mipmap.bg_choose_green));

            circleBitmapFocus = BitmapFactory.decodeResource(getResources(), styledAttrs.getResourceId(R.styleable.RangeSliderWithNumber_rsn_circleFocusBitmap, R.mipmap.ic_choose_ring_focus));

            isLineRound = styledAttrs.getBoolean(R.styleable.RangeSliderWithNumber_rsn_isLineRound, true);

            isShowRuler = styledAttrs.getBoolean(R.styleable.RangeSliderWithNumber_rsn_isShowRuler, false);
            rulerTextColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_rulerTextColor, rulerTextColor);
            rulerColor = styledAttrs.getColor(R.styleable.RangeSliderWithNumber_rsn_rulerColor, colorControlNormal);
            rulerTextSize = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_rulerTextSize, sp2Px(12));
            rulerInterval = styledAttrs.getInt(R.styleable.RangeSliderWithNumber_rsn_rulerInterval, 20);
            rulerMarginTop = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_rulerMarginTop, dp2Px(4));
            rulerAndTextMargin = styledAttrs.getDimension(R.styleable.RangeSliderWithNumber_rsn_rulerAndTextMargin, dp2Px(4));

            styledAttrs.recycle();
        }

        range = max - min;

    }

    /**
     * Get default colors from theme.  Compatible with 5.0+ themes and AppCompat themes.
     * Will attempt to get 5.0 colors, if not avail fallback to AppCompat, and if not avail use
     * black and gray.
     * These will be used if colors are not set in xml.
     */
    private void getDefaultColors() {
        TypedValue typedValue = new TypedValue();

        TypedArray materialStyledAttrs = getContext().obtainStyledAttributes(typedValue.data, new int[]{
                android.R.attr.colorControlNormal,
                android.R.attr.colorControlHighlight
        });

        TypedArray appcompatMaterialStyledAttrs = getContext().obtainStyledAttributes(typedValue.data, new int[]{
                android.support.v7.appcompat.R.attr.colorControlNormal,
                android.support.v7.appcompat.R.attr.colorControlHighlight
        });
        colorControlNormal = ResUtils.getInstance(getContext()).getColor(R.color.colorPrimary);
        colorControlHighlight = ResUtils.getInstance(getContext()).getColor(R.color.colorPrimaryDark);

        targetColor = colorControlNormal;
        insideRangeColor = colorControlHighlight;

        materialStyledAttrs.recycle();
        appcompatMaterialStyledAttrs.recycle();
    }

    /**
     * Get default measurements to use for radius and stroke width.
     * These are used if measurements are not set in xml.
     */
    private void getDefaultMeasurements() {
        insideRangeLineStrokeWidth = Math.round(dp2Px(DEFAULT_INSIDE_RANGE_STROKE_WIDTH));
        outsideRangeLineStrokeWidth = Math.round(dp2Px(DEFAULT_OUTSIDE_RANGE_STROKE_WIDTH));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int desiredWidth = widthSize;
        int desiredHeight;

        getTextBounds(String.valueOf(min), minTextRect);
        getTextBounds(String.valueOf(max), maxTextRect);

        if (isShowBubble) {
            desiredHeight = (int) (circleBitmap.getHeight() + numberMarginBottom) + bubbleBitmap.getHeight();
        } else {

            desiredHeight = (int) (circleBitmap.getHeight() + numberMarginBottom + minTextRect.height());
        }

        int rulerHeight = (int) (rulerMarginTop + rulerNormalHeight * 3 + rulerAndTextMargin + rulerTextRect.height());
        if (isShowRuler) {
            getRulerTextBounds(String.valueOf(min), rulerTextRect);
            desiredHeight += rulerHeight;
        }

        int width = desiredWidth;
        int height = desiredHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = desiredHeight;
        }

        int marginStartEnd = isShowBubble ? bubbleBitmap.getWidth() : Math.max(circleBitmap.getWidth(), maxTextRect.width());

        lineLength = (width - marginStartEnd);
        middleY = isShowRuler ? height - rulerHeight - circleBitmap.getHeight() / 2 : height - circleBitmap.getHeight() / 2;
        lineStartX = marginStartEnd / 2;
        lineEndX = lineLength + marginStartEnd / 2;

        calculateConvertFactor();

        if (isFirstInit) {
            setSelectedMin(startingMin != -1 ? startingMin : min);
            setSelectedMax(startingMax != -1 ? startingMax : max);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawEntireRangeLine(canvas);
        drawSelectedRangeLine(canvas);
        drawSelectedNumber(canvas);
        drawRuler(canvas);
        drawSelectedTargets(canvas);
    }

    private void drawEntireRangeLine(Canvas canvas) {
        paint.setColor(outsideRangeColor);
        paint.setStrokeWidth(outsideRangeLineStrokeWidth);
        canvas.drawLine(lineStartX, middleY, lineEndX, middleY, paint);

        if (isLineRound) {
            canvas.drawCircle(lineStartX, middleY, outsideRangeLineStrokeWidth / 2, paint);
            canvas.drawCircle(lineEndX, middleY, outsideRangeLineStrokeWidth / 2, paint);
        }
    }

    private void drawSelectedRangeLine(Canvas canvas) {
        paint.setStrokeWidth(insideRangeLineStrokeWidth);
        paint.setColor(insideRangeColor);
        canvas.drawLine(minPosition, middleY, maxPosition, middleY, paint);
    }

    private void drawSelectedNumber(Canvas canvas) {

        String min = String.valueOf(getSelectedMin());
        String max = String.valueOf(getSelectedMax());

        getTextBounds(min, minTextRect);
        getTextBounds(max, maxTextRect);


        float minXBubble = minPosition - bubbleBitmap.getWidth() / 2;
        float maxXBubble = maxPosition - bubbleBitmap.getWidth() / 2;
        int maxTextLengthBubble = bubbleBitmap.getWidth() + 5;
        if (isTouching && isTouchingMaxTarget.size() > 0) {
            if (Math.abs(maxXBubble - minXBubble) <= maxTextLengthBubble) {
                maxXBubble = minXBubble + maxTextLengthBubble;

                if (maxXBubble > lineEndX - maxTextLengthBubble / 2) {
                    maxXBubble = lineEndX - maxTextLengthBubble / 2;
                }
            }
        }

        if (isTouching && isTouchingMinTarget.size() > 0) {
            if (Math.abs(maxXBubble - minXBubble) <= maxTextLengthBubble) {
                minXBubble = maxXBubble - maxTextLengthBubble;

                if (minXBubble < lineStartX) {
                    minXBubble = lineStartX;
                }
            }
        }

        if (Math.abs(maxXBubble - minXBubble) <= maxTextLengthBubble) {

            if (!isTouchingMin) {
                maxXBubble = minXBubble + maxTextLengthBubble;
                if (maxXBubble > lineEndX - maxTextLengthBubble / 2) {
                    maxXBubble = lineEndX - maxTextLengthBubble / 2;
                    minXBubble = maxXBubble - maxTextLengthBubble;
                }
            } else {
                minXBubble = maxXBubble - maxTextLengthBubble;
                if (minXBubble < lineStartX + maxTextLengthBubble / 2) {
                    minXBubble = lineStartX + maxTextLengthBubble / 2;
                    maxXBubble = minXBubble + maxTextLengthBubble;
                }
            }
        }


        float yText;
        //bubble
        if (isShowBubble) {

            float top = middleY - circleBitmap.getHeight() / 2 - bubbleBitmap.getHeight() - numberMarginBottom;
            yText = top + bubbleBitmap.getHeight() / 2 + minTextRect.height() / 2 - 6;

            canvas.drawBitmap(bubbleBitmap, maxXBubble, top, paint);
            canvas.drawBitmap(bubbleBitmap, minXBubble, top, paint);

        } else {
            yText = middleY - circleBitmap.getHeight() / 2 - numberMarginBottom;
        }

        //text

        float minX = minPosition - minTextRect.width() / 2;
        float maxX = maxPosition - maxTextRect.width() / 2 - 6;
        int maxTextLength = isShowBubble ? bubbleBitmap.getWidth() : getMaxTextLength() + 5;

        if (isTouching && isTouchingMaxTarget.size() > 0) {
            if (Math.abs(maxX - minX) <= maxTextLength) {
                maxX = minX + maxTextLength;

                if (maxX > lineEndX - maxTextLength / 2) {
                    maxX = lineEndX - maxTextLength / 2;
                }
            }
        }

        if (isTouching && isTouchingMinTarget.size() > 0) {
            if (Math.abs(maxX - minX) <= maxTextLength) {
                minX = maxX - maxTextLength;

                if (minX < lineStartX) {
                    minX = lineStartX;
                }
            }
        }

        if (Math.abs(maxX - minX) <= maxTextLength) {
            if (!isTouchingMin) {
                maxX = minX + maxTextLength;
                if (maxX > lineEndX - maxTextLength / 2) {
                    maxX = lineEndX - maxTextLength / 2;
                    minX = maxX - maxTextLength;
                }
            } else {
                minX = maxX - maxTextLength;
                if (minX < lineStartX + maxTextLength / 2) {
                    minX = lineStartX + maxTextLength / 2;
                    maxX = minX + maxTextLength;
                }
            }
        }


        paint.setTextSize(numberTextSize);

        paint.setColor(numberTextColor);
        canvas.drawText(min, minX, yText, paint);

        paint.setColor(numberTextColor);
        canvas.drawText(max, maxX, yText, paint);


    }

    private void drawRuler(Canvas canvas) {
        if (isShowRuler) {
            float startX = lineStartX;
            float stopX = 0;
            float stopY = 0;
            float startY = 0;
            float totalLength = lineLength;
            int divider = rulerInterval / 10;
            float scaleLength = (float) lineLength / (float) ((max - min) / (rulerInterval / 10)) / divider;

            boolean isMinHasText = false;
            boolean isMaxHasText = false;

            for (int i = min; i <= max; i++) {
                if (i % rulerInterval == 0) {
                    //draw big scale
                    stopX = startX;
                    startY = middleY + circleBitmap.getHeight() / 2 + rulerMarginTop;
                    stopY = startY + rulerNormalHeight * 3;


                    paint.setColor(rulerTextColor);
                    paint.setTextSize(rulerTextSize);
                    getRulerTextBounds(String.valueOf(i), rulerTextRect);
                    canvas.drawText(String.valueOf(i), startX - rulerTextRect.width() / 2, stopY + rulerTextRect.height() + rulerAndTextMargin, paint);

                    if (i == min) {
                        isMinHasText = true;
                    }
                    if (i == max) {
                        isMaxHasText = true;
                    }
                    paint.setStrokeWidth(1.5f);


                    paint.setColor(rulerColor);
                    canvas.drawLine(startX, startY, startX, stopY, paint);

                } else if (i % (rulerInterval / 2) == 0) {
                    //draw middle scale
                    startY = middleY + circleBitmap.getHeight() / 2 + rulerMarginTop;
                    stopY = startY + rulerNormalHeight * 2;
                    paint.setStrokeWidth(1.0f);

                    paint.setColor(rulerColor);
                    canvas.drawLine(startX, startY, startX, stopY, paint);


                } else {
                    //draw small scale
                    startY = middleY + circleBitmap.getHeight() / 2 + rulerMarginTop;
                    stopY = startY + rulerNormalHeight;
                    paint.setStrokeWidth(0.8f);

                    if (i % (rulerInterval / 10) == 0) {
                        paint.setColor(rulerColor);
                        canvas.drawLine(startX, startY, startX, stopY, paint);
                    }

                }

                if ((i == max && !isMaxHasText) || (i == min && !isMinHasText)) {
                    paint.setColor(rulerTextColor);
                    paint.setTextSize(rulerTextSize);
                    getRulerTextBounds(String.valueOf(i), rulerTextRect);
                    canvas.drawText(String.valueOf(i), startX - rulerTextRect.width() / 2, startY + rulerNormalHeight * 3 + rulerTextRect.height() + rulerAndTextMargin, paint);

                }

                startX += scaleLength;


            }
        }

    }

    private void drawSelectedTargets(Canvas canvas) {
        paint.setColor(targetColor);
        canvas.drawCircle(minPosition, middleY, 20, paint);
        canvas.drawCircle(maxPosition, middleY, 20, paint);

        if (isTouching) {
            if (lastTouchedMin) {
                canvas.drawBitmap(circleBitmapFocus, minPosition - circleBitmapFocus.getWidth() / 2, middleY - circleBitmapFocus.getWidth() / 2, paint);
                canvas.drawBitmap(circleBitmap, maxPosition - circleBitmap.getWidth() / 2, middleY - circleBitmap.getWidth() / 2, paint);
            } else {
                canvas.drawBitmap(circleBitmap, minPosition - circleBitmap.getWidth() / 2, middleY - circleBitmap.getWidth() / 2, paint);
                canvas.drawBitmap(circleBitmapFocus, maxPosition - circleBitmapFocus.getWidth() / 2, middleY - circleBitmapFocus.getWidth() / 2, paint);
            }
        } else {
            canvas.drawBitmap(circleBitmap, maxPosition - circleBitmap.getWidth() / 2, middleY - circleBitmap.getWidth() / 2, paint);
            canvas.drawBitmap(circleBitmap, minPosition - circleBitmap.getWidth() / 2, middleY - circleBitmap.getWidth() / 2, paint);
        }
    }

    private void getTextBounds(String text, Rect rect) {
        paint.setTextSize(numberTextSize);
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    private void getRulerTextBounds(String text, Rect rect) {
        paint.setTextSize(rulerTextSize);
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    //user has touched outside the target, lets jump to that position
    private void jumpToPosition(int index, MotionEvent event) {
        if (event.getX(index) > maxPosition && event.getX(index) <= lineEndX) {
            maxPosition = (int) event.getX(index);
            invalidate();
            callMaxChangedCallbacks();
        } else if (event.getX(index) < minPosition && event.getX(index) >= lineStartX) {
            minPosition = (int) event.getX(index);
            invalidate();
            callMinChangedCallbacks();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;

        isFirstInit = false;

        final int actionIndex = event.getActionIndex();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                isTouching = true;

                if (lastTouchedMin) {
                    if (!checkTouchingMinTarget(actionIndex, event)
                            && !checkTouchingMaxTarget(actionIndex, event)) {
                        jumpToPosition(actionIndex, event);
                    }
                } else if (!checkTouchingMaxTarget(actionIndex, event)
                        && !checkTouchingMinTarget(actionIndex, event)) {
                    jumpToPosition(actionIndex, event);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                isTouching = false;

                isTouchingMinTarget.remove(event.getPointerId(actionIndex));
                isTouchingMaxTarget.remove(event.getPointerId(actionIndex));

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:

                isTouching = true;

                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (isTouchingMinTarget.contains(event.getPointerId(i))) {
                        int touchX = (int) event.getX(i);
                        touchX = clamp(touchX, lineStartX, lineEndX);
                        if (touchX >= maxPosition) {
                            maxPosition = touchX;
                            callMaxChangedCallbacks();
                        }
                        minPosition = touchX;
                        callMinChangedCallbacks();
                        isTouchingMin = true;
                    }
                    if (isTouchingMaxTarget.contains(event.getPointerId(i))) {
                        int touchX = (int) event.getX(i);
                        touchX = clamp(touchX, lineStartX, lineEndX);
                        if (touchX <= minPosition) {
                            minPosition = touchX;
                            callMinChangedCallbacks();
                        }
                        maxPosition = touchX;
                        callMaxChangedCallbacks();
                        isTouchingMin = false;
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                isTouching = true;

                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (lastTouchedMin) {
                        if (!checkTouchingMinTarget(i, event)
                                && !checkTouchingMaxTarget(i, event)) {
                            jumpToPosition(i, event);
                        }
                    } else if (!checkTouchingMaxTarget(i, event)
                            && !checkTouchingMinTarget(i, event)) {
                        jumpToPosition(i, event);
                    }
                }

                break;

            case MotionEvent.ACTION_CANCEL:

                isTouching = false;

                isTouchingMinTarget.clear();
                isTouchingMaxTarget.clear();

                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    private int getMinTextLength() {
        getTextBounds(String.valueOf(min), minTextRect);
        return minTextRect.width();
    }

    private int getMaxTextLength() {
        getTextBounds(String.valueOf(max), maxTextRect);
        return maxTextRect.width();
    }

    /**
     * Checks if given index is touching the min target.  If touching start animation.
     */
    private boolean checkTouchingMinTarget(int index, MotionEvent event) {
        if (isTouchingMinTarget(index, event)) {
            lastTouchedMin = true;
            isTouchingMinTarget.add(event.getPointerId(index));
            return true;
        }
        return false;
    }

    /**
     * Checks if given index is touching the max target.  If touching starts animation.
     */
    private boolean checkTouchingMaxTarget(int index, MotionEvent event) {
        if (isTouchingMaxTarget(index, event)) {
            lastTouchedMin = false;
            isTouchingMaxTarget.add(event.getPointerId(index));
            return true;
        }
        return false;
    }

    private void callMinChangedCallbacks() {
        if (rangesliderListener != null) {
            rangesliderListener.onMinChanged(getSelectedMin());
        }
    }

    private void callMaxChangedCallbacks() {
        if (rangesliderListener != null) {
            rangesliderListener.onMaxChanged(getSelectedMax());
        }
    }

    private boolean isTouchingMinTarget(int pointerIndex, MotionEvent event) {
        return event.getX(pointerIndex) > minPosition - DEFAULT_TOUCH_TARGET_SIZE
                && event.getX(pointerIndex) < minPosition + DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) > middleY - DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) < middleY + DEFAULT_TOUCH_TARGET_SIZE;
    }

    private boolean isTouchingMaxTarget(int pointerIndex, MotionEvent event) {
        return event.getX(pointerIndex) > maxPosition - DEFAULT_TOUCH_TARGET_SIZE
                && event.getX(pointerIndex) < maxPosition + DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) > middleY - DEFAULT_TOUCH_TARGET_SIZE
                && event.getY(pointerIndex) < middleY + DEFAULT_TOUCH_TARGET_SIZE;
    }

    private void calculateConvertFactor() {
        convertFactor = ((float) range) / lineLength;
    }

    public int getSelectedMin() {
        return Math.round((minPosition - lineStartX) * convertFactor + min);
    }

    public int getSelectedMax() {
        return Math.round((maxPosition - lineStartX) * convertFactor + min);
    }

    public void setStartingMinMax(int startingMin, int startingMax) {
        this.startingMin = startingMin;
        this.startingMax = startingMax;
        setSelectedMin(startingMin);
        setSelectedMax(startingMax);
        invalidate();
    }

    private void setSelectedMin(int selectedMin) {
        minPosition = Math.round(((selectedMin - min) / convertFactor) + lineStartX);
        callMinChangedCallbacks();
    }

    private void setSelectedMax(int selectedMax) {
        maxPosition = Math.round(((selectedMax - min) / convertFactor) + lineStartX);
        callMaxChangedCallbacks();
    }

    public void setRangeSliderListener(RangeSliderListener listener) {
        rangesliderListener = listener;
    }

    public RangeSliderListener getRangeSliderListener() {
        return rangesliderListener;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        range = max - min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        range = max - min;
    }

    /**
     * Resets selected values to MIN and MAX.
     */
    public void reset() {
        minPosition = lineStartX;
        maxPosition = lineEndX;
        if (rangesliderListener != null) {
            rangesliderListener.onMinChanged(getSelectedMin());
            rangesliderListener.onMaxChanged(getSelectedMax());
        }
        invalidate();
    }


    /**
     * Keeps Number value inside min/max bounds by returning min or max if outside of
     * bounds.  Otherwise will return the value without altering.
     */
    private <T extends Number> T clamp(@NonNull T value, @NonNull T min, @NonNull T max) {
        if (value.doubleValue() > max.doubleValue()) {
            return max;
        } else if (value.doubleValue() < min.doubleValue()) {
            return min;
        }
        return value;
    }

    private int getColor(int res) {
        return ContextCompat.getColor(getContext(), res);
    }

    private static float dp2Px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public int sp2Px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, getContext().getResources().getDisplayMetrics());

    }
}