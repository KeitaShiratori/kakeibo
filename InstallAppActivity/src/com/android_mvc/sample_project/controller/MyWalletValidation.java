package com.android_mvc.sample_project.controller;


import com.android_mvc.framework.controller.validation.ValidationResult;
import com.android_mvc.sample_project.activities.accountbook.MyWalletShowActivity;
import com.android_mvc.sample_project.controller.util.ValidationsUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

/**
 * DB操作系の画面のバリデーション処理の記述。
 * @author id:language_and_engineering
 *
 */
public class MyWalletValidation extends ValidationsUtil
{

    // Activityごとに引数の型を変えてオーバーロードする。

    /**
     * 財布の中身画面での入力値を検証
     */
    public ValidationResult validate(MyWalletShowActivity activity)
    {
        initValidationOf(activity);

        // 日付のチェック
        assertNotEmpty(MyWalletCol.YMD);

        // 金額のチェック
        assertValidInteger(MyWalletCol.KINGAKU);

        // 残高のチェック
        assertValidInteger(MyWalletCol.ZANDAKA);

        // 引き出しのチェック
        assertValidInteger(MyWalletCol.HIKIDASHI);
        
        return getValidationResult();
    }


    
}
