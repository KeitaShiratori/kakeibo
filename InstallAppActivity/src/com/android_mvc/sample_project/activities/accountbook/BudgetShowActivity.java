package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.activities.accountbook.data.BudgetRecordData;
import com.android_mvc.sample_project.activities.accountbook.data.BudgetShowActivityData;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.db.entity.lib.CostDetailComparator;
import com.android_mvc.sample_project.db.entity.lib.IncomeDetailComparator;

/**
 * サンプルのDB参照アクティビティ。
 * @author id:language_and_engineering
 *
 */
public class BudgetShowActivity extends BaseNormalActivity {

	// View定義
    MLinearLayout layout1;
    MTextView tv1;
    MTextView tv2;


    // メンバ変数
    BudgetShowActivityData budgetShowActivityData;

    


    @Override
    public boolean requireProcBeforeUI(){
        // UI構築前に処理を要求する
    	
        return true;
    }


    // UI構築前に別スレッドで実行される処理
    @Override
    public void procAsyncBeforeUI(){
        //
        Util.d("UI構築前に実行される処理です。");

        // 全CostDetailをDBからロード
        budgetShowActivityData = new BudgetShowActivityData();

        // データの初期化
        budgetShowActivityData.init(this);


    }


    @Override
    public void defineContentView() {
        final BudgetShowActivity activity = this;
    	
    	// 取得したデータをソートする
    	

        // まず親レイアウトを定義
        new UIBuilder(context)
        .add(
            layout1 = new MLinearLayout(context)
              .orientationVertical()
              .widthFillParent()
              .heightWrapContent()
              .add(

                tv1 = new MTextView(context)
                  .text("ここにDBの中身が列挙されます。" )
                  .widthWrapContent()
                  .paddingPx(10)
                ,

                tv2 = new MTextView(context)
                  .invisible()
                  .textColor(Color.RED)
                  .widthWrapContent()
                  .paddingPx(10)

              )
        )
        .display();

        // レイアウト内に動的に全友達の情報を表示。Adapterは不要。
            layout1.add(
                // 水平方向のレイアウトを１個追加
                new MLinearLayout(context)
                    .orientationHorizontal()
                    .widthFillParent()
                    .heightWrapContent()
                    .paddingPx(10)
                    .add(

                        new MTextView(context)
                            .widthWrapContent()
                        )
                    );

        for(BudgetRecordData b : budgetShowActivityData.getBudgetRecordData()){
		layout1.add(
    			//月ごとの目標金額を表示する。
    			new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .paddingPx(10)
                .add(

                    new MTextView(context)
                        .text(b.getDescription())
                        .widthWrapContent()

                    ,
                    
                    new MButton(context)
                        .text("・")
                        .click(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							}
						})
                )
        );
        }
		layout1.add(
            new MButton(context)
              .text("トップに戻る")
              .click(new OnClickListener(){

                  @Override
                  public void onClick(View v) {
//                      FuncDBController.submit(activity, "BACK_TO_TOP", null);
                  }

              })
        );

        // 描画
        layout1.inflateInside();
    }



}