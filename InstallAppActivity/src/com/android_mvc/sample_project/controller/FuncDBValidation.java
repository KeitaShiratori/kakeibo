package com.android_mvc.sample_project.controller;


import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.CostDetailEditActivity;
import com.android_mvc.sample_project.activities.accountbook.IncomeDetailEditActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.controller.util.ValidationsUtil;

/**
 * DB操作系の画面のバリデーション処理の記述。
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

//        assertNotEmpty("budget_ymd");

        assertNotEmpty("budget_cost");
        assertValidInteger("budget_cost");
        assertNumberOperation("budget_cost", greaterThan(0));

        assertValidInteger("settle_cost");

        return getValidationResult();
    }

	public ValidationResult validate(InstallCompletedActivity activity) {

        initValidationOf(activity);
        
        assertValidInteger("mokuhyou_kingaku");	
        assertNumberOperation("mokuhyou_kingaku", greaterThan(0));

        assertValidInteger("mokuhyou_kikan");	
        assertNumberOperation("mokuhyou_kikan", greaterThan(0));
        

        return getValidationResult();
	}
	
    /**
     * DB登録画面での入力値を検証
     */
    public ValidationResult validate(IncomeDetailEditActivity activity)
    {
        initValidationOf(activity);

//        assertNotEmpty("budget_ymd");

        assertNotEmpty("budget_income");
        assertValidInteger("budget_income");
        assertNumberOperation("budget_income", greaterThan(0));

        assertValidInteger("settle_income");

        return getValidationResult();
    }


}
