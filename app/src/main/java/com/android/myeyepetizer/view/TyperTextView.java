package com.android.myeyepetizer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import javax.xml.datatype.Duration;

/**
 * Created by Administrator on 2017/8/18.
 */

public class TyperTextView extends AppCompatTextView {

    private int mTextCount;
    public String mCharquence;
    private int mCurrentIndex = -1;
    private StringBuilder mStringBuilder = new StringBuilder();

    private int mDuration = 600;
    private ValueAnimator mValueAnimator;
    private TyperTextAnimationListener mAnimationListener;

    public interface TyperTextAnimationListener {
        void onAnimationFinish();
    }

    public TyperTextView setTyperTextAnimationListener(TyperTextAnimationListener listener) {
        this.mAnimationListener = listener;
        return this;
    }

    public TyperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TyperTextView(Context context) {
        super(context);
    }

    public TyperTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initAnimation() {
        mValueAnimator = ValueAnimator.ofInt(0, mTextCount + 1);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mDuration);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int index = (int) animation.getAnimatedValue();

                if (index > mCurrentIndex) {
                    mCurrentIndex = index;

                    if (index > 0 && index <= mCharquence.length()) {
                        SpannableString spannableString = new SpannableString(mCharquence.toString());
                        spannableString.setSpan(new ForegroundColorSpan(0), index - 1, mCharquence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        setText(spannableString);
                    }
                    if (mCurrentIndex == (mTextCount - 1)) {
                        if (mAnimationListener != null) {
                            mAnimationListener.onAnimationFinish();
                        }
                    }
                }
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setText(mCharquence);
            }
        });
    }

    public TyperTextView setAnimationString(String string) {
        if (string != null) {
            mTextCount = string.length();
            mCharquence = string;

            initAnimation();
        }

        return this;
    }

    public TyperTextView startAnimation() {
        if (mValueAnimator != null) {
            mCurrentIndex = -1;
            mStringBuilder.setLength(0);
            setText("");

            mValueAnimator.start();
        }

        return this;
    }

    public TyperTextView stopAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }

        return this;
    }

}
