package com.android_mvc.sample_project.activities.accountbook;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.activities.accountbook.data.SettleRecordData;
import com.android_mvc.sample_project.activities.accountbook.data.SettleShowActivityData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class SettleShowActivity extends AccountBookAppUserBaseActivity {

    // View定義
    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;

    // メンバ変数
    SettleShowActivityData settleShowActivityData;

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

        // データの初期化
        settleShowActivityData = new SettleShowActivityData();
        settleShowActivityData.init(this);

    }

    @Override
    public void defineContentView() {
        final SettleShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("実績照会")
                .add(layout1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .widthFillParent()
                        .heightWrapContent())
                .display();

        for (SettleRecordData s : settleShowActivityData.getSettleRecordData()) {
            layout1.add(
                    s.getDescription(context)
                    );
        }

        // 描画
        layout1.inflateInside();
    }

}