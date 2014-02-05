package com.android_mvc.sample_project.activities.accountbook;

import com.android_mvc.framework.activities.base.BaseTabHostActivity;
import com.android_mvc.framework.ui.tab.TabDescription;
import com.android_mvc.framework.ui.tab.TabHostBuilder;
import com.android_mvc.sample_project.controller.FuncDBController;

/**
 * 親タブのサンプル画面。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AnalysisTabHostActivity extends BaseTabHostActivity
{

    @Override
    public void defineContentView() {

        // 初期表示タブ判定
        String firstTab = new String(); 

        if ($.extras() != null && !$.extras().isEmpty()) {
           firstTab = $.extras().getString("FIRST_TAB");
        }

        // タブの定義を記述する。
        new TabHostBuilder(context)
                .setChildActivities(FuncDBController.getChildActivities(this))
                .add(
                        new TabDescription("SHOW_BUDGET_SHOW")
                                .text("予定")
                                .icon(android.R.drawable.ic_menu_add)
                        ,

                        new TabDescription("SHOW_SETTLE_SHOW")
                                .text("実績")
                                .icon(android.R.drawable.ic_menu_agenda)
                )
                .display(firstTab);

    }

}
