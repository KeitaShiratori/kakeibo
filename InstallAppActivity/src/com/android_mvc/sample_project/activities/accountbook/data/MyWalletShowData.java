package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.MyWallet;

public class MyWalletShowData {
    MLinearLayout layout1;

    // １レコード作成するための情報
    private List<Calendar> LabelYMD;
    private List<MyWallet> myWallets;
    private List<CostDetail> CostDetails;

    // MyWalletShowActivity制御用データ
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

    public List<Calendar> getLabelYMD() {
        return LabelYMD;
    }

    public void setLabelYMD(List<Calendar> labelYMD) {
        LabelYMD = labelYMD;
    }

    public List<MyWallet> getMyWallets() {
        return myWallets;
    }

    public void setMyWallets(List<MyWallet> myWallets) {
        this.myWallets = myWallets;
    }

    public List<CostDetail> getCostDetails() {
        return CostDetails;
    }

    public void setCostDetails(List<CostDetail> costDetails) {
        CostDetails = costDetails;
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
                .heightFillParent();

        // modeとstartDateのデフォルト値を設定
        mode = "DEFAULT";
        startDate = Calendar.getInstance();
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
}
