package com.android_mvc.framework.ui.view;

import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TextViewのラッパークラス。
 * 
 * @author id:language_and_engineering
 * 
 */
public class MTextView extends TextView implements IFWView
{

    public MTextView(Context context) {
        super(context);
    }

    public MTextView(Context context, AttributeSet attrs, int defStyle) {
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

    public MTextView text(String s) {
        setText(s);
        return this;
    }

    public MTextView paddingPx(int px) {
        setPadding(px, px, px, px);
        return this;
    }

    public MTextView widthWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    public MTextView widthMatchParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public MTextView widthPx(int w) {
        setWidth(w);
        return this;
    }

    public MTextView widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT);
        return this;
    }

    public MTextView heightWrapContent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    public MTextView weight(int w) {
        setViewParam("layout_weight", w);
        return this;
    }

    public MTextView heightPx(int px) {
        setHeight(px);
        return this;
    }

    public MTextView textColor(int color) {
        setTextColor(color);
        // 色の指定には，XMLではなく「android.graphics.Color」クラスを使う。
        // @see http://www.javadrive.jp/android/textview/index3.html
        return this;
    }

    public MTextView visible() {
        setVisibility(VISIBLE);
        return this;
    }

    public MTextView invisible() {
        setVisibility(GONE);
        return this;
    }

    public MTextView backgroundDrawable(int resource) {
        setBackgroundDrawable(getResources().getDrawable(resource));
        return this;
    }

    public MTextView backgroundResource(int resid) {
        setBackgroundResource(resid);
        return this;
    }

    public MTextView gravity(int g) {
        setGravity(g);
        return this;
    }

    public MTextView click(OnClickListener l) {
        // クリックできる（＝ボタン）の場合、中央ぞろえにする。
        setGravity(Gravity.CENTER);
        setOnClickListener(l);
        return this;
    }

    public String text() {
            return this.getText().toString();
    }
    
    public MTextView hint(String s){
        setHint(s);
        return this;
    }

    public MTextView hintTextColor(int res){
        setHintTextColor(res);
        return this;
    }
    public MTextView textsize(int size){
        setTextSize(size);
        return this;
    }

    public MTextView drawableLeft(int resource) {
        setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        return this;
    }

    public MTextView click(Builder createCalculaterDialogWithOKButton) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
