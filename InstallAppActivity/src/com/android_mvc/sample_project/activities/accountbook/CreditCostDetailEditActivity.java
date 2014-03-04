package com.android_mvc.sample_project.activities.accountbook;

import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.db.entity.CostDetail;

/**
 * アプリの初期化処理を実行する画面。 アプリ内でのLAUNCHERアクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CreditCostDetailEditActivity extends AccountBookAppUserBaseActivity {

    private CostDetail inputData;

    @Override
    public void defineContentView() {

        final CreditCostDetailEditActivity activity = this;

        // 入力フォームUIを動的に構築する。
        new UIBuilder(context)
                .setDisplayHeaderText(s(R.string.SISYUTU_TOUROKU_KUREJITTO))
                .add(
                )
                .display();

    }

    private MLinearLayout makeRowLabel() {
        MTextView label = new MTextView(context);
        MTextView field = new MTextView(context);
        
        return new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .add(label, field);
    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
        return new ActivityParams()
        ;
//                .add("予定年月日", "budget_ymd", Util.toCalendar(bYMD.text()))
//                .add("予算費用", "budget_cost", et2.text())
//                .add("カテゴリ名", CostDetailCol.CATEGORY_TYPE, (sp3.getSelectedItemPosition() + 1))
//                .add("使用年月日", "settle_ymd", Util.toCalendar(settleYmd))
//                .add("実績費用", "settle_cost", et5.text())
//                .add("支払方法", CostDetailCol.PAY_TYPE, (sp6.getSelectedItemPosition()) + 1)
//                .add("繰り返し区分", "repeat_dvn", sp7.getSelectedItem());
    }

}
