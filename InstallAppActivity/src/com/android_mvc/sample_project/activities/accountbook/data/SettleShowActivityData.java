package com.android_mvc.sample_project.activities.accountbook.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.dao.CreditCardSettingDAO;
import com.android_mvc.sample_project.db.dao.IncomeDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.db.entity.lib.AccountBookDetailComparator;
import com.android_mvc.sample_project.db.entity.lib.CostDetailComparator;
import com.android_mvc.sample_project.db.entity.lib.IncomeDetailComparator;

// SettleShowActivityクラスで使用するデータを保持するクラス
public class SettleShowActivityData {
    // メンバ変数
    AccountBook accountBook;
    List<AccountBookDetail> accountBookDetails;
    List<CostDetail> costDetails;
    List<IncomeDetail> incomeDetails;
    Calendar accountBookStartDate;
    Integer accountBookOperator1;
    Integer accountBookOperator2;
    Integer settleCost;
    Integer settleIncome;
    List<SettleRecordData> settleRecordData;
    CreditCardSetting creditCardSetting;

    // 処理フラグ
    private static String COST = "C";
    private static String INCOME = "I";
    private static String NOTHING = "";

    // 初期処理
    public void init(AccountBookAppUserBaseActivity activity) {
        accountBook = new AccountBookDAO(activity).findAll().get(0);
        accountBookStartDate = accountBook.getStartDate();
        accountBookDetails = new AccountBookDetailDAO(activity).findAll();
        costDetails = new CostDetailDAO(activity).findByOrder();
        incomeDetails = new IncomeDetailDAO(activity).findOrderByBudgetYmd();
        creditCardSetting = new CreditCardSettingDAO(activity).findNewestOne();

        settleRecordData = new ArrayList<SettleRecordData>();
        for (int i = accountBookDetails.size() - 1; i >= 0; i--) {
            AccountBookDetail a = accountBookDetails.get(i);
            SettleRecordData tmp = new SettleRecordData();
            tmp.setYoteiYYYYMM(a.getMokuhyouMonth());
            tmp.setMokuhyouKingaku(a.getMokuhyouMonthKingaku());
            tmp.setStartDate(accountBookStartDate.get(Calendar.DAY_OF_MONTH));
            settleRecordData.add(tmp);
        }

        this.controllBreak();
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

    public Calendar getAccountBookStartDate() {
        return accountBookStartDate;
    }

    public void setAccountBookStartDate(Calendar accountBookStartDate) {
        this.accountBookStartDate = accountBookStartDate;
    }

    public Integer getAccountBookOperator1() {
        return accountBookOperator1;
    }

    public void setAccountBookOperator1(Integer accountBookOperator1) {
        this.accountBookOperator1 = accountBookOperator1;
    }

    public Integer getAccountBookOperator2() {
        return accountBookOperator2;
    }

    public void setAccountBookOperator2(Integer accountBookOperator2) {
        this.accountBookOperator2 = accountBookOperator2;
    }

    public Integer getSettleCost() {
        return settleCost;
    }

    public void setSettleCost(Integer settleCost) {
        this.settleCost = settleCost;
    }

    public Integer getSettleIncome() {
        return settleIncome;
    }

    public void setSettleIncome(Integer settleIncome) {
        this.settleIncome = settleIncome;
    }

    public List<SettleRecordData> getSettleRecordData() {
        return settleRecordData;
    }

    public void setSettleRecordData(List<SettleRecordData> settleRecordData) {
        this.settleRecordData = settleRecordData;
    }

    public boolean sortAccountBookDetails() {
        if (this.accountBookDetails.size() == 0) {
            return false;
        }
        Collections.sort(this.accountBookDetails, new AccountBookDetailComparator());
        return true;
    }

    public boolean sortCostDetails() {

        if (this.costDetails.size() == 0) {
            return false;
        }
        Collections.sort(this.costDetails, new CostDetailComparator());
        return true;
    }

    public boolean sortIncomeDetails() {
        if (this.incomeDetails.size() == 0) {
            return false;
        }
        Collections.sort(this.incomeDetails, new IncomeDetailComparator());
        return true;
    }

    private boolean controllBreak() {
        // ソート済みのリストを受け取る前提

        // 初期処理 フラグの定義
        boolean bContinueFlag = false;
        boolean iContinueFlag = false;
        boolean cContinueFlag = false;
        Calendar ymd = Calendar.getInstance();
        Integer mokuhyouKingaku;
        Integer iSum = 0;
        Integer cSum = 0;
        Integer[] creditSum = new Integer[accountBookDetails.size()];
        for (int i = 0; i < accountBookDetails.size(); i++) {
            creditSum[i] = 0;
        }
        String procFlag = NOTHING; // COST or INCOME or NOTHING
        int cPos = 0;
        int iPos = 0;
        int bPos = 0;

        // リストの読み込み
        bContinueFlag = sortAccountBookDetails();
        // AccountBookDetailが読み込めなかったら処理終了
        if (!bContinueFlag) {
            return false;
        }

        iContinueFlag = sortIncomeDetails();
        cContinueFlag = sortCostDetails();

        // 処理フラグの設定
        if (iContinueFlag && cContinueFlag) {
            procFlag = COST;
        } else if (!iContinueFlag && cContinueFlag) {
            procFlag = COST;
        } else if (iContinueFlag && !cContinueFlag) {
            procFlag = INCOME;
        } else {
            procFlag = NOTHING;
        }

        // コントロールブレイク
        while (bContinueFlag) {
            // 集計対象の年月を取得
            ymd = this.accountBookDetails.get(bPos).getMokuhyouMonth();
            // 開始日を取得
            ymd.set(Calendar.DAY_OF_MONTH, accountBookStartDate.get(Calendar.DAY_OF_MONTH));

            mokuhyouKingaku = this.accountBookDetails.get(bPos).getMokuhyouMonthKingaku();
            cSum = 0;
            iSum = 0;

            while (cContinueFlag && procFlag.equals(COST)) {
                // ymdを基準として、有効日付の大小をチェックする。
                int cChkYMDResult = chkYMD(ymd, this.costDetails.get(cPos).getEffectiveYMD());

                // 集計対象月以前に登録されたレコードで、クレジットカード払いのレコードが存在する場合は、
                // 特別に集計対象とする。
                if (bPos == 0
                        && cChkYMDResult < 0
                        && this.costDetails.get(cPos).getPayType() == 2
                        && creditCardSetting != null) {
                    cChkYMDResult = 0;
                }

                // ymdと有効日付の年月が同じ場合
                if (cChkYMDResult == 0) {
                    // 支払方法がクレジットかつ、クレジットカード設定が登録済みの場合、creditSumに支払金額を計上する。
                    if (this.costDetails.get(cPos).getPayType() == 2 && creditCardSetting != null) {
                        int offset = 0;
                        // CostDetailの予定日付とクレカの締日を比較して、締日以降であればoffsetをインクリメントする。
                        if (costDetails.get(cPos).getBudgetYmd().get(Calendar.DAY_OF_MONTH)
                        > creditCardSetting.getSimeYmd().get(Calendar.DAY_OF_MONTH)) {
                            offset++;
                        }
                        // クレカの支払日と家計簿の基準日を比較して、クレカの支払日が小さければoffsetをデクリメントする
                        if (creditCardSetting.getSiharaiYmd().get(Calendar.DAY_OF_MONTH)
                        <= accountBook.getStartDate().get(Calendar.DAY_OF_MONTH)) {
                            offset--;
                        }
                        for (int divide = 0; divide < this.costDetails.get(cPos).getDivideNum(); divide++) {
                            if (bPos + 1 + divide + offset < this.accountBookDetails.size()
                                    && bPos + 1 + divide + offset >= 0) {
                                creditSum[bPos + 1 + divide + offset] += costDetails.get(cPos).getEffectiveCost()
                                        / costDetails.get(cPos).getDivideNum();
                            }
                        }
                    }
                    else {
                        cSum += this.costDetails.get(cPos).getEffectiveSettleCost();
                    }
                    if (cPos + 1 < this.costDetails.size()) {
                        cPos++;
                    } else {
                        cContinueFlag = false;
                        procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, cChkYMDResult);
                    }
                } else if (cChkYMDResult < 0) {
                    // 集計対象の年月（ymd）より有効日付が過去日の場合、次のレコードを読み込む
                    if (cPos + 1 < this.costDetails.size()) {
                        cPos++;
                    } else {
                        cContinueFlag = false;
                        procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, cChkYMDResult);
                    }
                } else if (cChkYMDResult > 0) {
                    // 集計対象の年月（ymd）より有効日付が未来日の場合、処理フラグを変更する。
                    procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, cChkYMDResult);
                    if (procFlag == COST) {
                        break;
                    }
                }
            }
            cSum += creditSum[bPos];
            this.settleRecordData.get(bPos).setCostSum(cSum);

            while (iContinueFlag && procFlag.equals(INCOME)) {
                int iChkYMDResult = chkYMD(ymd, this.incomeDetails.get(iPos).getEffectiveYMD());
                if (iChkYMDResult == 0) {
                    // 集計対象の年月と有効日付の年月が同じ場合
                    iSum += this.incomeDetails.get(iPos).getEffectiveSettleIncome();
                    if (iPos + 1 < this.incomeDetails.size()) {
                        iPos++;
                    } else {
                        iContinueFlag = false;
                        procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, iChkYMDResult);
                    }
                } else if (iChkYMDResult < 0) {
                    // 集計対象の年月（ymd）より有効日付が過去日の場合、次のレコードを読み込む
                    if (iPos + 1 < this.incomeDetails.size()) {
                        iPos++;
                    } else {
                        iContinueFlag = false;
                        procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, iChkYMDResult);
                    }
                } else if (iChkYMDResult > 0) {
                    // 集計対象の年月（ymd）より有効日付が未来日の場合、処理フラグを変更する。
                    procFlag = setProcFlag(iContinueFlag, cContinueFlag, procFlag, iChkYMDResult);
                    if (procFlag == INCOME) {
                        break;
                    }
                }
            }
            this.settleRecordData.get(bPos).setIncomeSum(iSum);

            // bPosの更新、bContinueFlagの設定、可処分所得の設定
            this.settleRecordData.get(bPos).setDisposablencome(iSum - cSum - mokuhyouKingaku);
            this.settleRecordData.get(bPos).setYoteiYYYYMM(ymd);
            if (bPos + 1 < this.accountBookDetails.size()) {
                bPos++;
            } else {
                bContinueFlag = false;
            }
        }
        return true;
    }

    /**
     * 処理フラグを設定する
     * 
     * @param iContinueFlag
     * @param cContinueFlag
     * @param procFlag
     * @return
     */
    private String setProcFlag(boolean iContinueFlag, boolean cContinueFlag, String procFlag, int chkResult) {
        if (procFlag == NOTHING) {
            return NOTHING;
        }
        if (!iContinueFlag && !cContinueFlag) {
            return NOTHING;
        }
        if (iContinueFlag) {
            if (cContinueFlag && procFlag.equals(COST) && chkResult == 0) {
                return COST;
            } else if (cContinueFlag && procFlag.equals(INCOME) && chkResult == 1) {
                return COST;
            } else if (cContinueFlag && procFlag.equals(COST) && chkResult == -1) {
                return COST;
            } else {
                return INCOME;
            }
        }
        if (cContinueFlag) {
            return COST;
        }
        return NOTHING;
    }

    private int chkYMD(Calendar baseYMD, Calendar comparedYMD) {
        // 実績日がnullの場合、集計対象から除外する。
        if (comparedYMD == null) {
            return -1;
        }

        // 基準日を数値化
        long base = baseYMD.get(Calendar.YEAR) * 10000 + baseYMD.get(Calendar.MONTH) * 100 + baseYMD.get(Calendar.DAY_OF_MONTH);

        // 基準日の翌月を数値化
        Calendar baseNextYMD = (Calendar) baseYMD.clone();
        baseNextYMD.add(Calendar.MONTH, 1);
        long baseNextMonth = baseNextYMD.get(Calendar.YEAR) * 10000 + baseNextYMD.get(Calendar.MONTH) * 100 + baseNextYMD.get(Calendar.DAY_OF_MONTH);

        // 比較対象の日付を数値化
        long comp = comparedYMD.get(Calendar.YEAR) * 10000 + comparedYMD.get(Calendar.MONTH) * 100 + comparedYMD.get(Calendar.DAY_OF_MONTH);

        if (base > comp) {
            return -1;
        } else if (base <= comp && comp < baseNextMonth) {
            return 0;
        } else {
            return 1;
        }
    }

}
