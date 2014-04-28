package com.android_mvc.framework.ui.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.ui.view.etc.LayoutUtil;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.common.HooterMenu;
import com.android_mvc.sample_project.common.Util;

/**
 * LinearLayoutのラッパークラス。
 * 
 * @author id:language_and_engineering
 * 
 */
@SuppressDebugLog
public class MGridLayout extends GridLayout implements IFWView, IFWLayoutView
{

    // このレイアウトが含んでいるビュー達
    public ArrayList<View> includingViews;

    // 描画が終了した内部Viewの個数
    private int numInflatedViews = 0;

    // NOTE: inflate後に後付けでaddして再度inflateする場合，すでに描画済みのViewについては下記の例外が発生する。
    // Caused by: java.lang.IllegalStateException:
    // The specified child already has a parent. You must call removeView() on
    // the child's parent first.
    // なので，各Viewが描画済みか・そうでないかを記憶しておく必要がある。

    public MGridLayout(Context context)
    {
        super(context);
        includingViews = new ArrayList<View>();
    }

    public MGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        includingViews = new ArrayList<View>();
    }

    // NOTE: このコンストラクタがないと描画時に下記の例外になる。
    // android.view.InflateException: Binary XML file line #～: Error inflating
    // class ～
    // @see http://q.hatena.ne.jp/1322903451

    // 独自レイアウトとして共通のメソッド

    @Override
    public MGridLayout add(View... v)
    {
        return (MGridLayout) LayoutUtil.addViewsToLayout(this, v);
    }

    @Override
    public void addOneIncludingView(View v)
    {
        this.includingViews.add(v);
    }

    @Override
    public int getNumInflatedViews() {
        return numInflatedViews;
    }

    @Override
    public void inflateInside()
    {
        LayoutUtil.renderAllUnrenderedViewsInsideLayout(this);
    }

    @Override
    public View getIncludingViewAt(int i) {
        return includingViews.get(i);
    }

    @Override
    public void updateNumInflatedViews() {
        numInflatedViews = getIncludingViewsSize();
    }

    @Override
    public int getIncludingViewsSize()
    {
        return includingViews.size();
    }

    @Override
    public void registerAndRenderOneView(View v)
    {
        LayoutUtil.registerAndRenderOneViewInLayout(v, this);
    }

    @Override
    public int[] getWidthHeightOfView(View v) {
        return LayoutUtil.getWidthHeightOfViewInLayout(v, this);
    }

    @Override
    public void removeAllIncludingViews() {
        this.includingViews.clear();
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

    public MGridLayout orientationHorizontal() {
        setOrientation(LinearLayout.HORIZONTAL);
        return this;
    }

    public MGridLayout orientationVertical() {
        setOrientation(LinearLayout.VERTICAL);
        return this;
    }

    public MGridLayout widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT);
        return this;
    }

    public MGridLayout widthMatchParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public MGridLayout widthWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    public MGridLayout widthPx(int px) {
        setViewParam("layout_width", px);
        return this;
    }

    public MGridLayout heightWrapContent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.WRAP_CONTENT);
        return this;
    }

    public MGridLayout heightFillParent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.FILL_PARENT);
        return this;
    }

    public MGridLayout heightMatchParent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public MGridLayout heightPx(int px) {
        setViewParam("layout_height", px);
        // http://stackoverflow.com/questions/1357577/how-to-set-the-width-of-a-layout-in-android
        return this;
    }

    public MGridLayout paddingPx(int px) {
        setPadding(px, px, px, px);
        return this;
    }

    public MGridLayout paddingLeftPx(int px) {
        setPadding(px, 0, 0, 0);
        return this;
    }

    public MGridLayout paddingBottomPx(int px) {
        setPadding(0, 0, 0, px);
        return this;
    }

    public MGridLayout backgroundColor(int color) {
        int c = getResources().getColor(color);
        setBackgroundColor(c);
        return this;
    }

    public MGridLayout backgroundDrawable(int resource) {
        setBackgroundDrawable(getResources().getDrawable(resource));
        return this;
    }

    public MGridLayout backgroundResource(int resource) {
        setBackgroundResource(resource);
        return this;
    }

    public MGridLayout click(OnClickListener l) {
        setOnClickListener(l);
        return this;
    }

    public MGridLayout columnCount(int columnCount) {
        this.setColumnCount(columnCount);
        return this;
    }

    public MGridLayout rowCount(int rowCount) {
        this.setRowCount(rowCount);
        return this;
    }

    // Viewを受け取って、自身に追加する処理を書く。
    public MGridLayout addViewByXY(int x, int xspec, int y, int yspec, View v) {
        GridLayout.LayoutParams params00 = new GridLayout.LayoutParams();
        params00.rowSpec = GridLayout.spec(x, xspec);
        params00.columnSpec = GridLayout.spec(y, yspec);
        return this;
    }

    // Viewを受け取って、自身に追加する処理を書く。
    public MGridLayout addViewByXY(int x, int y, View v) {
        return addViewByXY(x, 1, y, 1, v);
    }

}
