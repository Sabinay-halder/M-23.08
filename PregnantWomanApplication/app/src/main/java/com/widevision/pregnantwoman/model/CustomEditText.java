package com.widevision.pregnantwoman.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.widevision.pregnantwoman.R;

/**
 * Created by newtrainee on 7/3/15.
 */
public class CustomEditText extends EditText {
    private int hintColor = getResources().getColor(R.color.hint_color);
    private Paint paint;
    private Status status = Status.ANIMATION_NONE;
    private String hintText;
    private Interpolator animOutInterpolator;
    private Interpolator animInInterpolator;
    private float animInDuration = 200.0f;
    private float animOutDuration = 260.0f;
    private boolean isSetPadding = false;
    private boolean isHasHint = false;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        animOutInterpolator = new OvershootInterpolator(1.3f);
        animInInterpolator = new DecelerateInterpolator();
        if (getHint() != null) {
            isHasHint = true;
            hintText = getHint().toString();
            setHint("");
        }
        if (attrs != null) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            setTypeface(myTypeface);
        }
// if (!TextUtils.isEmpty(getText().toString())) {
// status = Status.ANIMATION_OUT;
//
// } else {
// status = Status.ANIMATION_IN;
// }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    long startTime;
    private String preString;

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (!isHasHint) {
            return;
        }
        if (TextUtils.isEmpty(preString) != TextUtils.isEmpty(getText().toString())) {
            if (!TextUtils.isEmpty(getText().toString())) {
                status = Status.ANIMATION_OUT;
            } else {
                status = Status.ANIMATION_IN;
            }
            preString = (String) getText().toString();
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        preString = (String) text;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        isHasHint = true;
        this.hintText = hintText;
        setHint("");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isHasHint) {
            return;
        }
        paint.set(getPaint());
        paint.setAntiAlias(true);
        paint.setColor(hintColor);
        if (!isSetPadding) {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + (int) paint.measureText(hintText), getPaddingBottom());
            isSetPadding = true;
        }
        float maxHintY = getBaseline();
        switch (status) {
            case ANIMATION_IN:
                if (System.currentTimeMillis() - startTime < animInDuration
                        ) {
                    float hintX = getCompoundPaddingLeft() + getScrollX() + (getWidth() - getCompoundPaddingRight() - getCompoundPaddingLeft()) * (1 - animInInterpolator.getInterpolation((System.currentTimeMillis() - startTime) / animInDuration));
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                    postInvalidate();
                } else {
                    float hintX = getCompoundPaddingLeft();
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                }
                break;
            case ANIMATION_OUT:
                if (System.currentTimeMillis() - startTime < animOutDuration
                        ) {
                    float hintX = getCompoundPaddingLeft() + getScrollX() + (getWidth() - getCompoundPaddingRight() - getCompoundPaddingLeft()) * animOutInterpolator.getInterpolation((System.currentTimeMillis() - startTime) / animOutDuration);
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                    postInvalidate();
                } else {
                    float hintX =
                            getScrollX() + (getWidth() - getCompoundPaddingRight());
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                }
                break;
            case ANIMATION_NONE:
                if (getText().toString().length() == 0) {
                    float hintX = getCompoundPaddingLeft() + getScrollX();
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                } else {
                    float hintX =
                            getScrollX() + (getWidth() - getCompoundPaddingRight());
                    canvas.drawText(hintText, hintX, maxHintY, paint);
                }
                break;
        }
    }

    public enum Status {
        ANIMATION_IN,
        ANIMATION_NONE,
        ANIMATION_OUT
    }
}
