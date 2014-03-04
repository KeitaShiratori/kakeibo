package com.android_mvc.sample_project.activities.accountsheet;

import java.util.Calendar;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.lib.IAccountSheetActivity;
import com.android_mvc.sample_project.activities.accountsheet.data.AccountSheetActivityData;
import com.android_mvc.sample_project.activities.accountsheet.lib.AccountSheetAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.AccountSheetCellDAO;
import com.android_mvc.sample_project.db.dao.AccountSheetDAO;
import com.android_mvc.sample_project.db.entity.AccountSheet;
import com.android_mvc.sample_project.db.entity.AccountSheetCell;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCellCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountSheetActivity extends AccountSheetAppUserBaseActivity implements IAccountSheetActivity {

    // --------------------------------------------------//
    // ローカル変数定義
    // --------------------------------------------------//
    private List<AccountSheetCell> inputData;
    private AccountSheetActivityData accountSheetActivityData;

    @Override
    public boolean requireProcBeforeUI() {
        // UI構築前に処理を要求する
        return true;
    }

    // UI構築前に別スレッドで実行される処理
    @Override
    public void procAsyncBeforeUI() {
        //
        Util.d("UI構築前に実行される処理です。");

    }

    @Override
    public void defineContentView() {
        final AccountSheetActivity activity = this;

        // 初期処理
        accountSheetActivityData = new AccountSheetActivityData();
        accountSheetActivityData.init(activity);

        // モード判定
        accountSheetActivityData.setMode(accountSheetActivityData.MODE_NORMAL);

        // 基準日取得。とりあえず、現在時刻を基準日とする。
        // TODO 基準日を渡された場合は、渡された基準日を採用する
        accountSheetActivityData.setStartDate(Calendar.getInstance());

        // 貯金シートと貯金シートセルを取得
        List<AccountSheet> sheets = new AccountSheetDAO(activity).findWhere(AccountSheetCol.YMD, accountSheetActivityData.getStartDate());

        if (sheets.isEmpty()) {
            AccountSheet a = new AccountSheetDAO(activity).create(Calendar.getInstance());
            sheets.add(a);
        }

        accountSheetActivityData.setAccountSheet(sheets.get(0));
        sheets = null;

        int counter = accountSheetActivityData.getStartDate().getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] days = new String[counter];
        Calendar tmp = (Calendar) accountSheetActivityData.getStartDate().clone();
        tmp.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < counter; i++) {
            days[i] = LPUtil.encodeCalendarToText(tmp);
            tmp.add(Calendar.DAY_OF_MONTH, 1);
        }
        List<AccountSheetCell> cells = new AccountSheetCellDAO(activity).findWhereInValues(AccountSheetCellCol.YMD, days);

        // 親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText(s(R.string.TYOKIN_SHITO))
                // .setDisplayHeader(
                // accountSheetActivityData.getAccountSheet().getHeader(activity))
                .add(
                        accountSheetActivityData.getLayout1()
                )
                .display();

        // コンテンツエリアのコンポーネント設定
        accountSheetActivityData.getLayout1()
                .add(
//                        accountSheetActivityData.getCalendarView()
                );

        // 描画
        accountSheetActivityData.getLayout1().inflateInside();
    }

    private OnClickListener submit(final String modeBtn) {
        final AccountSheetActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // AccountSheetController.submit();
            }
        };
    }

    @Override
    public void makeDayCellLayout(AccountSheetCell accountSheet) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void makeHeaderLayout(Calendar dispCalendar) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void showCalculator() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onSelect() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void copyAccountSeetCell() {
        // TODO 自動生成されたメソッド・スタブ

    }

}