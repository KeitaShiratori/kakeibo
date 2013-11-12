package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;

import com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue;

public class SettleRecordData implements ControlBreakKeyAndValue {

	//メンバ変数
	Calendar yoteiYYYYMM;
	Integer costSum;
	Integer incomeSum;
	Integer disposablencome;

	public SettleRecordData(){
		yoteiYYYYMM = Calendar.getInstance();
		costSum = 0;
		incomeSum = 0;
		disposablencome = 0;
		
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
	
	
	// カスタムG&S
	public String getDescription(){
		String ret;
		//年月、可処分所得、所得小計、費用小計
		//YYYY年MM月, 99999円, 99999円, 99999円
		ret = yoteiYYYYMM.get(Calendar.YEAR) + "年"
				+ (yoteiYYYYMM.get(Calendar.MONTH) +1) + "月, "
				+ disposablencome + "円, "
				+ incomeSum + "円, "
				+ costSum + "円";
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
