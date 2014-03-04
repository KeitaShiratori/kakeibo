package com.android_mvc.sample_project.activities.accountlist.data;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;

import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.sample_project.db.entity.AccountList;

public class AccountListActivityData {
    // 画面描画用のレイアウト
    MLinearLayout layout1;

    // １レコード作成するための情報
    private List<AccountList> accountList;

    // 制御用変数
    private int mode;
    private Calendar startDate;

    // 定数定義
    public static int MODE_NORMAL = 0;
    public static int MODE_SELECTION = 1;
    public static int MODE_KIKAN = 2;

    // IDE自動生成のゲッター・セッター

    public List<AccountList> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountList> accountList) {
        this.accountList = accountList;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public MLinearLayout getLayout1() {
        return layout1;
    }

    public void setLayout1(MLinearLayout layout1) {
        this.layout1 = layout1;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    // カスタムG&S
    public void init(Activity context) {
        // mode変更用ボタンの親レイアウト
        layout1 = new MLinearLayout(context)
                .orientationVertical()
                .widthFillParent()
                .heightFillParent();

    }

}
