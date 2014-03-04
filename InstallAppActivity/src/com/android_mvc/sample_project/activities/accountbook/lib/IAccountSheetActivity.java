package com.android_mvc.sample_project.activities.accountbook.lib;

import java.util.Calendar;

import com.android_mvc.sample_project.db.entity.AccountSheetCell;

public interface IAccountSheetActivity {

    // 画面構成用メソッド
    abstract void makeDayCellLayout(AccountSheetCell accountSheet);
    abstract void makeHeaderLayout(Calendar dispCalendar);
    
    // 画面イベントメソッド
    abstract void showCalculator();
    abstract void onSelect();
    abstract void copyAccountSeetCell();
}
