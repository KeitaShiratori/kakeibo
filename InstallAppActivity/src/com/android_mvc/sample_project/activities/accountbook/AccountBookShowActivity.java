package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.activities.common.HooterMenu;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.AccountBookShowController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountBookShowActivity extends AccountBookAppUserBaseActivity {

    MLinearLayout layout1;
    MLinearLayout layout2;
    HooterMenu hooterMenu;
    MTextView tv1;
    MTextView tv2;

    // 全友達のリスト
    AccountBook accountBook;
    List<AccountBookDetail> accountBookDetails;

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

        // DBからロード
        accountBook = new AccountBookDAO(this).findAll().get(0);
        accountBookDetails = new AccountBookDetailDAO(this).findAll();
    }

    @Override
    public void defineContentView() {
        final AccountBookShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("目標金額照会")
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthFillParent()
                                .heightWrapContent()
                )
                .display();

        // コンテンツエリアの情報を表示するためのレイアウト
        layout1.add(
                // 最終目標
                accountBook.getHeader(context),
                accountBook.getDescription(context),
//                new MButton(context)
//                        .backgroundResource(R.drawable.button_design_3)
//                        .widthFillParent()
//                        .click(updateMokuhyouKingaku(accountBook))
//                ,
                // 空行
                new MTextView(context).paddingPx(15)
                ,
                // 月別目標のヘッダ
                accountBookDetails.get(0).getHeader(context)
                );

        for (int i = accountBookDetails.size() - 1; i >= 0; i--) {
            final AccountBookDetail a = accountBookDetails.get(i);
            layout1.add(
                    a.getDescription(context, new OnClickListener() {
                        public void onClick(View v) {
                            // クリック処理
                            inputDialogMokuhyouKingaku(a);
                        }
                    })
                    );

        }

        // 描画
        layout1.inflateInside();

    }

    private void inputDialogMokuhyouKingaku(final AccountBookDetail a) {

        Calendar c = a.getMokuhyouMonth();
        final MEditText et = new MEditText(context);
        new AlertDialog.Builder(AccountBookShowActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月の目標金額")
                .setView(et)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            Integer mokuhyouKingaku = Integer.parseInt(et.getText().toString());
                            a.setMokuhyouMonthKingaku(mokuhyouKingaku);
                            a.setAutoInputFlag(false);
                            AccountBookShowController.submit(AccountBookShowActivity.this, a);
                        } catch (NumberFormatException e) {
                            UIUtil.longToast(context, "数値を入力してください");
                        }
                    }

                })
                .setNegativeButton("自動入力ON",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                a.setAutoInputFlag(true);
                                AccountBookShowController.submit(AccountBookShowActivity.this, a);
                            }
                        }).show();
    }

    // 最終目標金額を更新するためのイベント
    private OnClickListener updateMokuhyouKingaku(final AccountBook ab) {
        // TODO 自動生成されたメソッド・スタブ
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 最終目標金額入力用EditText
                final MEditText et = new MEditText(context)
                        .hint("最終目標金額");

                // ダイアログを表示する。OKを押すと最終目標金額を更新する。
                new AlertDialog.Builder(AccountBookShowActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("最終目標金額の更新")
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Integer mokuhyouKingaku = Integer.parseInt(et.getText().toString());
                                    ab.setMokuhyouKingaku(mokuhyouKingaku);
                                    AccountBookShowController.submit(AccountBookShowActivity.this, "UPDATE_ACCOUNT_BOOK", ab);
                                } catch (NumberFormatException e) {
                                    UIUtil.longToast(context, "数値を入力してください");
                                }
                            }

                        })
                        .setNegativeButton("キャンセル",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // 何もしない
                                    }
                                }).show();

            }
        };
    }

    @Override
    public void onBackPressed()
    {
        // 戻るキーが押されたら終了
        moveTaskToBack(true);
    }

}