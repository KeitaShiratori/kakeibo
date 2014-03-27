package com.android_mvc.sample_project.controller;

import java.util.Calendar;
import java.util.List;

import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.util.ValidationsUtil;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;

/**
 * DB操作系の画面のバリデーション処理の記述。
 * 
 * @author id:language_and_engineering
 * 
 */
public class FuncDBValidation extends ValidationsUtil
{

    // Activityごとに引数の型を変えてオーバーロードする。

    /**
     * DB登録画面での入力値を検証
     */
    public ValidationResult validate(CostDetailEditActivity activity)
    {
        initValidationOf(activity);
        AccountBook ab = new AccountBookDAO(activity).findAll().get(0);
        Util.d("基準日：" + ab.getStartDate().getTime());
        List<AccountBookDetail> abd = new AccountBookDetailDAO(activity).findAll();

        Calendar fromYMD = null;
        Calendar toYMD = null;

        if (abd.get(0).getSimeFlag()) {
            vres.err("全期間で月末締め処理が完了しています。");
            return getValidationResult();
        }
        else {
            toYMD = abd.get(0).getMokuhyouMonth();
            toYMD.add(Calendar.MONTH, 1);
            toYMD.set(Calendar.DAY_OF_MONTH, ab.getStartDate().get(Calendar.DAY_OF_MONTH) - 1);
            toYMD.set(Calendar.HOUR_OF_DAY, 23);
            toYMD.set(Calendar.MINUTE, 59);
            toYMD.set(Calendar.SECOND, 59);
        }
        // 締めた月は除き、開始日を求める。
        for (int i = abd.size() - 1; i >= 0; i--) {
            if (!abd.get(i).getSimeFlag()) {
                fromYMD = abd.get(i).getMokuhyouMonth();
                fromYMD.set(Calendar.DAY_OF_MONTH, ab.getStartDate().get(Calendar.DAY_OF_MONTH));
                fromYMD.set(Calendar.HOUR_OF_DAY, 0);
                fromYMD.set(Calendar.MINUTE, 0);
                fromYMD.set(Calendar.SECOND, 0);
                break;
            }
        }

        Util.d("fromYMD: " + fromYMD.getTime());
        Util.d("toYMD: " + toYMD.getTime());
        assertNotEmpty(CostDetailCol.BUDGET_YMD);
        assertCalendarOperation(CostDetailCol.BUDGET_YMD, after(fromYMD));
        assertCalendarOperation(CostDetailCol.BUDGET_YMD, before(toYMD));

        assertNotEmpty("budget_cost");
        assertValidInteger("budget_cost");
        assertValidInteger("settle_cost");

        return getValidationResult();
    }

    public ValidationResult validate(InstallCompletedActivity activity) {

        initValidationOf(activity);

        // 未入力チェック
        assertNotEmpty(AccountBookCol.MOKUHYOU_KIKAN);
        assertNotEmpty(AccountBookCol.MOKUHYOU_KINGAKU);
        assertNotEmpty(AccountBookCol.START_DATE);

        // 数値チェック
        assertValidInteger(AccountBookCol.MOKUHYOU_KINGAKU);
        assertNumberOperation(AccountBookCol.MOKUHYOU_KINGAKU, greaterThan(0));

        assertValidInteger(AccountBookCol.MOKUHYOU_KIKAN);
        assertNumberOperation(AccountBookCol.MOKUHYOU_KIKAN, lessThan(25));

        return getValidationResult();
    }

    /**
     * DB登録画面での入力値を検証
     */
    public ValidationResult validate(IncomeDetailEditActivity activity)
    {
        initValidationOf(activity);

        assertNotEmpty(IncomeDetailCol.BUDGET_YMD);

        assertNotEmpty(IncomeDetailCol.BUDGET_INCOME);
        assertValidInteger(IncomeDetailCol.BUDGET_INCOME);

        assertValidInteger(IncomeDetailCol.BUDGET_INCOME);

        return getValidationResult();
    }

}
