package com.android_mvc.sample_project.activities.main;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;

/**
 * サンプルのトップ画面。
 * 
 * @author id:language_and_engineering
 * 
 */
public class TopActivity extends BaseNormalActivity
{

    @Override
    public void defineContentView() {
        final TopActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
                .setDisplayHeaderText("TOP")
                .add(
                        new MLinearLayout(activity)
                                .orientationVertical()
                                .widthFillParent()
                                .heightFillParent()
                                .add(

                                )
                )
                .display();
    }

    @Override
    public void onBackPressed()
    {
        // 戻るキーが押されたら終了
        moveTaskToBack(true);
    }

}
