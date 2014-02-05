package com.android_mvc.framework.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;

public class MPopup extends View {
    private PopupWindow mPopup;

    public MPopup(Context context) {
        super(context);
    }

    // Viewを持つPopupWindowを作成する
    public MPopup makePopupWindow(View v) {
        mPopup = new PopupWindow(this.getContext());
        mPopup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPopup.setContentView(v);
        return this;
    }

    // MyViewからの相対位置(x, y)にPopupWindowを表示する
    public void showPopupWindow(View parent, int offsetX, int offsetY) {
        int[] parentPosition = new int[2];
        parent.getLocationInWindow(parentPosition);

        Log.d("ParentPosition", "x = " + parentPosition[0] + ", y = " + parentPosition[1]);

        if (mPopup.isShowing()) {
//            View v = mPopup.getContentView();
//            int width = v.getMeasuredWidth();
//            int height = v.getMeasuredHeight();
//            mPopup.update(offsetX, offsetY, width, height);
        } else {
            mPopup.showAtLocation(parent, Gravity.NO_GRAVITY, parentPosition[0] + offsetX, parentPosition[1] + offsetY);
        }
    }

    public void showPopupWindow(View parent, int[] widthHeightOfViewInLayout) {
        int[] parentPosition = new int[2];
        parent.getLocationInWindow(parentPosition);
        // TODO 自動生成されたメソッド・スタブ
        Log.d("MPopup", "x = " + widthHeightOfViewInLayout[0] + ", y = " + widthHeightOfViewInLayout[1]);
        Log.d("ParentPosition", "x = " + parentPosition[0] + ", y = " + parentPosition[1]);

        showPopupWindow(parent, parentPosition[0], parentPosition[1]);
    }

    public void dismiss(){
        mPopup.dismiss();
    }
}
