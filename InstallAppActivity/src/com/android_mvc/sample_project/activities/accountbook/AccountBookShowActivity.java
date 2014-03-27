package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.data.SettleRecordData;
import com.android_mvc.sample_project.activities.accountbook.data.SettleShowActivityData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.activities.common.HooterMenu;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.AccountBookShowController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
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
                doSime1();
            }

        };
    }

    private void doSime1() {
        String title1 = "月末締め処理の実行（TEST）";
        String contents1 = "月末締め処理を行います。";
        android.content.DialogInterface.OnClickListener click1 = doSime2();

        Util.createAlertDialogWithOKandCancelButtons(this,
                title1, contents1, R.drawable.icon,
                click1, null);
    }

    private android.content.DialogInterface.OnClickListener doSime2() {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 締め処理の内容を記載
                // 処理対象月の取得
                AccountBookDetail target = getTargetAccountBookDetail();
                Calendar targetMonth = getTargetMonth(target);
                if (!chkTargetMonth(targetMonth)) {
                    // 対象月チェックでエラーがあれば処理終了。
                    return;
                }

                Calendar nextMonth = (Calendar) targetMonth.clone();
                nextMonth.add(Calendar.MONTH, 1);
                nextMonth.add(Calendar.DAY_OF_MONTH, -1);
                nextMonth.set(Calendar.HOUR_OF_DAY, 23);
                nextMonth.set(Calendar.MINUTE, 59);
                nextMonth.set(Calendar.SECOND, 59);
                nextMonth.set(Calendar.MILLISECOND, 999);

                String title2 = "締め対象月の確認";
                String contents2 = LPUtil.encodeCalendarToTextYMD(targetMonth)
                        + "～" + LPUtil.encodeCalendarToTextYMD(nextMonth)
                        + "を対象に月末締め処理を実行します。"
                        + "\nよろしいですか？";
                android.content.DialogInterface.OnClickListener click2 = doSime3(targetMonth, nextMonth, target);

                Util.createAlertDialogWithOKandCancelButtons(AccountBookShowActivity.this,
                        title2, contents2, R.drawable.icon,
                        click2, null);

            }

        };
    }

    private AccountBookDetail getTargetAccountBookDetail() {
        AccountBookDetail ret = null;
        if (accountBookDetails != null || accountBookDetails.isEmpty()) {
            for (int i = accountBookDetails.size() - 1; i >= 0; i--) {
                // 締めフラグが立っていない家計簿明細があれば、その月を取得する。
                if (!accountBookDetails.get(i).getSimeFlag()) {
                    ret = accountBookDetails.get(i);
                    break;
                }
            }
        }
        return ret;
    }

    private Calendar getTargetMonth(AccountBookDetail abd) {
        Calendar ret = null;
        ret = abd.getMokuhyouMonth();
        ret.set(Calendar.DAY_OF_MONTH, accountBook.getStartDate().get(Calendar.DAY_OF_MONTH));
        ret.set(Calendar.HOUR_OF_DAY, 0);
        ret.set(Calendar.MINUTE, 0);
        ret.set(Calendar.SECOND, 0);
        ret.set(Calendar.MILLISECOND, 0);
        return ret;
    }

    private boolean chkTargetMonth(Calendar targetMonth) {
        return targetMonth != null;
    }

    private android.content.DialogInterface.OnClickListener doSime3(final Calendar targetMonth, final Calendar nextMonth, final AccountBookDetail target) {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 実績照会の残り金額を取得
                SettleRecordData settleRecordData = null;
                SettleShowActivityData settleShowActivityData = new SettleShowActivityData();
                settleShowActivityData.init(AccountBookShowActivity.this);

                Util.d("マッチングを実行します。targetMonth.getTime(): " + targetMonth.getTime());
                Util.d("マッチングを実行します。nextMonth.getTime(): " + nextMonth.getTime());
                for (SettleRecordData s : settleShowActivityData.getSettleRecordData()) {
                    if (s.getYoteiYYYYMM().compareTo(targetMonth) >= 0 && s.getYoteiYYYYMM().compareTo(nextMonth) <= 0) {
                        settleRecordData = s;
                        Util.d("マッチング成功！settleRecordDate.getYoteiYYYYMM().getTime(): " + s.getYoteiYYYYMM().getTime());
                        Util.d("マッチング成功！settleRecordDate.getDisposablencome(): " + s.getDisposablencome());
                        break;
                    }
                    Util.d("マッチング失敗...settleRecordDate.getYoteiYYYYMM().getTime(): " + s.getYoteiYYYYMM().getTime());
                    Util.d("マッチング失敗...settleRecordDate.getDisposablencome(): " + s.getDisposablencome());
                }

                String title3 = "残り金額の調整";
                String contents3 = "今月の残り金額は "
                        + settleRecordData.getDisposablencome()
                        + "円です。";

                if (settleRecordData.getDisposablencome() > 0) {
                    // 残り金額が0より大きい場合、月別目標金額を調整するか、翌月に繰り越すか選択する。
                    contents3 += "\n"
                            + "\nよく頑張りましたね！"
                            + "\n残ったお金を調整します。実行する処理を選択してください。";
                    String buttonTitle1 = "月別目標調整";
                    String buttonTitle2 = "翌月繰り越し";
                    String buttonTitle3 = "キャンセル";
                    android.content.DialogInterface.OnClickListener click3_1 = doSime4_1(target, settleRecordData.getDisposablencome(), targetMonth, nextMonth);
                    android.content.DialogInterface.OnClickListener click3_2 = doSime4_2(target, settleRecordData.getDisposablencome());

                    Util.createAlertDialogWith3Buttons(AccountBookShowActivity.this,
                            title3, contents3, R.drawable.icon,
                            buttonTitle1, click3_1,
                            buttonTitle2, click3_2,
                            buttonTitle3, null);
                }
                else if (settleRecordData.getDisposablencome() == 0) {
                    // 残り金額がぴったり0の場合は調整不要。メッセージと確認ダイアログを表示する。
                    contents3 += "\n"
                            + "\nよく頑張りましたね！";
                    android.content.DialogInterface.OnClickListener click3_1 = doSime4_1(target, settleRecordData.getDisposablencome(), targetMonth, nextMonth);

                    Util.createAlertDialogWithOKandCancelButtons(AccountBookShowActivity.this,
                            title3, contents3, R.drawable.icon,
                            click3_1, null);
                }
                else {
                    // 残り金額がマイナスの場合、月別目標金額を減らして調整する。
                    contents3 += "\n"
                            + "\n残念ですが、今月は赤字になってしまいました。"
                            + "\n目標貯金額を調整してもよろしいですか？";
                    String buttonTitle1 = "月別目標調整";
                    String buttonTitle2 = "キャンセル";
                    android.content.DialogInterface.OnClickListener click3_1 = doSime4_1(target, settleRecordData.getDisposablencome(), targetMonth, nextMonth);

                    Util.createAlertDialogWith2Buttons(AccountBookShowActivity.this,
                            title3, contents3, R.drawable.icon,
                            buttonTitle1, click3_1,
                            buttonTitle2, null);
                }
            }

        };
    }

    /**
     * 月別目標金額の調整
     * 
     * @param target
     * 
     * @return
     */
    private android.content.DialogInterface.OnClickListener doSime4_1(final AccountBookDetail target, final Integer disposableIncome, final Calendar targetMonth, final Calendar nextMonth) {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // AccountBookDetailの設定値を修正して登録
                target.setMokuhyouMonthKingaku(target.getMokuhyouMonthKingaku() + disposableIncome);
                target.setAutoInputFlag(false);
                target.setSimeFlag(true);

                AccountBookShowController.submit(AccountBookShowActivity.this, target);

                // 締め対象月の変動費明細で、実績が未入力の場合は0に更新する。
                // UIスレッドとは非同期で実行する。
                class Param {
                }
                Param param = new Param();

                class Result {
                }

                AsyncTask<Param, Void, Result> task = new AsyncTask<Param, Void, Result>() {
                    @Override
                    protected Result doInBackground(Param... params) {

                        // 処理をしてonPostExecute()に渡すResult型オブジェクトに格納
                        Result result = new Result();

                        // IN句の設定
                        String in = new String();
                        Calendar tmp = (Calendar) targetMonth.clone();

                        while (tmp.compareTo(nextMonth) < 0) {
                            in += "'" + LPUtil.encodeCalendarToText(tmp) + "',";
                            tmp.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        in += "'" + LPUtil.encodeCalendarToText(nextMonth) + "'";
                        Util.d("IN句の内容：" + in);

                        DBHelper helper = new DBHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();
//                        Cursor c = db.rawQuery("select * from cost_detail "
//                                + "WHERE "
//                                 + CostDetailCol.SETTLE_COST
//                                 + " = '' AND "
//                                + CostDetailCol.BUDGET_YMD
//                                + " IN ("
//                                + in
//                                + ")", null);
//                        c.moveToFirst();
//                        CharSequence[][] list = new CharSequence[c.getCount()][7];
//                        Util.d("Select結果:" + list.length + "件");
//                        for (int i = 0; i < list.length; i++) {
//                            list[i][0] = c.getString(0);
//                            list[i][1] = c.getString(1);
//                            list[i][2] = c.getString(2);
//                            list[i][3] = c.getString(3);
//                            list[i][4] = c.getString(4);
//                            list[i][5] = c.getString(5);
//                            list[i][6] = c.getString(6);
//                            c.moveToNext();
//                            Util.d(i + "番目："
//                                    + list[i][0] + ", "
//                                    + list[i][1] + ", "
//                                    + list[i][2] + ", "
//                                    + list[i][3] + ", "
//                                    + list[i][4] + ", "
//                                    + list[i][5] + ", "
//                                    + list[i][6] + ", "
//                                    );
//                        }
//                        c.close();

                        db.execSQL("update cost_detail set "
                                + CostDetailCol.SETTLE_COST
                                + " = '0' WHERE "
                                + CostDetailCol.SETTLE_COST
                                + " = '' AND "
                                + CostDetailCol.BUDGET_YMD
                                + " IN ("
                                + in
                                + ")");

                        return result; // ここでreturnしたオブジェクトがonPostExecute()に渡される
                    }

                    @Override
                    protected void onPostExecute(Result result) {
                    }
                };
                task.execute(param); // パラメータを渡す }
            }
        };
    }

    /**
     * 翌月繰り越し
     * 
     * @param target
     * 
     * @return
     */
    private android.content.DialogInterface.OnClickListener doSime4_2(AccountBookDetail target, final Integer disposableIncome) {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
    }

    /**
     * 締め解除処理
     * 
     * @return
     */
    private OnClickListener undoSime() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                undoSime1();
            }
        };
    }

    private void undoSime1() {
        String title1 = "月末締め処理の解除";
        String contents1 = "月末締め処理を解除します。";
        android.content.DialogInterface.OnClickListener click1 = undoSime2();

        Util.createAlertDialogWithOKandCancelButtons(AccountBookShowActivity.this,
                title1, contents1, R.drawable.icon,
                click1, null);
    }

    private android.content.DialogInterface.OnClickListener undoSime2() {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountBookDetail target = null;

                Util.d("締め解除処理開始：");

                // 処理対象のAccountBookDetailを取得する
                if (accountBookDetails != null || accountBookDetails.isEmpty()) {

                    Util.d("accountBookDetailの読み込み開始：");

                    for (int i = 0; i < accountBookDetails.size(); i++) {
                        // 締めフラグが立っている家計簿明細があれば、その月を取得する。
                        if (accountBookDetails.get(i).getSimeFlag()) {
                            target = accountBookDetails.get(i);
                            Util.d("処理対象はコレ！：target.getMokuhyouMonth().getTime(): "
                                    + accountBookDetails.get(i).getMokuhyouMonth().getTime());
                            break;
                        }
                        Util.d("処理対象ではありませんでした。target.getMokuhyouMonth().getTime(): "
                                + accountBookDetails.get(i).getMokuhyouMonth().getTime());
                    }

                    if (target == null) {
                        // 処理対象が見つからない場合はメッセージを表示して処理終了する。
                        UIUtil.longToast(AccountBookShowActivity.this, "締め解除処理の対象が見つかりませんでした。");
                        return;
                    }
                }

                Calendar fromYMD = (Calendar) target.getMokuhyouMonth().clone();
                fromYMD.set(Calendar.DAY_OF_MONTH, accountBook.getStartDate().get(Calendar.DAY_OF_MONTH));

                Calendar toYMD = (Calendar) target.getMokuhyouMonth().clone();
                toYMD.set(Calendar.DAY_OF_MONTH, accountBook.getStartDate().get(Calendar.DAY_OF_MONTH));
                toYMD.add(Calendar.DAY_OF_MONTH, -1);

                if (target.getMokuhyouMonth().get(Calendar.DAY_OF_MONTH) >= accountBook.getStartDate().get(Calendar.DAY_OF_MONTH)) {
                    toYMD.add(Calendar.MONTH, 1);
                }
                else {
                    fromYMD.add(Calendar.MONTH, -1);
                }

                String title2 = "月末締め処理の解除";
                String contents2 = LPUtil.encodeCalendarToTextYMD(fromYMD)
                        + "～" + LPUtil.encodeCalendarToTextYMD(toYMD)
                        + "を対象に、月末締めを解除します。"
                        + "\n"
                        + "\n締め解除処理を実行してもよろしいですか？";
                android.content.DialogInterface.OnClickListener click2 = undoSime3(target);

                Util.createAlertDialogWithOKandCancelButtons(AccountBookShowActivity.this,
                        title2, contents2, R.drawable.icon,
                        click2, null);
            }

        };
    }

    private android.content.DialogInterface.OnClickListener undoSime3(final AccountBookDetail target) {
        return new android.content.DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                target.setSimeFlag(false);
                AccountBookShowController.submit(AccountBookShowActivity.this, target);

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