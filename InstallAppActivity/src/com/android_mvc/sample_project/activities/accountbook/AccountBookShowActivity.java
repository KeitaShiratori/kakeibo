package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
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
import com.android_mvc.sample_project.activities.accountbook.data.BudgetRecordData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.activities.common.HooterMenu;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.AccountBookShowController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

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
        try {
            accountBook = new AccountBookDAO(this).findAll().get(0);
            accountBookDetails = new AccountBookDetailDAO(this).findAll();
        } catch (Exception e) {
            AccountBookShowController.submit(this, "InstallCompletedActivity");
        }
    }

    @Override
    public void defineContentView() {
        final AccountBookShowActivity activity = this;

        // まず親レイアウトを定義
        UIBuilder uiBuildar = new UIBuilder(context)
                .setDisplayHeaderText("目標金額照会")
                .add(
                        layout1 = new MLinearLayout(context)
                                .orientationVertical()
                                .widthFillParent()
                                .paddingPx(10)
                                .heightWrapContent()
                );

        // コンテンツエリアの情報を表示するためのレイアウト
        layout1.add(
                // 最終目標
                accountBook.getHeader(context),
                accountBook.getDescription(context),
                new MLinearLayout(activity)
                        .orientationHorizontal()
                        .widthFillParent()
                        .heightWrapContent()
                        .add(
                                new MButton(context)
                                        .text("最終目標変更")
                                        .backgroundDrawable(R.drawable.button_design_h30_w115)
                                        .click(updateMokuhyouKingaku(accountBook))
                                ,
                                new MButton(context)
                                        .text("期間延長")
                                        .backgroundDrawable(R.drawable.button_design_h30_w115)
                                        .click(updateMokuhyouKikan(accountBook))
                                ,
                                new MButton(context)
                                        .text("基準日変更")
                                        .backgroundDrawable(R.drawable.button_design_h30_w115)
                                        .click(updateStartDate(accountBook))
                        )
                ,
                // 空行
                new MTextView(context)
                        .paddingPx(5)
                        .textsize(1)
                ,
                // 月別目標のヘッダ
                accountBookDetails.get(0).getHeader(context)
                );

        boolean isAllManualInput = true;
        long sum = 0;

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

            isAllManualInput = isAllManualInput && !a.getAutoInputFlag();
            sum += a.getMokuhyouMonthKingaku();
        }

        if (isAllManualInput) {
            // 全レコードが自動入力OFFの場合、合計金額があっているかチェックする
            if (accountBook.getMokuhyouKingaku() != sum) {
                // ダイアログの設定
                final String title1 = "月別目標金額の合計が不正です。";
                final String content1 = "自動入力をONにするか、最終目標金額を修正してください。";

                // ダイアログの生成
                Util.createAlertDialogWithOKButton(
                        activity, title1, content1, R.drawable.icon, null);

            }
        }

        // 描画
        if (pref.getTutorialDoneFlagAccountBookShow2(context)) {
            layout1.add(
                    // 空行
                    new MTextView(context)
                            .paddingPx(5)
                            .textsize(1)
                    ,
                    new MLinearLayout(activity)
                            .orientationHorizontal()
                            .widthFillParent()
                            .heightWrapContent()
                            .add(
                                    new MButton(context)
                                            .text("月末締め処理")
                                            .backgroundDrawable(R.drawable.button_design_h30_w173)
                                            .click(doSime())
                                    ,
                                    new MButton(context)
                                            .text("締め解除")
                                            .backgroundDrawable(R.drawable.button_design_h30_w172)
                                            .click(undoSime())
                            )
                    );
            uiBuildar.display();
        }
        else {
            uiBuildar.displayWithoutHooter();
            layout1.add(
                    new MButton(context)
                            .text("月別目標設定完了")
                            .backgroundDrawable(R.drawable.button_design_1)
                            .widthMatchParent()
                            .click(tutorialComplete(context)));
        }
        layout1.inflateInside();

    }

    private OnClickListener doSime() {
        final AccountBookShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = "月末締め処理の実行（TEST）";
                String contents = "月末締め処理を行います。";
                Util.createAlertDialogWithOKButton(activity, title, contents, R.drawable.icon, null);
            }
        };
    }

    private OnClickListener undoSime() {
        final AccountBookShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = "月末締め処理の解除（TEST）";
                String contents = "月末締め処理を解除します。";
                Util.createAlertDialogWithOKButton(activity, title, contents, R.drawable.icon, null);
            }
        };
    }

    private OnClickListener updateMokuhyouKikan(final AccountBook ab) {
        final AccountBookShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = "目標期間の延長（TEST）";
                String contents = "目標期間の延長を行います。"
                        + "\n※目標期間の短縮はできません。"
                        + "\n"
                        + "\n目標期間を延長しますか？";
                Util.createAlertDialogWithOKButton(activity, title, contents, R.drawable.icon, null);
            }
        };
    }

    private void inputDialogMokuhyouKingaku(final AccountBookDetail a) {

        Calendar c = a.getMokuhyouMonth();
        final MEditText et = new MEditText(context);
        new AlertDialog.Builder(AccountBookShowActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月の目標金額")
                .setView(et)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
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
                .setNegativeButton("自動入力をON",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                a.setAutoInputFlag(true);
                                AccountBookShowController.submit(AccountBookShowActivity.this, a);
                            }
                        }).show();
    }

    // 最終目標金額を更新するためのイベント
    private OnClickListener updateMokuhyouKingaku(final AccountBook ab) {
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

    /**
     * 基準日を更新するためのイベント
     * 
     * @param ab
     * @return
     */
    private OnClickListener updateStartDate(final AccountBook ab) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 基準日入力用NumberPicker
                final NumberPicker np1 = new NumberPicker(context);
                np1.setMaxValue(28);
                np1.setMinValue(1);
                np1.setBackgroundColor(context.getResources().getColor(R.color.white));
                np1.setFocusable(true);
                np1.setFocusableInTouchMode(true);

                // ダイアログを表示する。OKを押すと最終目標金額を更新する。
                new AlertDialog.Builder(AccountBookShowActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("最終目標金額の更新")
                        .setView(np1)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Integer input = np1.getValue();
                                    ab.getStartDate().set(Calendar.DAY_OF_MONTH, input);
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
    public void onResume() {
        super.onResume();

        if (pref.getTutorialDoneFlagAccountBookShow1(context)) {
            // チュートリアルが完了していたら何もしない
            return;
        }

        // チュートリアルが完了していない場合
        final AccountBookShowActivity activity = this;
        final int icon = R.drawable.icon;

        // ダイアログの設定
        final String title1 = "チュートリアル";
        final String content1 = "最終目標が決まりました！"
                + "\n"
                + "\n次に、毎月の貯金計画を立てましょう。"
                + "\n月ごとの目標貯金額を設定してください。";

        // OKボタンを押した時の動作
        android.content.DialogInterface.OnClickListener click = new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // チュートリアル済みフラグを立てる。
                pref.updateTutorialDoneFlagAccountBookShow1(context, true);
            }
        };

        // ダイアログの生成
        Util.createAlertDialogWithOKButton(
                activity, title1, content1, icon, click);

    }

    private OnClickListener tutorialComplete(final Context context) {
        final AccountBookShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ダイアログの設定
                final String title1 = "チュートリアル";
                final String content1 = "月別目標が決まりました！"
                        + "\n"
                        + "\n次に、今月の収入を登録しましょう！";

                // OKボタンを押した時の動作
                android.content.DialogInterface.OnClickListener click = new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // チュートリアル済みフラグを立てる。
                        pref.updateTutorialDoneFlagAccountBookShow2(context, true);

                        // 収入登録画面へ遷移
                        AccountBookShowController.submit(activity, "EDIT_INCOME_DETAIL");
                    }
                };

                // ダイアログの生成
                Util.createAlertDialogWithOKButton(
                        activity, title1, content1, R.drawable.icon, click);
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