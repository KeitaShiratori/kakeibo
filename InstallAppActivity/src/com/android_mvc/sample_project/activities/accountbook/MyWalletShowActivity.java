package com.android_mvc.sample_project.activities.accountbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.accountbook.data.MyWalletShowData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.MyWalletController;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.dao.MyWalletDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class MyWalletShowActivity extends AccountBookAppUserBaseActivity {

    private MyWalletShowData myWalletShowData;
    private MyWallet inputDataMyWallet;
    private CostDetail inputDataCostDetail;

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
        final MyWalletShowActivity activity = this;

        // myWalletShowDataの初期化
        myWalletShowData = new MyWalletShowData();
        myWalletShowData.init(context);

        // モード判定
        if (ShowTabHostActivity.mode != null) {
            myWalletShowData.setMode(ShowTabHostActivity.mode);
        }
        if (ShowTabHostActivity.startDate != null) {
            myWalletShowData.setStartDate(ShowTabHostActivity.startDate);
        }
        Util.d("表示モード: " + myWalletShowData.getMode());
        Util.d("startDate: " + myWalletShowData.getStartDate());

        // 日、週、月のいずれかのモード
        String[] days = null;
        List<Calendar> YMD = new ArrayList<Calendar>();
        Calendar tmp = (Calendar) myWalletShowData.getStartDate().clone();
        int offset = 0;

        // モードにしたがい、DBの検索範囲を設定
        if (myWalletShowData.getMode().equals(s(R.string.DAY_MODE))) {
            days = new String[2];
        }
        else if (myWalletShowData.getMode().equals(s(R.string.WEEK_MODE))) {
            days = new String[8];
        }
        else if (myWalletShowData.getMode().equals(s(R.string.MONTH_MODE))) {
            days = new String[myWalletShowData.getStartDate().getActualMaximum(Calendar.DAY_OF_MONTH) + 1];
            tmp.set(Calendar.DAY_OF_MONTH, myWalletShowData.getStartDate().getActualMaximum(Calendar.DAY_OF_MONTH));
            offset = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - myWalletShowData.getStartDate().get(Calendar.DAY_OF_MONTH);
        }
        else {
            days = new String[2];
        }

        tmp.add(Calendar.DATE, 1 - days.length);
        for (int i = 0; i < days.length; i++) {
            YMD.add((Calendar) tmp.clone());
            days[i] = LPUtil.encodeCalendarToText(tmp);
            tmp.add(Calendar.DATE, 1);
        }
        tmp = null;

        // DBからロード
        myWalletShowData.setLabelYMD(YMD);
        myWalletShowData.setCostDetails(new CostDetailDAO(this).findWhereIn(CostDetailCol.BUDGET_YMD, days));
        myWalletShowData.setMyWallets(new MyWalletDAO(this).findWhereInValues(MyWalletCol.YMD, days));

        defineModeButtons();

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeader(myWalletShowData.getModeButtons())
                .add(
                        myWalletShowData.getLayout1()
                )
                .display();

        // 変動費明細レコードが取得できた場合、ヘッダ行を表示する
        if (!myWalletShowData.getLabelYMD().isEmpty()) {
            myWalletShowData.getLayout1().add(
                    makeLabel()
                    );
        }

        // 支出合計の計算
        Integer[] costSums = new Integer[days.length];
        long time2 = myWalletShowData.getStartDate().getTimeInMillis();

        for (CostDetail c : myWalletShowData.getCostDetails()) {
            // 支払方法がクレジットだった場合、加算処理を行わずに次のレコードを読み込む
            if (c.getPayType().equals(2)) {
                continue;
            }
            long time1 = c.getBudgetYmd().getTimeInMillis();
            int diff = (int) ((time2 - time1) / (1000 * 60 * 60 * 24));

            if (diff + offset < days.length) {
                if (costSums[days.length - diff - offset - 1] == null) {
                    costSums[days.length - diff - offset - 1] = 0;
                }
                costSums[days.length - diff - offset - 1] += c.getEffectiveSettleCost();
                Util.d("CostDetail: " + c.getEffectiveSettleCost() + "円, 日付: " + c.getBudgetYmd().getTime() + ", diff: " + diff);
            }
        }

        // 財布の中身の集計
        Integer[] walletKingaku = new Integer[days.length];
        Integer[] walletHikidasi = new Integer[days.length];

        for (MyWallet m : myWalletShowData.getMyWallets()) {
            long time1 = m.getYmd().getTimeInMillis();
            int diff = (int) ((time2 - time1) / (1000 * 60 * 60 * 24));

            if (diff < days.length) {
                // 残金の設定
                if (walletKingaku[days.length - diff - offset - 1] == null) {
                    walletKingaku[days.length - diff - offset - 1] = 0;
                }
                walletKingaku[days.length - diff - offset - 1] = m.getKingaku();
                Util.d("MyWallet: " + m.getKingaku() + "円, 日付: " + m.getYmd().getTime() + ", diff: " + diff);

                // 預金引き出しの設定
                if (walletHikidasi[days.length - diff - offset - 1] == null) {
                    walletHikidasi[days.length - diff - offset - 1] = 0;
                }
                walletHikidasi[days.length - diff - offset - 1] = m.getHikidashi();
                Util.d("預金引き出し: " + m.getHikidashi() + "円, 日付: " + m.getYmd().getTime() + ", diff: " + diff);
            }

        }
        // レイアウト内に動的に全財布の中身の情報を表示。
        for (int i = 1; i < days.length; i++) {

            // 差額チェック
            boolean sagakuCheckFlag = true;
            int tempWalletKingakuZenjitu = 0;
            int tempWalletHikidasi = 0;
            int tempWalletKingakuToujitu = 0;
            int tempCostSum = 0;

            if (walletKingaku[i - 1] != null) {
                tempWalletKingakuZenjitu = walletKingaku[i - 1];
            }
            if (walletHikidasi[i] != null) {
                tempWalletHikidasi = walletHikidasi[i];
            }
            if (walletKingaku[i] != null) {
                tempWalletKingakuToujitu = walletKingaku[i];
            }
            if (costSums[i] != null) {
                tempCostSum = costSums[i];
            }
            if (tempWalletKingakuZenjitu + tempWalletHikidasi - tempCostSum != tempWalletKingakuToujitu) {
                sagakuCheckFlag = false;
            }

            String tempHikidasi = s(R.string.MI_NYUURYOKU);
            if (walletHikidasi[i] != null) {
                tempHikidasi = walletHikidasi[i] + s(R.string.ENN);
            }

            String tempKingaku = s(R.string.MI_NYUURYOKU);
            if (walletKingaku[i] != null) {
                tempKingaku = walletKingaku[i] + s(R.string.ENN);
            }

            String tempSisyutuGoukei = s(R.string.MI_NYUURYOKU);
            if (costSums[i] != null) {
                tempSisyutuGoukei = costSums[i] + s(R.string.ENN);
            }

            // 自動入力ダイアログの出力値を設定
            final Calendar dialogYMD = (Calendar) YMD.get(i).clone();
            final int dialogWalletKingakuZenjitu = tempWalletKingakuZenjitu;
            final int dialogHikidasi = tempWalletHikidasi;
            final int dialogWalletKingakuToujitu = tempWalletKingakuToujitu;
            final int dialogCostSum = tempCostSum;

            // 描画処理
            myWalletShowData.getLayout1().add(

                    new MLinearLayout(context)
                            .orientationHorizontal()
                            .widthFillParent()
                            .heightWrapContent()
                            .click(sagakuCheckFlag ? null :
                                    new OnClickListener() {
                                        MyWalletShowActivity activity = MyWalletShowActivity.this;

                                        @Override
                                        public void onClick(View v) {
                                            Util.createAlertDialogWith3Buttons(
                                                    activity,
                                                    // title
                                                    "入力内容に誤りがある可能性があります。",
                                                    // content
                                                    (dialogYMD.get(Calendar.MONTH) + 1) + "/"
                                                            + dialogYMD.get(Calendar.DAY_OF_MONTH)
                                                            + "の入力値を補正します。"
                                                            + "実行する操作を選択してください。"
                                                            + "\n※通常、1,2,3の順に操作すると計算が合います。"
                                                            + "\n"
                                                            + "\n1.預金引出："
                                                            + "\n銀行から預金を引き出した場合に入力して下さい。"
                                                            + "\n"
                                                            + "\n2.残金修正："
                                                            + "\n残金の修正をします。"
                                                            + "\n"
                                                            + "\n3.支出修正："
                                                            + "\n支出合計の帳尻合わせをします。",
                                                    // icon
                                                    R.drawable.icon,
                                                    "1.預金引出", updateHikidasi(activity, dialogYMD, dialogWalletKingakuZenjitu, dialogHikidasi, dialogWalletKingakuToujitu, dialogCostSum),
                                                    "2.残金修正", update(activity, dialogYMD, dialogWalletKingakuZenjitu, dialogHikidasi, dialogWalletKingakuToujitu, dialogCostSum),
                                                    "3.支出修正", autoInputCostDetail(activity, dialogYMD, dialogWalletKingakuZenjitu, dialogHikidasi, dialogWalletKingakuToujitu, dialogCostSum));
                                        }

                                    }
                            )
                            .add(
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(sagakuCheckFlag ? R.drawable.record_design_h30_w60 : R.drawable.button_design_h30_w60)
                                            .text((YMD.get(i).get(Calendar.MONTH) + 1) + "/" + YMD.get(i).get(Calendar.DAY_OF_MONTH))
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                    ,
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(R.drawable.record_design_h30_w95)
                                            .text(tempHikidasi)
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                    ,
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(R.drawable.record_design_h30_w95)
                                            .text(tempKingaku)
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                    ,
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(R.drawable.record_design_h30_w95)
                                            .text(tempSisyutuGoukei)
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                            )
                    );

        }

        // レコード追加ボタン
        myWalletShowData.getLayout1().add(
                new MTextView(context)
                        .paddingPx(5)
                        .textsize(1)
                ,
                new MButton(context)
                        .drawableLeft(android.R.drawable.ic_input_add)
                        .backgroundResource(R.drawable.button_design_h30_w345)
                        .text(s(R.string.TUIKA))
                        .click(insert())
                );

        // 描画
        myWalletShowData.getLayout1().inflateInside();

    }

    private MLinearLayout makeLabel() {
        return new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(
                        new MTextView(context)
                                .text(s(R.string.NEN_GAPPI))
                                .backgroundResource(R.drawable.header_design_h30_w60)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text(s(R.string.HIKIDASHI))
                                .backgroundResource(R.drawable.header_design_h30_w95)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text(s(R.string.KINGAKU))
                                .backgroundResource(R.drawable.header_design_h30_w95)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text(s(R.string.SISYUTU_GOUKEI))
                                .backgroundResource(R.drawable.header_design_h30_w95)
                                .gravity(Gravity.CENTER_VERTICAL)
                );
    }

    private void defineModeButtons() {

        myWalletShowData.setBtnBefore(
                new MButton(context)
                        .backgroundDrawable(R.drawable.button_design)
                        .text("<<")
                        .click(submit(s(R.string.BEFORE))
                        )
                );

        myWalletShowData.setBtnDayMode(
                new MButton(context)
                        .backgroundResource(R.drawable.button_design)
                        .text("日")
                        .click(submit(s(R.string.DAY_MODE))
                        )
                );

        if (myWalletShowData.getMode().equals(s(R.string.DAY_MODE))) {
            myWalletShowData.getBtnDayMode().backgroundResource(R.drawable.button_design_pressed);
        }

        myWalletShowData.setBtnWeekMode(
                new MButton(context)
                        .backgroundResource(R.drawable.button_design)
                        .text("週")
                        .click(submit(s(R.string.WEEK_MODE))
                        )
                );

        if (myWalletShowData.getMode().equals(s(R.string.WEEK_MODE))) {
            myWalletShowData.getBtnWeekMode().backgroundResource(R.drawable.button_design_pressed);
        }

        myWalletShowData.setBtnMonthMode(
                new MButton(context)
                        .backgroundResource(R.drawable.button_design)
                        .text("月")
                        .click(submit(s(R.string.MONTH_MODE))
                        )
                );

        if (myWalletShowData.getMode().equals(s(R.string.MONTH_MODE))) {
            myWalletShowData.getBtnMonthMode().backgroundResource(R.drawable.button_design_pressed);
        }

        myWalletShowData.setBtnAfter(
                new MButton(context)
                        .backgroundResource(R.drawable.button_design)
                        .text(">>")
                        .click(submit(s(R.string.AFTER))
                        )
                );

        myWalletShowData.makeModeButtons();

    }

    private OnClickListener submit(final String modeBtn) {
        final MyWalletShowActivity activity = this;
        final Calendar newStartDate = (Calendar) myWalletShowData.getStartDate().clone();

        if (modeBtn.equals(s(R.string.BEFORE))) {
            if (myWalletShowData.getMode().equals(s(R.string.DAY_MODE))) {
                newStartDate.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (myWalletShowData.getMode().equals(s(R.string.WEEK_MODE))) {
                newStartDate.add(Calendar.DAY_OF_MONTH, -7);
            }
            else if (myWalletShowData.getMode().equals(s(R.string.MONTH_MODE))) {
                newStartDate.add(Calendar.MONTH, -1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyWalletController.submit(activity, myWalletShowData.getMode(), newStartDate);
                }
            };

        }

        else if (modeBtn.equals(s(R.string.AFTER))) {
            if (myWalletShowData.getMode().equals(s(R.string.DAY_MODE))) {
                newStartDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            else if (myWalletShowData.getMode().equals(s(R.string.WEEK_MODE))) {
                newStartDate.add(Calendar.DAY_OF_MONTH, 7);
            }
            else if (myWalletShowData.getMode().equals(s(R.string.MONTH_MODE))) {
                newStartDate.add(Calendar.MONTH, 1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyWalletController.submit(activity,
                            myWalletShowData.getMode(), newStartDate);
                }
            };
        }

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                MyWalletController.submit(activity, modeBtn,
                        myWalletShowData.getStartDate());
            }
        };
    }

    public DialogInterface.OnClickListener update(final MyWalletShowActivity activity, final Calendar ymd,
            final int zenjituKingaku, final int hikidasi, final int toujituKingaku, final int costSum) {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 自動補完する金額を計算
                Integer autoInput = zenjituKingaku + hikidasi - costSum;

                final MTextView tv1 = new MTextView(activity)
                        .text(s(R.string.KINGAKU))
                        .textColor(android.graphics.Color.WHITE);

                final MEditText et1 = new MEditText(activity)
                        .hint(s(R.string.KINGAKU))
                        .text(autoInput.toString());

                final MTextView tv2 = new MTextView(activity)
                        .text(s(R.string.ENN))
                        .textColor(android.graphics.Color.WHITE);

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationHorizontal()
                        .add(tv1, et1, tv2);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(s(R.string.MSG_00002))
                        .setView(ll1)
                        .setPositiveButton(s(R.string.KOUSIN), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                inputDataMyWallet = new MyWallet();
                                inputDataMyWallet.setYmd(ymd);
                                inputDataMyWallet.setKingaku(Integer.parseInt(et1.text()));
                                inputDataMyWallet.setZandaka(0);// Integer.parseInt(et2.text()));
                                inputDataMyWallet.setHikidashi(hikidasi);// Integer.parseInt(et3.text()));

                                MyWalletController.submit(activity);
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

    public DialogInterface.OnClickListener updateHikidasi(final MyWalletShowActivity activity, final Calendar ymd,
            final int zenjituKingaku, final int hikidasi, final int toujituKingaku, final int costSum) {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Integer autoInput = 0;
                // 自動補完する金額を計算
                if (hikidasi != 0) {
                    // 引出が渡された場合、その値を設定する。
                    autoInput = hikidasi;
                }
                else if (zenjituKingaku - toujituKingaku - costSum >= 0) {
                    // 支出額が小さく、当日残金が0を割らない場合
                    autoInput = 0;
                }
                else {
                    // 支出額が大きく、当日残金が0を割る場合
                    int temp = (toujituKingaku + costSum - zenjituKingaku) % 1000;
                    if (temp < 10) {
                        autoInput = (temp + 1) * 1000;
                    } else {
                        temp = temp % 10;
                        autoInput = (temp + 1) * 10000;
                    }
                }

                final MTextView tv1 = new MTextView(activity)
                        .text("預金引出")
                        .textColor(android.graphics.Color.WHITE);

                final MEditText et1 = new MEditText(activity)
                        .hint("預金引出")
                        .text(autoInput.toString());

                final MTextView tv2 = new MTextView(activity)
                        .text(s(R.string.ENN))
                        .textColor(android.graphics.Color.WHITE);

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationHorizontal()
                        .add(tv1, et1, tv2);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(s(R.string.MSG_00002))
                        .setView(ll1)
                        .setPositiveButton(s(R.string.KOUSIN), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    inputDataMyWallet = new MyWallet();
                                    inputDataMyWallet.setYmd(ymd);
                                    inputDataMyWallet.setKingaku(toujituKingaku);
                                    inputDataMyWallet.setZandaka(0);
                                    inputDataMyWallet.setHikidashi(Integer.parseInt(et1.text()));
                                }
                                catch (NumberFormatException e) {
                                    UIUtil.longToast(context, "数値を入力してください");
                                    return;
                                }

                                MyWalletController.submit(activity);
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

    private android.content.DialogInterface.OnClickListener autoInputCostDetail(final MyWalletShowActivity activity, final Calendar ymd,
            final int zenjituKingaku, final int hikidasi, final int toujituKingaku, final int costSum) {
        return new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 自動補完する金額を計算
                final Integer autoInput = zenjituKingaku + hikidasi - toujituKingaku - costSum;

                final MTextView tv1 = new MTextView(activity)
                        .text("前日残金：" + zenjituKingaku + "円"
                                + "\n本日残金：" + toujituKingaku + "円"
                                + "\n預金引出：" + hikidasi + "円"
                                + "\n入力済み支出：" + costSum + "円"
                                + "\n未入力金額：" + autoInput + "円"
                                + "\n"
                                + "\n帳尻合わせとして" + autoInput + "円を登録します。")
                        .textColor(android.graphics.Color.WHITE);

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationHorizontal()
                        .add(tv1);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("支出の帳尻合わせ")
                        .setView(ll1)
                        .setPositiveButton(s(R.string.KOUSIN), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                inputDataCostDetail = new CostDetail();
                                inputDataCostDetail.setBudgetYmd(ymd);
                                inputDataCostDetail.setBudgetCost(0);
                                inputDataCostDetail.setCategoryType(14);
                                inputDataCostDetail.setPayType(1);
                                inputDataCostDetail.setSettleCost(autoInput);

                                MyWalletController.autoInputCostDetail(activity);
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

    public OnClickListener insert() {
        final MyWalletShowActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MLinearLayout ll1 = new MLinearLayout(context)
                        .orientationHorizontal()
                        .widthWrapContent()
                        .heightWrapContent();
                final MLinearLayout ll2 = new MLinearLayout(context)
                        .orientationHorizontal()
                        .widthWrapContent()
                        .heightWrapContent();
                final MLinearLayout ll3 = new MLinearLayout(context)
                        .orientationHorizontal()
                        .widthWrapContent()
                        .heightWrapContent();

                final Calendar c = myWalletShowData.getStartDate();
                final MTextView tv1 = new MTextView(context)
                        .hint(s(R.string.NEN_GAPPI))
                        .text(c.get(Calendar.YEAR) + "/"
                                + (c.get(Calendar.MONTH) + 1) + "/"
                                + c.get(Calendar.DAY_OF_MONTH))
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundResource(R.drawable.button_design_1);
                tv1.click(Util.createDatePickerDialog(activity, tv1, c));
                final MEditText et1 = new MEditText(context)
                        .hint(s(R.string.KINGAKU))
                        .widthPx(300);
                final MEditText et2 = new MEditText(context)
                        .hint(s(R.string.ZANDAKA))
                        .widthPx(300);
                final MEditText et3 = new MEditText(context)
                        .hint(s(R.string.HIKIDASHI))
                        .widthPx(300)
                        .text("0");

                ll1.add(
                        new MTextView(context)
                                .text("日付")
                                .widthPx(150)
                                .textColor(android.graphics.Color.WHITE)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        tv1
                        );

                ll2.add(
                        new MTextView(context)
                                .text("残金")
                                .widthPx(150)
                                .textColor(android.graphics.Color.WHITE)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        et1
                        );

                ll3.add(
                        new MTextView(context)
                                .text("預金引出")
                                .widthPx(150)
                                .textColor(android.graphics.Color.WHITE)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        et3
                        );

                final MLinearLayout ll4 = new MLinearLayout(activity)
                        .orientationVertical()
                        .add(ll1, ll2, ll3);

                ll4.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(s(R.string.MSG_00001))
                        .setView(ll4)
                        .setPositiveButton(s(R.string.TUIKA), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                inputDataMyWallet = new MyWallet();
                                inputDataMyWallet.setYmd(LPUtil.decodeTextToCalendarYMD(tv1.text()));
                                inputDataMyWallet.setKingaku(Integer.parseInt(et1.text()));
                                inputDataMyWallet.setZandaka(0);// Integer.parseInt(et2.text()));
                                inputDataMyWallet.setHikidashi(Integer.parseInt(et3.text()));

                                MyWalletController.submit(activity);
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
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        ActivityParams ret = new ActivityParams();

        // 財布の中身の入力値がある場合
        if (inputDataMyWallet != null) {
            ret.add("年月日", MyWalletCol.YMD, inputDataMyWallet.getYmd())
                    .add("金額", MyWalletCol.KINGAKU, inputDataMyWallet.getKingaku())
                    .add("残高", MyWalletCol.ZANDAKA, inputDataMyWallet.getZandaka())
                    .add("引き出し", MyWalletCol.HIKIDASHI, inputDataMyWallet.getHikidashi());
        }

        // 変動費明細の入力値がある場合
        if (inputDataCostDetail != null) {
            ret.add("予定年月日", CostDetailCol.BUDGET_YMD, inputDataCostDetail.getBudgetYmd())
                    .add("予算費用", CostDetailCol.BUDGET_COST, inputDataCostDetail.getBudgetCost().toString())
                    .add("カテゴリ名", CostDetailCol.CATEGORY_TYPE, inputDataCostDetail.getCategoryType())
                    .add("実績費用", CostDetailCol.SETTLE_COST, inputDataCostDetail.getSettleCost().toString())
                    .add("支払方法", CostDetailCol.PAY_TYPE, inputDataCostDetail.getPayType())
                    .add("支払回数", CostDetailCol.DIVIDE_NUM, null)
                    .add("繰り返し区分", "repeat_dvn", "繰り返しなし");
        }
        return ret;
    }
}