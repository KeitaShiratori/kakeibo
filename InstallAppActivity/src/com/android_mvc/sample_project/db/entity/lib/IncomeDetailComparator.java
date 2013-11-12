package com.android_mvc.sample_project.db.entity.lib;

import java.util.Calendar;
import java.util.Comparator;

import com.android_mvc.sample_project.db.entity.IncomeDetail;

public class IncomeDetailComparator  implements Comparator<IncomeDetail> {

    //比較メソッド（データクラスを比較して-1, 0, 1を返すように記述する）
    public int compare(IncomeDetail a, IncomeDetail b) {
    	
    	Calendar effectiveYMD1 = a.getEffectiveYMD();
    	Calendar effectiveYMD2 = b.getEffectiveYMD();

    	//こうすると有効日付の昇順でソートされる
        if (effectiveYMD1.after(effectiveYMD2)) {
            return 1;

        } else if (effectiveYMD1.equals(effectiveYMD2)) {
            return 0;

        } else {
            return -1;

        }
    }
}
