package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;

import android.content.Context;
import android.view.Gravity;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue;

public class SettleRecordData implements ControlBreakKeyAndValue {

    // メンバ変数
    Calendar yoteiYYYYMM;
    Integer costSum;
    Integer incomeSum;
    Integer disposablencome;
    Integer mokuhyouKingaku;

    public SettleRecordData() {
        yoteiYYYYMM = Calendar.getInstance();
        costSum = 0;
        incomeSum = 0;
        disposablencome = 0;
        mokuhyouKingaku = 0;

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
                                                .text(yoteiYYYYMM.get(Calendar.YEAR) + "年"
                                                        + (yoteiYYYYMM.get(Calendar.MONTH) + 1) + "月")
                                                .backgroundDrawable(R.drawable.record_design)
                                                .textsize(18)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("可処分所得: " + disposablencome + "円")
                                                .backgroundDrawable(R.drawable.record_design)
                                                .textsize(18)
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
                                                .backgroundDrawable(R.drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("貯金: " + mokuhyouKingaku + "円")
                                                .backgroundDrawable(R.drawable.record_design)
                                                .widthWrapContent()
                                        ,
                                        new MTextView(context)
                                                .gravity(Gravity.CENTER_VERTICAL)
                                                .text("支出: " + costSum + "円")
                                                .backgroundDrawable(R.drawable.record_design)
                                                .widthWrapContent()
                                )
                );
        return ret;
    }

    @Override
    public Object getLeveledKey(int level) {

        Object ret = null;

        switch (level) {
        case 0:
            break;

        case 1:
            ret = 1;
            break;

        case 2:
            ret = 1;
            break;

        default:
            break;
        }

        return ret;
    }

    @Override
    public int getValue() {
        // TODO 自動生成されたメソッド・スタブ
        return incomeSum;
    }

}
