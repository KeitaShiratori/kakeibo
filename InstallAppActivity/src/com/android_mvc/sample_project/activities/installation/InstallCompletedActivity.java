package com.android_mvc.sample_project.activities.installation;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.MainController;

/**
 * サンプルのインストール完了画面。
 * @author id:language_and_engineering
 *
 */
public class InstallCompletedActivity extends BaseNormalActivity
{
    MButton button0;
    MTextView tv0;
    
    MLinearLayout layout1;

    MTextView tv1_1;
    MEditText et1;
    MTextView tv1_2;

    MTextView tv2_1;
    MEditText et2;
    MTextView tv2_2;

    MTextView tv3_1;
    MTextView tv3_2;
    MButton b3;
    DatePickerDialog dpd3;
    
    MLinearLayout layout2;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void defineContentView() {
        final InstallCompletedActivity activity = this;

        // ここに，画面上のUI部品の定義を記述する。
        new UIBuilder(context)
            .add(
               tv0 = new MTextView(context)
                   .text("目標金額と目標期間を設定してください。" )
                   .widthWrapContent()
               ,

               layout1 = new MLinearLayout(context)
               .orientationHorizontal()
               .widthFillParent()
               .add(

                 tv3_1 = new MTextView(context)
                   .text("開始日:" )
                   .widthWrapContent()
                 ,
                 
                 tv3_2 = new MTextView(context)
                   .text(year + "/" + (month + 1) + "/" + day)
                   .widthWrapContent()
                   ,

                   b3 = new MButton(context)
                     .text("入力")
                     .click(new OnClickListener(){

                         @Override
                         public void onClick(View v) {
                             dpd3 = new DatePickerDialog(
                                     context,
                                     new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                         	tv3_2.setText(
                                                 String.valueOf(year) + "/" +
                                                 String.valueOf(monthOfYear + 1) + "/" +
                                                 String.valueOf(dayOfMonth));
                                         }
                                     },
                                     year, month, day);
                             dpd3.show();
                         }

                     })
               ),

               layout1 = new MLinearLayout(context)
               .orientationHorizontal()
               .widthFillParent()
               .add(
            	   tv1_1 = new MTextView(context)
            	   	   .text("目標金額")
                       .widthWrapContent()
                   ,

                   et1 = new MEditText(context)
                       .widthPx(300)
                   ,

                   tv1_2 = new MTextView(context)
        	   	   .text("円")
                   .widthWrapContent()
                   ),

                   layout2 = new MLinearLayout(context)
                   .orientationHorizontal()
                   .widthFillParent()
                   .add(
                	   tv2_1 = new MTextView(context)
                	   	   .text("目標期間")
                           .widthWrapContent()
                       ,

                       et2 = new MEditText(context)
                           .widthPx(300)
                       ,

                       tv2_2 = new MTextView(context)
            	   	   .text("ヶ月")
                       .widthWrapContent()
                       ),
               button0 = new MButton(context)
                   .widthFillParent()
                   .text("目標を設定してトップ画面へ" )
                   .click(new OnClickListener(){
                       @Override
                       public void onClick(View v) {
                           MainController.submit(activity);
                       }
                   })
            )
        .display();

    }

    @Override
    public ActivityParams toParams() {
        // 入力された値をすべて回収
    	return new ActivityParams()
            .add("目標金額", "mokuhyou_kingaku", et1.text() )
            .add("目標期間", "mokuhyou_kikan", et2.text() )
            .add("使用年月日", "start_date", Util.toCalendar(tv3_2.getText().toString()) )
            ;
    }


    @Override
    public void onBackPressed()
    {
        // 戻るボタンで戻させない
    }

}
