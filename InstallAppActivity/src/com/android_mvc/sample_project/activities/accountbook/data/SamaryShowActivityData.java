package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.view.Gravity;
import android.widget.Spinner;

import com.android_mvc.framework.ui.view.MGridLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.dao.CreditCardSettingDAO;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.dao.PayTypeDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.db.entity.PayType;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

// SettleShowActivityクラスで使用するデータを保持するクラス
public final class SamaryShowActivityData {
    private static SamaryShowActivityData instance;

    // メンバ変数
    private AccountBookAppUserBaseActivity activity;
    private AccountBook accountBook;
    private List<AccountBookDetail> accountBookDetails;
    private CreditCardSetting creditCardSetting;
    private List<CostDetail> costDetails;
    private List<IncomeDetail> incomeDetails;

    // DAO
    private AccountBookDAO accountBookDAO;
    private AccountBookDetailDAO accountBookDetailDAO;
    private CreditCardSettingDAO creditCardSettingDAO;
    private CostDetailDAO costDetailDAO;
    private IncomeDetailDAO incomeDetailDAO;

    // 処理フラグ
    private boolean isCreditCardSetting;

    // 初期処理
    public static SamaryShowActivityData getInstance(AccountBookAppUserBaseActivity activity) {
        if (instance == null) {
            // 初期処理
            instance = new SamaryShowActivityData(activity);
        }
        return instance;
    }

    private SamaryShowActivityData(AccountBookAppUserBaseActivity activity) {
        super();

        // 親Activityの初期化
        this.activity = activity;

        // DAOの初期化
        accountBookDAO = new AccountBookDAO(activity);
        accountBookDetailDAO = new AccountBookDetailDAO(activity);
        creditCardSettingDAO = new CreditCardSettingDAO(activity);
        costDetailDAO = new CostDetailDAO(activity);
        incomeDetailDAO = new IncomeDetailDAO(activity);

        // メンバ変数の初期化
        accountBook = accountBookDAO.findAll().get(0);
        accountBookDetails = accountBookDetailDAO.findOrderByKeyAsc(AccountBookDetailCol.MOKUHYOU_MONTH);
        creditCardSetting = creditCardSettingDAO.findNewestOne();
        if (creditCardSetting != null) {
            setCreditCardSetting(true);
        }
        costDetails = costDetailDAO.findOrderByAsc("substr(" + CostDetailCol.BUDGET_YMD + ",1,7)", CostDetailCol.CATEGORY_TYPE);

        Util.d("CostDetailのYMD" + LPUtil.encodeCalendarToText(costDetails.get(0).getBudgetYmd()));

        incomeDetails = incomeDetailDAO.findOrderByAscBudgetYmd();

    }

    public AccountBook getAccountBook() {
        return accountBook;
    }

    public void setAccountBook(AccountBook accountBook) {
        this.accountBook = accountBook;
    }

    public List<AccountBookDetail> getAccountBookDetails() {
        return accountBookDetails;
    }

    public void setAccountBookDetails(List<AccountBookDetail> accountBookDetails) {
        this.accountBookDetails = accountBookDetails;
    }

    public List<CostDetail> getCostDetails() {
        return costDetails;
    }

    public void setCostDetails(List<CostDetail> costDetails) {
        this.costDetails = costDetails;
    }

    public List<IncomeDetail> getIncomeDetails() {
        return incomeDetails;
    }

    public void setIncomeDetails(List<IncomeDetail> incomeDetails) {
        this.incomeDetails = incomeDetails;
    }

    public boolean isCreditCardSetting() {
        return isCreditCardSetting;
    }

    public void setCreditCardSetting(boolean isCreditCardSetting) {
        this.isCreditCardSetting = isCreditCardSetting;
    }

    public Spinner getYmdSpinner() {
        return null;
    }

    public MGridLayout getLayout() {
        // Viewの初期化
        MGridLayout categoryBetuCostLayout = new MGridLayout(activity);

        // カテゴリ別支出の初期化
        categoryBetuCostLayout.columnCount(3);

        Iterator<CostDetail> costDetailsIterator = costDetails.iterator();

        // Integer[3] = {年、月、カテゴリ分類コード}
        Integer[] newKey = new Integer[3];
        Integer[] oldKey = new Integer[3];

        CostDetail c = costDetailsIterator.next();
        setKey(newKey, c);

        while (costDetailsIterator.hasNext()) {
            // ヘッダを出力する必要がある場合、ヘッダを出力する。
            if (oldKey[0] == null || newKey[0] - oldKey[0] != 0 || newKey[1] - oldKey[1] != 0) {
                makeHeader(categoryBetuCostLayout, c);
            }

            oldKey = newKey.clone();

            Integer budgetSum = 0;
            Integer settleSum = 0;

            categoryBetuCostLayout.add(
                    c.getCategoryTypeView(activity)
                    );

            while (chkKey(oldKey, newKey)) {

                budgetSum += c.getBudgetCost();
                settleSum += c.getEffectiveSettleCost();

                // 次明細読み込み
                if (costDetailsIterator.hasNext()) {
                    c = costDetailsIterator.next();
                    setKey(newKey, c);
                }
                else {
                    break;
                }
            }
            // ブレイク時処理
            categoryBetuCostLayout.add(
                    c.getBudgetCostView(activity, budgetSum),
                    c.getSettleCostView(activity, settleSum)
                    );
        }

        return categoryBetuCostLayout;
    }

    private void setKey(Integer[] key, CostDetail c) {

        key[0] = c.getBudgetYmd().get(Calendar.YEAR);
        key[1] = c.getBudgetYmd().get(Calendar.MONTH);
        key[2] = c.getCategoryType();
    }

    private boolean chkKey(Integer[] oldKey, Integer[] newKey) {
        return oldKey[0] - newKey[0] == 0 && oldKey[1] - newKey[1] == 0 && oldKey[2] - newKey[2] == 0;
    }

    private void makeHeader(MGridLayout categoryBetuCostLayout, CostDetail c) {
        String from = c.getBudgetYmd().get(Calendar.YEAR) + "/"
                + (c.getBudgetYmd().get(Calendar.MONTH) + 1) + "/"
                + accountBook.getStartDate().get(Calendar.DAY_OF_MONTH);

        Calendar tmp = (Calendar) c.getBudgetYmd().clone();
        tmp.set(Calendar.DAY_OF_MONTH, accountBook.getStartDate().get(Calendar.DAY_OF_MONTH));
        tmp.add(Calendar.MONTH, 1);
        tmp.add(Calendar.DAY_OF_MONTH, -1);

        String to = tmp.get(Calendar.YEAR) + "/"
                + (tmp.get(Calendar.MONTH) + 1) + "/"
                + tmp.get(Calendar.DAY_OF_MONTH);

        categoryBetuCostLayout.add(
                new MTextView(activity)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .text(from)
                        .backgroundDrawable(R.drawable.header_design)
                ,
                new MTextView(activity)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .text("～" + to)
                        .backgroundDrawable(R.drawable.header_design)
                ,
                new MTextView(activity)
                        .gravity(Gravity.CENTER_VERTICAL)
                        .backgroundDrawable(R.drawable.header_design)
                );
    }
}
