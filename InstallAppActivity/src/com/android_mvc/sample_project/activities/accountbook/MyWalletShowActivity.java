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
    private MyWallet inputData;

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
        myWalletShowData.setMode(ShowTabHostActivity.mode);
        myWalletShowData.setStartDate(ShowTabHostActivity.startDate);

        Util.d("表示モード: " + myWalletShowData.getMode());
        Util.d("startDate: " + myWalletShowData.getStartDate());

        // 日、週、月のいずれかのモード
        String[] days = null;
        List<Calendar> YMD = new ArrayList<Calendar>();

        // モードにしたがい、DBの検索範囲を設定
        if (myWalletShowData.getMode().equals(s(R.string.DAY_MODE))) {
            days = new String[2];
        }
        else if (myWalletShowData.getMode().equals(s(R.string.WEEK_MODE))) {
            days = new String[8];
        }
        else if (myWalletShowData.getMode().equals(s(R.string.MONTH_MODE))) {
            days = new String[myWalletShowData.getStartDate().get(Calendar.DAY_OF_MONTH) + 1];
        }
        Calendar tmp = (Calendar) myWalletShowData.getStartDate().clone();
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
            long time1 = c.getBudgetYmd().getTimeInMillis();
            int diff = (int) ((time2 - time1) / (1000 * 60 * 60 * 24));

            if (diff < days.length) {
                if (costSums[days.length - diff - 1] == null) {
                    costSums[days.length - diff - 1] = 0;
                }
                costSums[days.length - diff - 1] += c.getEffectiveSettleCost();
                Util.d("CostDetail: " + c.getEffectiveSettleCost() + "円, 日付: " + c.getBudgetYmd().getTime() + ", diff: " + diff);
            }
        }

        // 財布の中身の集計
        Integer[] walletKingaku = new Integer[days.length];

        for (MyWallet m : myWalletShowData.getMyWallets()) {
            long time1 = m.getYmd().getTimeInMillis();
            int diff = (int) ((time2 - time1) / (1000 * 60 * 60 * 24));

            if (diff < days.length) {
                if (walletKingaku[days.length - diff - 1] == null) {
                    walletKingaku[days.length - diff - 1] = 0;
                }
                walletKingaku[days.length - diff - 1] = m.getKingaku();
                Util.d("MyWallet: " + m.getKingaku() + "円, 日付: " + m.getYmd().getTime() + ", diff: " + diff);
            }

        }
        // レイアウト内に動的に全財布の中身の情報を表示。
        for (int i = 1; i < days.length; i++) {

            // 差額チェック
            boolean sagakuCheckFlag = true;
            int tempWalletKingakuZenjitu = 0;
            int tempCostSum = 0;
            int tempWalletKingakuToujitu = 0;
            if (walletKingaku[i - 1] != null) {
                tempWalletKingakuZenjitu = walletKingaku[i - 1];
            }
            if (costSums[i] != null) {
                tempCostSum = costSums[i];
            }
            if (walletKingaku[i] != null) {
                tempWalletKingakuToujitu = walletKingaku[i];
            }
            if (tempWalletKingakuZenjitu - tempCostSum != tempWalletKingakuToujitu) {
                sagakuCheckFlag = false;
            }

            String tempKingaku = s(R.string.MI_NYUURYOKU);
            if (walletKingaku[i] != null) {
                tempKingaku = walletKingaku[i] + s(R.string.ENN);
            }

            String tempSisyutuGoukei = s(R.string.MI_NYUURYOKU);
            if (costSums[i] != null) {
                tempSisyutuGoukei = costSums[i] + s(R.string.ENN);
            }
            myWalletShowData.getLayout1().add(

                    new MLinearLayout(context)
                            .orientationHorizontal()
                            .widthFillParent()
                            .heightWrapContent()
                            .add(
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(R.drawable.record_design)
                                            .text((YMD.get(i).get(Calendar.MONTH) + 1) + "/" + YMD.get(i).get(Calendar.DAY_OF_MONTH))
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                    ,
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(sagakuCheckFlag ? R.drawable.record_design : R.drawable.button_design_1)
                                            .text(tempKingaku)
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                            .click(sagakuCheckFlag ? null : update(activity, YMD.get(i), tempWalletKingakuZenjitu, tempWalletKingakuToujitu, tempCostSum))
                                    ,
                                    new MTextView(context)
                                            .gravity(Gravity.CENTER_VERTICAL)
                                            .backgroundResource(R.drawable.record_design)
                                            .text(tempSisyutuGoukei)
                                            .textColor(sagakuCheckFlag ? android.graphics.Color.BLACK : android.graphics.Color.RED)
                                            .click(sagakuCheckFlag ? null : submitToCostShow(YMD.get(i)))
                            )
                    );

        }

        // レコード追加ボタン
        myWalletShowData.getLayout1().add(
                new MButton(context)
                        .widthFillParent()
                        .backgroundResource(android.R.drawable.ic_input_add)
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
                                .backgroundResource(R.drawable.header_design)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text(s(R.string.KINGAKU))
                                .backgroundResource(R.drawable.header_design)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text(s(R.string.SISYUTU_GOUKEI))
                                .backgroundResource(R.drawable.header_design)
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

    // 指定した日付の変動費照会画面へ遷移する。
    private OnClickListener submitToCostShow(final Calendar c) {
        final MyWalletShowActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                MyWalletController.submitToCostShow(activity, s(R.string.DAY_MODE), c);

            }
        };
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

    public OnClickListener delete(final MyWalletShowActivity activity, final CostDetail c) {

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("この明細を削除してもよろしいですか？")
                        .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    // DBから削除へ
                                    // MyWalletController.submit(activity,
                                    // "DELETE_COST_DETAIL", c.getId());
                                } catch (Exception e) {
                                    UIUtil.longToast(context, "削除に失敗しました。");
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

    public OnClickListener update(final MyWalletShowActivity activity, final Calendar ymd,
            final int zenjituKingaku, final int toujituKingaku, final int costSum) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {

                // 自動保管する金額を計算
                Integer autoInput = zenjituKingaku - costSum;

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
                                inputData = new MyWallet();
                                inputData.setYmd(ymd);
                                inputData.setKingaku(Integer.parseInt(et1.text()));
                                inputData.setZandaka(0);// Integer.parseInt(et2.text()));
                                inputData.setHikidashi(0);// Integer.parseInt(et3.text()));

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

    public OnClickListener insert() {
        final MyWalletShowActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = myWalletShowData.getStartDate();
                final MTextView tv1 = new MTextView(context)
                        .hint(s(R.string.NEN_GAPPI))
                        .text(c.get(Calendar.YEAR) + "/"
                                + (c.get(Calendar.MONTH) + 1) + "/"
                                + c.get(Calendar.DAY_OF_MONTH))
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundResource(R.drawable.button_design_1);
                tv1.click(createDatePickerDialog(tv1, c));
                final MEditText et1 = new MEditText(context)
                        .hint(s(R.string.KINGAKU))
                        .widthPx(300);
                final MEditText et2 = new MEditText(context)
                        .hint(s(R.string.ZANDAKA))
                        .widthPx(300);
                final MEditText et3 = new MEditText(context)
                        .hint(s(R.string.HIKIDASHI))
                        .widthPx(300);

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .add(tv1, et1);// , et2, et3);

                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(s(R.string.MSG_00001))
                        .setView(ll1)
                        .setPositiveButton(s(R.string.TUIKA), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                inputData = new MyWallet();
                                inputData.setYmd(LPUtil.decodeTextToCalendarYMD(tv1.text()));
                                inputData.setKingaku(Integer.parseInt(et1.text()));
                                inputData.setZandaka(0);// Integer.parseInt(et2.text()));
                                inputData.setHikidashi(0);// Integer.parseInt(et3.text()));

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

    /**
     * MTextViewをクリックしたときにDatePickerDialogを開くリスナーを返す。
     * 
     * @param tv
     * @param c
     * @return
     */
    private OnClickListener createDatePickerDialog(final MTextView tv, final Calendar c) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog dpd1 = new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv.text(
                                        String.valueOf(year) + "/" +
                                                String.valueOf(monthOfYear + 1) + "/" +
                                                String.valueOf(dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd1.show();
            }
        };
    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        return new ActivityParams()
                .add("年月日", MyWalletCol.YMD, inputData.getYmd())
                .add("金額", MyWalletCol.KINGAKU, inputData.getKingaku())
                .add("残高", MyWalletCol.ZANDAKA, inputData.getZandaka())
                .add("引き出し", MyWalletCol.HIKIDASHI, inputData.getHikidashi());
    }

}