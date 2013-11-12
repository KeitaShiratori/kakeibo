package com.android_mvc.sample_project.activities.common;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.controller.FuncDBController;

public class HooterMenu extends MLinearLayout {

    private boolean isNew = false;
    private HooterMenu hooterMenu;

    private HooterMenu(Context context) {
        super(context);
    }

    public HooterMenu getHooterMenu(Context context) {
        hooterMenu = new HooterMenu(context);

        hooterMenu
                .orientationHorizontal()
                .widthWrapContent()
                .heightWrapContent()
                .paddingPx(10)
                .add(
                        new MButton(context)
                                .backgroundDrawable(drawable.button_design)
                                .click(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        CommonController.submit(context);
                                    }

                                })
                );
        return this.hooterMenu;
    }
}
