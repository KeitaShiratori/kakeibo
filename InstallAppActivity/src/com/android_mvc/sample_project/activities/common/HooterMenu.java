package com.android_mvc.sample_project.activities.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.controller.common.CommonController;

public class HooterMenu extends MLinearLayout {

    private boolean isNew = false;
    private HooterMenu hooterMenu;

    public HooterMenu(Context context) {
        super(context);
    }

    public HooterMenu getHooterMenu(Context context, final Activity activity) {
        if(hooterMenu == null){
            hooterMenu = new HooterMenu(context);
        }

        hooterMenu
                .orientationHorizontal()
                .widthWrapContent()
                .heightWrapContent()
                .paddingPx(10)
                .add(
                        new MButton(context)
                                .backgroundDrawable(drawable.button_design)
                                .text("目標金額照会")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        CommonController.submit(activity, "SHOW_ACCOUNT_BOOK");
                                    }

                                })

                        ,
                        new MButton(context)
                                .backgroundDrawable(drawable.button_design)
                                .text("予定照会")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        CommonController.submit(activity, "SHOW_BUDGET_SHOW");
                                    }

                                })

                        ,
                        new MButton(context)
                                .backgroundDrawable(drawable.button_design)
                                .text("実績照会")
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        CommonController.submit(activity, "SHOW_SETTLE_SHOW");
                                    }

                                })

                );
        return this.hooterMenu;
    }
}
