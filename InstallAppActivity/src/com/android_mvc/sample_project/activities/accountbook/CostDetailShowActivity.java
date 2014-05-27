package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MCalculatorView;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MGridLayout;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.CostDetailController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.dao.CreditCardSettingDAO;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CostDetailShowActivity extends AccountBookAppUserBaseActivity {

    private MLinearLayout layout1;
    private MTextView tv1;
    private MTextView tv2;
    private Calendar LabelYMD;
    private String mode;
    private Calendar startDate;
    private MLinearLayout modeButtons;
    private MButton btnDayMode;
    private MButton btnWeekMode;
    private MButton btnMonthMode;
    private MButton btnAfter;
    private MButton btnBefore;

    // DAO
    private CostDetailDAO costDetailDAO;
    private CreditCardSettingDAO creditCardSettingDAO;
    private AccountBookDAO accountBookDAO;
    private AccountBookDetailDAO accountBookDetailDAO;

    // 制御用変数
    private boolean isCreditSiharai;
    private CreditCardSetting creditCardSetting;
    private Calendar simeLastDay;

    // モード定義
    public static String NEW_RECORD_MODE = "NEW_RECORD_MODE";
    public static String DAY_MODE = "DAY_MODE";
    public static String WEEK_MODE = "WEEK_MODE";
    public static String MONTH_MODE = "MONTH_MODE";
    public static String ALL_MODE = "ALL_MODE";
    // 前後
    public static String BEFORE = "BEFORE";
    public static String AFTER = "AFTER";

    // 変動費明細のリスト
    List<CostDetail> CostDetails;

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
        // 初期処理
        final CostDetailShowActivity activity = this;

        costDetailDAO = new CostDetailDAO(this);
        creditCardSettingDAO = new CreditCardSettingDAO(this);
        accountBookDAO = new AccountBookDAO(this);
        accountBookDetailDAO = new AccountBookDetailDAO(this);

        creditCardSetting = creditCardSettingDAO.findNewestOne();
        isCreditSiharai = false;

        List<AccountBookDetail> accountBookDetails = accountBookDetailDAO.findAll();

        for (int i = accountBookDetails.size() -1 ; i >= 0; i--) {
            if (accountBookDetails.get(i).getSimeFlag()) {
                simeLastDay = accountBookDetails.get(i).getMokuhyouMonth();
            }
            break;
        }
        if (simeLastDay != null) {
            simeLastDay.add(Calendar.MONTH, 1);
            simeLastDay.set(Calendar.DAY_OF_MONTH, accountBookDAO.findAll().get(0).getStartDate().get(Calendar.DAY_OF_MONTH));
        }

        layout1 = new MLinearLayout(context)
                .orientationVertical()
                .widthMatchParent()
                .paddingLeftPx(10)
                .heightWrapContent();

        // モード判定
        mode = ShowTabHostActivity.mode;
        startDate = ShowTabHostActivity.startDate;

        // 変動費明細が１件もない状態で収入登録された後、startDateとmodeがnullになる。nullだと画面表示時にエラー発生する。
        if (startDate == null) {
            startDate = Calendar.getInstance();
        }
        if (mode == null) {
            mode = "DEFAULT";
        }

        Util.d("表示モード: " + mode);
        Util.d("startDate: " + startDate);

        defineModeButtons();

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeader(modeButtons)
                .add(
                        layout1
                )
                .display();

        // 新規登録直後
        if (mode.equals(NEW_RECORD_MODE)) {
            CostDetails = ShowTabHostActivity.costDetails;
            ShowTabHostActivity.costDetails = null;
            tv1 = new MTextView(activity)
                    .text("支出が登録されました")
                    .textColor(R.color.red);
            layout1.add(tv1);

            String[] days = null;
            days = new String[1];
            days[0] = LPUtil.encodeCalendarToText(startDate);

        }
        // 全件モード
        else if (mode.equals(ALL_MODE)) {
            CostDetails = costDetailDAO.findOrderByDesc(CostDetailCol.BUDGET_YMD);
        }
        // 日、週、月のいずれかのモード
        else if (mode.equals(DAY_MODE) || mode.equals(WEEK_MODE) || mode.equals(MONTH_MODE)) {
            String[] days = null;

            // モードにしたがい、DBの検索範囲を設定
            if (mode.equals(DAY_MODE)) {
                days = new String[1];
                days[0] = LPUtil.encodeCalendarToText(startDate);
                if (creditCardSetting != null
                        && startDate.get(Calendar.DAY_OF_MONTH)
                            == creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH)) {
                    isCreditSiharai = true;
                }
            }
            else if (mode.equals(WEEK_MODE)) {
                days = new String[7];
                days[0] = LPUtil.encodeCalendarToText(startDate);
                Calendar tmp = (Calendar) startDate.clone();
                for (int i = 1; i < days.length; i++) {
                    if (creditCardSetting != null
                            && tmp.get(Calendar.DAY_OF_MONTH)
                                == creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH)) {
                        isCreditSiharai = true;
                    }
                    tmp.add(Calendar.DATE, 1);
                    days[i] = LPUtil.encodeCalendarToText(tmp);
                }
                tmp = null;
            }
            else if (mode.equals(MONTH_MODE)) {
                Calendar tmp = (Calendar) startDate.clone();
                tmp.set(Calendar.DAY_OF_MONTH, 1);
                days = new String[startDate.getActualMaximum(Calendar.DAY_OF_MONTH)];
                days[0] = LPUtil.encodeCalendarToText(tmp);
                for (int i = 1; i < days.length; i++) {
                    if (creditCardSetting != null
                            && tmp.get(Calendar.DAY_OF_MONTH)
                                == creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH)) {
                        isCreditSiharai = true;
                    }
                    tmp.add(Calendar.DATE, 1);
                    days[i] = LPUtil.encodeCalendarToText(tmp);
                }
                tmp = null;
            }

            try {
                // 全CostDetailをDBからロード
                CostDetails = costDetailDAO.findWhereIn(CostDetailCol.BUDGET_YMD, days);

                // クレジット支払フラグが立っていたら、クレジットを集計する。
                if (isCreditSiharai) {
                    List<CostDetail> creditCostDetails = costDetailDAO.findWhereIn(CostDetailCol.PAY_TYPE, "2");

                    // クレジット締日を設定
                    Calendar fromCreditSimeYMD = (Calendar) startDate.clone();
                    Calendar toCreditSimeYMD = (Calendar) startDate.clone();

                    fromCreditSimeYMD.add(Calendar.MONTH, -1);
                    fromCreditSimeYMD.set(Calendar.DAY_OF_MONTH, creditCardSetting.getSimeYmd().get(Calendar.DAY_OF_MONTH));
                    fromCreditSimeYMD.add(Calendar.DAY_OF_MONTH, 1);
                    toCreditSimeYMD.set(Calendar.DAY_OF_MONTH, creditCardSetting.getSimeYmd().get(Calendar.DAY_OF_MONTH));

                    // クレジット引き去り用明細に設定する値
                    CostDetail creditCostDetail = new CostDetail();
                    int creditBudgetCostSum = 0;
                    int creditSettleCostSum = 0;
                    Calendar creditBudgetYMD = (Calendar) startDate.clone();
                    creditBudgetYMD.set(Calendar.DAY_OF_MONTH, creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH));

                    for (CostDetail c : creditCostDetails) {
                        for (int divide = 0; divide < c.getDivideNum(); divide++) {
                            if (c.getBudgetYmd().after(fromCreditSimeYMD) && c.getBudgetYmd().before(toCreditSimeYMD)) {
                                creditBudgetCostSum += c.getBudgetCost() / c.getDivideNum();
                                creditSettleCostSum += c.getSettleCost() / c.getDivideNum();
                                break;
                            }
                            c.getBudgetYmd().add(Calendar.MONTH, 1);
                        }
                    }

                    creditCostDetail.setBudgetCost(creditBudgetCostSum);
                    creditCostDetail.setSettleCost(creditSettleCostSum);
                    creditCostDetail.setBudgetYmd(creditBudgetYMD);
                    creditCostDetail.setCategoryType(14);
                    creditCostDetail.setPayType(1);

                    // クレジット引き去り用明細を表示
                    layout1.add(
                            creditLabel(creditCostDetail)
                            );
                }
                // 変動費明細レコードが取得できなかった場合、処理終了
                if (CostDetails.isEmpty()) {
                    Util.d("CostDetail and creditCostDetails are Empty!");
                    layout1.add(
                            emptyLabel(days)
                            );
                    layout1.inflateInside();
                    return;
                }
            } catch (NullPointerException e) {
                // 変動費明細レコードが取得できなかった場合、処理終了
                Util.d("CostDetailDAO throws NullPointerException!");
                layout1.add(
                        emptyLabel(days)
                        );
                layout1.inflateInside();
                return;
            }

            // 週モードか月モードの場合、合計ラベルを表示する。
            if (mode.equals(s(R.string.WEEK_MODE))
                    || mode.equals(s(R.string.MONTH_MODE))) {
                layout1.add(
                        samaryLabgel(days, CostDetails)
                        );
            }
        }
        else if (mode.equals("DEFAULT")) {
            return;
        }

        // レイアウト内に動的に全変動費明細の情報を表示。
        for (int i = 0; i < CostDetails.size(); i++) {
            CostDetail c = CostDetails.get(i);

            // 予定日が変わったら、空白行を挟んで、新しいヘッダ行を追加する。LabelYMDを更新する。
            if (LabelYMD == null
                    || !LabelYMD.equals(c.getBudgetYmd())) {
                layout1.add(
                        new MTextView(context)
                                .paddingPx(1)
                                .textsize(5)
                        ,
                        getLabelYMD(c.getBudgetYmd(), CostDetails)
                        );
                LabelYMD = c.getBudgetYmd();
            }

            MGridLayout record = new MGridLayout(activity)
                    .columnCount(3)
                    .rowCount(3)
                    .heightWrapContent()
                    .backgroundDrawable(R.drawable.border);
            MTextView categoryTypeView = c.getCategoryTypeView(this);
            MTextView payTypeVeiw = c.getPayTypeView(this);
            MTextView budgetCostView = c.getBudgetCostView(this);
            MTextView settleCostView = c.getSettleCostView(this);

            MTextView updateButton = new MTextView(activity)
                    .gravity(Gravity.CENTER_VERTICAL)
                    .text(c.isKurikosi() || isSime(c.getBudgetYmd()) ? "変更不可" : "変更")
                    .backgroundDrawable(c.isKurikosi() || isSime(c.getBudgetYmd()) ? R.drawable.record_design : R.drawable.button_design_1)
                    .click(c.isKurikosi() || isSime(c.getBudgetYmd()) ? null : inputDialogUpdateSettleCost(c, settleCostView));

            MTextView deleteButton = new MTextView(activity)
                    .gravity(Gravity.CENTER_VERTICAL)
                    .text(c.isKurikosi() || isSime(c.getBudgetYmd()) ? "削除不可" : "削除")
                    .backgroundDrawable(c.isKurikosi() || isSime(c.getBudgetYmd()) ? R.drawable.record_design : R.drawable.button_design_1)
                    .click(c.isKurikosi() || isSime(c.getBudgetYmd()) ? null : delete(this, c));

            record.add(
                    categoryTypeView,
                    budgetCostView,
                    updateButton,
                    payTypeVeiw,
                    settleCostView,
                    deleteButton
                    );

            // クレジットカードの分割回数行の作成処理
            if (isCreditSiharai && c.getDivideNum() != null) {
                String divideNum = (c.getDivideNum() == null) ? "" : " " + c.getDivideNum() + "回払い";
                String payTerm = c.getPayTerm(creditCardSetting);

                // クレジット支払いフラグが立っていて、変動費明細に分割数が設定されている場合、Viewを作成する。
                MTextView divideNumView = new MTextView(activity)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .text(divideNum)
                        .backgroundDrawable(drawable.record_design)
                        .widthWrapContent();

                MTextView divideKingakuView = new MTextView(activity)
                        .text("支払額: " + (c.getEffectiveCost() / c.getDivideNum()) + "円")
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundDrawable(drawable.record_design)
                        .widthWrapContent();

                MTextView payTermView = new MTextView(activity)
                        .text("引落月: " + payTerm)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundDrawable(drawable.record_design)
                        .widthWrapContent();

                record.add(
                        divideNumView,
                        divideKingakuView,
                        payTermView
                        );
            }

            layout1.add(
                    record
                    );

        }

        // 描画
        layout1.inflateInside();
        if (mode.equals(s(R.string.NEW_RECORD_MODE))) {
            mode = s(R.string.DAY_MODE);
        }
    }

    private boolean isSime(Calendar target) {
        if (simeLastDay != null) {
            return simeLastDay.after(target);
        }
        return false;
    }

    private MLinearLayout creditLabel(CostDetail credit) {
        String title = "クレジット引落\n" + LPUtil.encodeCalendarToTextYMD(credit.getBudgetYmd());

        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .paddingBottomPx(10)
                .heightWrapContent()
                .add(
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text(title)
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定合計"
                                        + "\n"
                                        + credit.getBudgetCost() + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績合計"
                                        + "\n"
                                        + credit.getSettleCost() + "円")
                                .backgroundDrawable(R.drawable.header_design)

                );

    }

    private MLinearLayout emptyLabel(String[] days) {
        String YMD = new String();
        String fromYMD = days[0].substring(0, 10);

        YMD += fromYMD + "\n";

        if (days.length > 1) {
            String toYMD = days[days.length - 1].substring(0, 10);
            YMD += "～" + toYMD;
        }

        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text(YMD)
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定合計"
                                        + "\n"
                                        + 0 + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績合計"
                                        + "\n"
                                        + 0 + "円")
                                .backgroundDrawable(R.drawable.header_design)

                );

    }

    private MLinearLayout samaryLabgel(String[] days, List<CostDetail> selectedCostDetails) {
        String YMD = new String();

        String fromYMD = days[0].substring(0, 10);
        YMD += fromYMD + "\n";

        if (days.length > 1) {
            String toYMD = days[days.length - 1].substring(0, 10);
            YMD += "～" + toYMD;
        }

        Integer budgetCostSum = 0;
        Integer settleCostSum = 0;

        for (CostDetail c : selectedCostDetails) {
            if (!c.getPayType().equals(2)) {
                budgetCostSum += c.getBudgetCost();
                settleCostSum += c.getEffectiveSettleCost();
            }
        }

        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .paddingBottomPx(10)
                .add(

                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text(YMD)
                                .backgroundDrawable(R.drawable.header_design)
                                .click(homeru(budgetCostSum, settleCostSum, LPUtil.decodeTextToCalendarYMD(days[days.length - 1])))
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定合計"
                                        + "\n"
                                        + budgetCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績合計"
                                        + "\n"
                                        + settleCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)

                );
    }

    private void defineModeButtons() {

        btnBefore = new MButton(context)
                .backgroundDrawable(R.drawable.button_design)
                .text("<<")
                .click(submit(BEFORE));

        btnDayMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("日")
                .click(submit(DAY_MODE));
        if (mode.equals(DAY_MODE)) {
            btnDayMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnWeekMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("週")
                .click(submit(WEEK_MODE));
        if (mode.equals(WEEK_MODE)) {
            btnWeekMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnMonthMode = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text("月")
                .click(submit(MONTH_MODE));
        if (mode.equals(MONTH_MODE)) {
            btnMonthMode.backgroundResource(R.drawable.button_design_pressed);
        }

        btnAfter = new MButton(context)
                .backgroundResource(R.drawable.button_design)
                .text(">>")
                .click(submit(AFTER));

        modeButtons = new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .paddingPx(10)
                .add(
                        btnBefore
                        ,
                        btnDayMode
                        ,
                        btnWeekMode
                        ,
                        btnMonthMode
                        ,
                        btnAfter
                );

    }

    private OnClickListener submit(final String modeBtn) {
        final CostDetailShowActivity activity = this;
        final Calendar offset = (Calendar) startDate.clone();

        if (modeBtn.equals(BEFORE)) {
            if (mode.equals(DAY_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (mode.equals(WEEK_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, -7);
            }
            else if (mode.equals(MONTH_MODE)) {
                offset.add(Calendar.MONTH, -1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CostDetailController.submit(activity, mode, offset);
                }
            };

        }
        else if (modeBtn.equals(AFTER)) {
            if (mode.equals(DAY_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, 1);
            }
            else if (mode.equals(WEEK_MODE)) {
                offset.add(Calendar.DAY_OF_MONTH, 7);
            }
            else if (mode.equals(MONTH_MODE)) {
                offset.add(Calendar.MONTH, 1);
            }
            return new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CostDetailController.submit(activity, mode, offset);
                }
            };
        }

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                CostDetailController.submit(activity, modeBtn, startDate);
            }
        };
    }

    public LinearLayout getLabelYMD(Calendar budgetYmd, List<CostDetail> costDetails) {

        Integer budgetCostSum = 0;
        Integer settleCostSum = 0;
        String KEI = mode.equals(s(R.string.DAY_MODE)) ? "合計" : "小計";

        for (CostDetail c : costDetails) {
            if (!c.getPayType().equals(2)
                    && c.getBudgetYmd().equals(budgetYmd)) {
                budgetCostSum += c.getBudgetCost();
                settleCostSum += c.getEffectiveSettleCost();
            }
        }
        // 使用予定日のラベルを１個追加
        return new MLinearLayout(this)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(

                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text(budgetYmd.get(Calendar.YEAR) + "/"
                                        + (budgetYmd.get(Calendar.MONTH) + 1) + "/"
                                        + budgetYmd.get(Calendar.DAY_OF_MONTH) + "\n")
                                .backgroundDrawable(R.drawable.header_design)
                                .click(homeru(budgetCostSum, settleCostSum, budgetYmd))
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("予定" + KEI + "\n" + budgetCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)
                        ,
                        new MTextView(this)
                                .gravity(Gravity.CENTER_VERTICAL)
                                .text("実績" + KEI + "\n" + settleCostSum + "円")
                                .backgroundDrawable(R.drawable.header_design)

                );
    }

    private OnClickListener homeru(final Integer budgetCostSum, final Integer settleCostSum, final Calendar targetYMD) {
        final CostDetailShowActivity activity = this;
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                targetYMD.add(Calendar.DAY_OF_MONTH, 1);
                if (targetYMD.before(Calendar.getInstance())) {
                    if (budgetCostSum >= settleCostSum) {
                        UIUtil.longToast(activity, "おめでとう！予定金額よりも節約できましたね！");
                    }
                    else {
                        UIUtil.longToast(activity, "残念。予定金額より使いすぎてしまいましたね。");
                    }
                }
                else {
                    if (budgetCostSum >= settleCostSum) {
                        UIUtil.longToast(activity, "予定金額を超えないように気を付けてね♪");
                    }
                    else {
                        UIUtil.longToast(activity, "残念。予定金額より使いすぎてしまいましたね。");
                    }
                }
            }
        };
    }

    public OnClickListener delete(final CostDetailShowActivity activity, final CostDetail c) {

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
                                    CostDetailController.submit(activity, "DELETE_COST_DETAIL", c.getId());
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

    public OnClickListener update(final CostDetailShowActivity activity, final CostDetail c) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                final MEditText et1 = new MEditText(activity)
                        .hint("実績金額")
                        .widthMatchParent();

                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                final MLinearLayout ll1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .add(et1);
                ll1.inflateInside();

                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("実際に使用した金額を入力してください。")
                        .setView(ll1)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Integer temp1 = Integer.parseInt(et1.text());
                                    c.setSettleCost(temp1);

                                    // DB更新へ
                                    CostDetailController.submit(activity, "UPDATE_COST_DETAIL", c);
                                } catch (Exception e) {
                                    UIUtil.longToast(context, "更新に失敗しました。\n実績金額を正しく入力して下さい。");
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

    // 月別目標金額を更新するためのイベント
    private OnClickListener inputDialogUpdateSettleCost(final CostDetail c, final MTextView target) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = "実績金額を入力してください。";
                // 最終目標金額入力用View
                Integer initVal = 0;
                if (target != null && target.text() != null && !target.text().isEmpty()) {
                    try {
                        String tmp = target.text().replace("円", "");
                        tmp = tmp.replace("実績: ", "");
                        tmp = tmp.replace("未入力", "");
                        if (!tmp.isEmpty()) {
                            initVal = Integer.parseInt(tmp);
                        }
                    } catch (NumberFormatException e) {
                        UIUtil.longToast(context, "数値を入力してください");
                        return;
                    }
                }

                createCalculaterDialogWith2Button(CostDetailShowActivity.this, title, null, 0,
                        target, initVal, "円", c);
            }
        };
    }

    /**
     * 2つのボタンを持つ電卓ダイアログを表示する。 各ボタンを押した時のイベントはclickに渡す。
     * 
     * @param activity
     * @param title
     * @param content
     * @param icon
     * @param click
     * @return
     */
    public static Builder createCalculaterDialogWith2Button(final CostDetailShowActivity activity, String title, String content, int icon,
            final TextView v, Integer initVal, final String valUnit, final CostDetail c) {

        AlertDialog.Builder ret = new AlertDialog.Builder(activity);

        final MCalculatorView calc = new MCalculatorView(activity, null, initVal);

        // ダイアログの設定
        ret.setTitle(title); // タイトル
        ret.setMessage(content); // 内容
        ret.setIcon(icon); // アイコン設定
        ret.setView(calc);

        ret.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                v.setText("実績： " + calc.getValue() + valUnit);
                c.setSettleCost(Integer.parseInt((String) calc.getValue()));
                // DB更新へ
                CostDetailController.submit(activity, "UPDATE_COST_DETAIL", c);
            }
        });

        ret.setNegativeButton("キャンセル", null);

        calc.inflateInside();
        ret.create();
        ret.show();

        return ret;
    }

}