package com.android_mvc.sample_project.controller;


import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.AccountBookShowActivity;
import com.android_mvc.sample_project.activities.installation.InstallCompletedActivity;
import com.android_mvc.sample_project.controller.util.ValidationsUtil;

/**
 * DB操作系の画面のバリデーション処理の記述。
 * @author id:language_and_engineering
 *
 */
public class AccountBookShowValidation extends ValidationsUtil
{

    // Activityごとに引数の型を変えてオーバーロードする。

    /**
     * 家計簿表示画面での入力値を検証
     */
    public ValidationResult validate(AccountBookShowActivity activity)
    {
        initValidationOf(activity);

        return getValidationResult();
    }

}
