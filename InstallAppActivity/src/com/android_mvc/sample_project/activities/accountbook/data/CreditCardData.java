package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;

public class CreditCardData {
    // 画面UI部品
    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;

    // １レコード作成するための情報
    private CreditCardSetting creditCardSetting;

    // 制御用データ
    private String mode;
    private Calendar startDate;

    // mode変更用ボタンの親レイアウト
    private MLinearLayout modeButtons;

    // mode変更用ボタンの子要素
    private MButton btnDayMode;
    private MButton btnWeekMode;
    private MButton btnMonthMode;
    private MButton btnAfter;
    private MButton btnBefore;

    // IDE自動生成のゲッター・セッター

    public MLinearLayout getLayout1() {
        return layout1;
    }

    public void setLayout1(MLinearLayout layout1) {
        this.layout1 = layout1;
    }

    public MTextView getTv1() {
        return tv1;
    }

    public void setTv1(MTextView tv1) {
        this.tv1 = tv1;
    }

    public MTextView getTv2() {
        return tv2;
    }

    public void setTv2(MTextView tv2) {
        this.tv2 = tv2;
    }

    public CreditCardSetting getCreditCardSetting() {
        return creditCardSetting;
    }

    public void setCreditCardSetting(CreditCardSetting creditCardSetting) {
        this.creditCardSetting = creditCardSetting;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public MLinearLayout getModeButtons() {
        return modeButtons;
    }

    public void setModeButtons(MLinearLayout modeButtons) {
        this.modeButtons = modeButtons;
    }

    public MButton getBtnDayMode() {
        return btnDayMode;
    }

    public void setBtnDayMode(MButton btnDayMode) {
        this.btnDayMode = btnDayMode;
    }

    public MButton getBtnWeekMode() {
        return btnWeekMode;
    }

    public void setBtnWeekMode(MButton btnWeekMode) {
        this.btnWeekMode = btnWeekMode;
    }

    public MButton getBtnMonthMode() {
        return btnMonthMode;
    }

    public void setBtnMonthMode(MButton btnMonthMode) {
        this.btnMonthMode = btnMonthMode;
    }

    public MButton getBtnAfter() {
        return btnAfter;
    }

    public void setBtnAfter(MButton btnAfter) {
        this.btnAfter = btnAfter;
    }

    public MButton getBtnBefore() {
        return btnBefore;
    }

    public void setBtnBefore(MButton btnBefore) {
        this.btnBefore = btnBefore;
    }

    // カスタムG&S
    public void init(Activity context) {
        // mode変更用ボタンの親レイアウト
        modeButtons = new MLinearLayout(context)
                .orientationHorizontal()
                .widthWrapContent()
                .heightWrapContent();

        layout1 = new MLinearLayout(context)
                .orientationVertical()
                .widthFillParent()
                .paddingPx(10)
                .heightFillParent();

    }

    public void makeModeButtons() {
        this.modeButtons.add(
                btnBefore,

                btnDayMode,
                btnWeekMode,
                btnMonthMode,
                btnAfter
                );

    }

    public MLinearLayout makeRecord(Context context) {

        String simeYmd = new String();
        String siharaiYmd = new String();

        if (creditCardSetting != null) {
            simeYmd = (creditCardSetting.getSimeYmd() != null) ? creditCardSetting.getSimeYmd().get(Calendar.DAY_OF_MONTH) + "日" : "未入力";
            siharaiYmd = (creditCardSetting.getSiharaiYmd() != null) ? creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH) + "日" : "未入力";
        }
        else{
            simeYmd = "未入力";
            siharaiYmd = "未入力";
        }
        tv1 = new MTextView(context)
                .text(simeYmd)
                .backgroundResource(R.drawable.button_design_h30_w173)
                .gravity(Gravity.CENTER_VERTICAL);
        tv1.click(Util.createNumberPickerDialog(context, tv1));

        tv2 = new MTextView(context)
                .text(siharaiYmd)
                .backgroundResource(R.drawable.button_design_h30_w172)
                .gravity(Gravity.CENTER_VERTICAL);
        tv2.click(Util.createNumberPickerDialog(context, tv2));

        return new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(
                        tv1, tv2
                );
    }

}
