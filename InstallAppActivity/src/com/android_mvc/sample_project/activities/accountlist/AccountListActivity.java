package com.android_mvc.sample_project.activities.accountlist;

import java.util.Calendar;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountlist.data.AccountListActivityData;
import com.android_mvc.sample_project.activities.accountlist.lib.AccountListAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.AccountListDAO;
import com.android_mvc.sample_project.db.entity.AccountList;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountListCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountListActivity extends AccountListAppUserBaseActivity {

    // --------------------------------------------------//
    // ローカル変数定義
    // --------------------------------------------------//
    private List<AccountList> inputData;
    private AccountListActivityData accountListActivityData;

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
        final AccountListActivity activity = this;

        // 初期処理
        accountListActivityData = new AccountListActivityData();
        accountListActivityData.init(activity);

        // モード判定
        accountListActivityData.setMode(accountListActivityData.MODE_NORMAL);

        // 基準日取得。とりあえず、現在時刻を基準日とする。
        // TODO 基準日を渡された場合は、渡された基準日を採用する
        accountListActivityData.setStartDate(Calendar.getInstance());

        // 貯金シートと貯金シートセルを取得
        int counter = accountListActivityData.getStartDate().getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] days = new String[counter];
        Calendar tmp = (Calendar) accountListActivityData.getStartDate().clone();
        tmp.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < counter; i++) {
            days[i] = LPUtil.encodeCalendarToText(tmp);
            tmp.add(Calendar.DAY_OF_MONTH, 1);
        }
        List<AccountList> list = new AccountListDAO(activity).findWhereInValues(AccountListCol.YMD, days);

        // 親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText(s(R.string.TYOKIN_SHITO))
                // .setDisplayHeader(
                // accountListActivityData.getAccountList().getHeader(activity))
                .add(
                        accountListActivityData.getLayout1()
                )
                .display();

        // コンテンツエリアのコンポーネント設定
        accountListActivityData.getLayout1()
                .add(
                );

        // 描画
        accountListActivityData.getLayout1().inflateInside();
    }

    private OnClickListener submit(final String modeBtn) {
        final AccountListActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // AccountListController.submit();
            }
        };
    }

}