package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;

import android.content.Context;
import android.view.Gravity;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;

public class BudgetRecordData {

    // メンバ変数
    Calendar yoteiYYYYMM;
    Integer costSum;
    Integer incomeSum;
    Integer disposablencome;
    Integer mokuhyouKingaku;
    int startDate;

    public BudgetRecordData(Context context) {
        yoteiYYYYMM = Calendar.getInstance();
        costSum = 0;
        incomeSum = 0;
        disposablencome = 0;
        mokuhyouKingaku = 0;
        startDate = new AccountBookDAO(context).findAll().get(0).getStartDate().get(Calendar.DAY_OF_MONTH);

    }

    public Calendar getYoteiYYYYMM() {
        return yoteiYYYYMM;
    }

    public void setYoteiYYYYMM(Calendar yoteiYYYYMM) {
        this.yoteiYYYYMM = yoteiYYYYMM;
    }

    public Integer getCostSum() {
        return costSum;
    }

    public void setCostSum(Integer costSum) {
        this.costSum = costSum;
    }

    public Integer getIncomeSum() {
        return incomeSum;
    }

    public void setIncomeSum(Integer incomeSum) {
        this.incomeSum = incomeSum;
    }

    public Integer getDisposablencome() {
        return disposablencome;
    }

    public void setDisposablencome(Integer disposablencome) {
        this.disposablencome = disposablencome;
    }

    public Integer getMokuhyouKingaku() {
        return mokuhyouKingaku;
    }

    public void setMokuhyouKingaku(Integer mokuhyouKingaku) {
        this.mokuhyouKingaku = mokuhyouKingaku;
    }

    // カスタムG&S

    public MLinearLayout getDescription(Context context) {

        yoteiYYYYMM.set(Calendar.DAY_OF_MONTH, startDate);
        Calendar nextYMD = (Calendar) yoteiYYYYMM.clone();
        nextYMD.add(Calendar.MONTH, 1);
        nextYMD.add(Calendar.DAY_OF_MONTH, -1);

        MLinearLayout ret = new MLinearLayout(context)
                .orientationVertical()
                .widthWrapContent()
                .heightWrapContent()
                .paddingLeftPx(10)
                .backgroundDrawable(R.drawable.border)
                .add(
                        new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text(yoteiYYYYMM.get(Calendar.YEAR) + "/"
                                                        + (yoteiYYYYMM.get(Calendar.MONTH) + 1) + "/"
                                                        + yoteiYYYYMM.get(Calendar.DAY_OF_MONTH)
                                                        + "～"
                                                        + nextYMD.get(Calendar.YEAR) + "/"
                                                        + (nextYMD.get(Calendar.MONTH) + 1) + "/"
                                                        + nextYMD.get(Calendar.DAY_OF_MONTH)
                                                        + "  ")
                                                .backgroundDrawable(drawable.record_design)
                                                .textsize(18)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("残り: " + disposablencome + "円")
                                                .backgroundDrawable(drawable.record_design)
                                                .textsize(18)
                                                .textColor(disposablencome < 0 ? android.graphics.Color.RED : android.graphics.Color.BLACK)
                                                .widthFillParent()
                                )
                        ,
                        new MLinearLayout(context)
                                .orientationHorizontal()
                                .widthFillParent()
                                .heightWrapContent()
                                .add(

                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("収入: " + incomeSum + "円")
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("貯金: " + mokuhyouKingaku + "円")
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("支出: " + costSum + "円")
                                                .backgroundDrawable(drawable.record_design)
                                                .widthWrapContent()
                                )
                );
        return ret;
    }

}
