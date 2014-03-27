package com.android_mvc.framework.ui.view;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Buttonのラッパークラス。
 * 
 * @author id:language_and_engineering
 * 
 */
public class MButton extends Button implements IFWView {

    public MButton(Context context) {
        super(context);
    }

    public MButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // パラメータ保持
    HashMap<String, Object> view_params = new HashMap<String, Object>();

    @Override
    public Object getViewParam(String key) {
        return view_params.get(key);
    }

    @Override
    public void setViewParam(String key, Object val) {
        view_params.put(key, val);
    }

    // 以下は属性操作

    public MButton text(String s) {
        setText(s);
        return this;
    }

    public MButton click(OnClickListener l) {
        setOnClickListener(l);
        return this;
    }

    public MButton longClick(OnLongClickListener l) {
        setOnLongClickListener(l);
        return this;
    }

    public MButton widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT);
        return this;
    }

    public MButton widthMatchParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public MButton widthWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    public MButton backgroundResource(int resource) {
        setBackgroundResource(resource);
        return this;
    }

    public MButton drawableLeft(int resource) {
        setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        return this;
    }

    public MButton backgroundDrawable(int resource) {
        setBackgroundDrawable(getResources().getDrawable(resource));
        return this;
    }

    public MButton gravity(int g) {
        setGravity(g);
        return this;
    }

    public MButton touch(OnTouchListener l) {
        setOnTouchListener(l);
        return this;
    }

    public MButton hint(String s) {
        setHint(s);
        return this;
    }

    public MButton textSize(float size) {
        setTextSize(size);
        return this;
    }

    public MButton textColor(int color) {
        setTextColor(color);
        // 色の指定には，XMLではなく「android.graphics.Color」クラスを使う。
        // @see http://www.javadrive.jp/android/textview/index3.html
        return this;
    }

}
