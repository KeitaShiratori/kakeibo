package com.android_mvc.sample_project.activities.accountbook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.android_mvc.framework.activities.base.BaseTabHostActivity;
import com.android_mvc.framework.ui.tab.TabDescription;
import com.android_mvc.framework.ui.tab.TabHostBuilder;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.controller.FuncDBController;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.IncomeDetail;

/**
 * 親タブのサンプル画面。
 * 
 * @author id:language_and_engineering
 * 
 */
public class ShowTabHostActivity extends BaseTabHostActivity
{
    // 子タブへの引継ぎ用変数
    public static String mode;
    public static Calendar startDate;
    public static List<CostDetail> costDetails;
    public static List<IncomeDetail> incomeDetails;

    // モード定義
    public static String NEW_RECORD_MODE = "NEW_RECORD_MODE";
    public static String DAY_MODE = "DAY_MODE";
    public static String WEEK_MODE = "WEEK_MODE";
    public static String MONTH_MODE = "MONTH_MODE";
    public static String ALL_MODE = "ALL_MODE";
    // 前後
    public static String BEFORE = "BEFORE";
    public static String AFTER = "AFTER";

    @Override
    public void defineContentView() {

        // 初期表示タブ判定
        String firstTab = new String();

        if ($.hasActionResult() && $.actionResultHasKey("FIRST_TAB")) {
            firstTab = $.getActionResult().get("FIRST_TAB").toString();
        }
        else if ($.extras() != null && !$.extras().isEmpty()) {
            firstTab = $.extras().getString("FIRST_TAB");
        }

        // モード判定
        if ($.hasActionResult()) {
            // 新規登録直後
            if ($.actionResultHasKey("new_cost_detail0")) {
                mode = NEW_RECORD_MODE;
                startDate = ((CostDetail) $.getActionResult().get("new_cost_detail0")).getBudgetYmd();

                if (costDetails == null) {
                    costDetails = new ArrayList<CostDetail>();
                }
                boolean procflag = true;
                int counter = 0;
                while (procflag) {
                    costDetails.add((CostDetail) $.getActionResult().get("new_cost_detail" + counter));
                    counter++;
                    if (!$.actionResultHasKey("new_cost_detail" + counter)) {
                        procflag = false;
                    }
                }
            }
        }
        // モード切替後(更新処理後、削除処理後も含む)
        else if ($.extras() != null
                && !$.extras().isEmpty()
                && $.extras().containsKey("MODE")
                && $.extras().containsKey("START_DATE")) {
            mode = $.extras().getString("MODE");
            startDate = (Calendar) $.extras().get("START_DATE");
        }
        // フッターメニューまたはTOPから遷移
        else {
            mode = DAY_MODE;
            startDate = Calendar.getInstance();
            startDate.set(Calendar.HOUR_OF_DAY, 00);
            startDate.set(Calendar.MINUTE, 00);
            startDate.set(Calendar.SECOND, 00);
        }

        // タブの定義を記述する。
        new TabHostBuilder(context)
                .setChildActivities(FuncDBController.getChildActivities(this))
                .add(
                        new TabDescription("SHOW_COST_DETAIL")
                                .text("変動費照会")
                                .icon(R.drawable.ic_sisyutu)
                        ,

                        new TabDescription("SHOW_INCOME_DETAIL")
                                .text("収入照会")
                                .icon(R.drawable.ic_syuunyuu)

                        ,
                        new TabDescription(getResources().getString(R.string.MYWALLET))
                                .text("財布の中身")
                                .icon(android.R.drawable.ic_menu_crop)
                )
                .display(firstTab);

    }
}
