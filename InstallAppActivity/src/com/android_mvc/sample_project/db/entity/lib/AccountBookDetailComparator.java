package com.android_mvc.sample_project.db.entity.lib;

import java.util.Calendar;
import java.util.Comparator;

import com.android_mvc.sample_project.db.entity.AccountBookDetail;

public class AccountBookDetailComparator  implements Comparator<AccountBookDetail> {

    //比較メソッド（データクラスを比較して-1, 0, 1を返すように記述する）
    public int compare(AccountBookDetail a, AccountBookDetail b) {
    	
    	Calendar effectiveYMD1 = a.getMokuhyouMonth();
    	Calendar effectiveYMD2 = b.getMokuhyouMonth();

    	//こうすると有効日付の昇順でソートされる
        if (effectiveYMD1.before(effectiveYMD2)) {
            return -1;

        } else if (effectiveYMD1.equals(effectiveYMD2)) {
            return 0;

        } else {
            return 1;

        }
    }
}
