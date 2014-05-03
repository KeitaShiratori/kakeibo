package com.android_mvc.sample_project.activities.accountbook;

import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.sample_project.activities.accountbook.data.SamaryShowActivityData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class SamaryShowActivity extends AccountBookAppUserBaseActivity {

    // View定義
    MLinearLayout layout1;

    // メンバ変数
    SamaryShowActivityData samaryShowActivityData;

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
        final SamaryShowActivity activity = this;

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("統計分析")
                .add(layout1 = new MLinearLayout(activity)
                        .orientationVertical()
                        .widthFillParent()
                        .paddingPx(10)
                        .heightWrapContent())
                .display();

        // データクラスの取得
        samaryShowActivityData = SamaryShowActivityData.getInstance(this);

        // 画面レイアウトの生成
        layout1.add(
                samaryShowActivityData.getLayout()
                );

        // 描画
        layout1.inflateInside();
    }
}
