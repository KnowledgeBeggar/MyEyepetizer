package com.android.myeyepetizer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.android.myeyepetizer.R;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MyRadioButton extends AppCompatRadioButton {

    private Drawable mDrawableTop; //drawableTop图片对象
    private int mDrawableTopWidth; //宽度
    private int mDrawableTopHeight;//高度

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawableTopSize(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDrawableTopSize(context, attrs);
    }

    private void setDrawableTopSize(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
            mDrawableTop = array.getDrawable(R.styleable.MyRadioButton_drawableTop);
            mDrawableTopWidth = (int) (array.getDimension(R.styleable.MyRadioButton_drawableTopWidth, 0) + 0.5f);
            mDrawableTopHeight = (int) (array.getDimension(R.styleable.MyRadioButton_drawableTopHeight, 0) + 0.5f);
            array.recycle();

            setCompoundDrawables(null, mDrawableTop, null, null);
        }
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (top != null) {
            top.setBounds(0, 0, mDrawableTopWidth <= 0 ? top.getIntrinsicWidth() : mDrawableTopWidth,
                    mDrawableTopHeight <= 0 ? top.getIntrinsicHeight() : mDrawableTopHeight);
        }

        super.setCompoundDrawables(left, top, right, bottom);
    }
}
