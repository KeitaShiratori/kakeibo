package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MEditText;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R.drawable;
import com.android_mvc.sample_project.activities.common.HooterMenu;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.AccountBookShowController;
import com.android_mvc.sample_project.db.dao.AccountBookDAO;
import com.android_mvc.sample_project.db.dao.AccountBookDetailDAO;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class AccountBookShowActivity extends BaseNormalActivity {

    MLinearLayout layout1;
    MLinearLayout layout2;
    HooterMenu hooterMenu;
    MTextView tv1;
    MTextView tv2;

    // 全友達のリスト
    AccountBook accountBook;
    List<AccountBookDetail> accountBookDetails;

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

        // 全CostDetailをDBからロード
        accountBook = new AccountBookDAO(this).findAll().get(0);
        accountBookDetails = new AccountBookDetailDAO(this).findAll();
    }

    @Override
    public void defineContentView() {
        final AccountBookShowActivity activity = this;

//        // タブの定義を記述する。
//        new TabHostBuilder(context)
//            .setChildActivities( AccountBookShowController.getChildActivities(this) )
//            .add(
//                new TabDescription("MONTH_MOKUHYOU_EDIT")
//                    .text("月目標登録")
//                    .icon(android.R.drawable.ic_menu_add)
//            )
//            .display()
//        ;

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
                            .text( accountBook.getDescription() ) // この変動費明細の説明を取得
                            .widthWrapContent()
                        )
                    );

        for (final AccountBookDetail a : accountBookDetails){
        	layout1.add(
        			//月ごとの目標金額を表示する。
        			new MLinearLayout(context)
                    .orientationHorizontal()
                    .widthFillParent()
                    .heightWrapContent()
                    .paddingPx(10)
                    .add(

                        new MTextView(context)
                            .text( a.getDescription()) 
                            .widthWrapContent()

                        ,
                        
                        new MButton(context)
                            .text("・")
                            .click(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									inputDialogMokuhyouKingaku(a);
								}
							})
                    )
            );
        			
        }
        layout1.add(
                layout2 = new MLinearLayout(activity)
                  .orientationHorizontal()
                  .widthFillParent()
                  .heightWrapContent()
                  .gravity(Gravity.BOTTOM)
                  .add(
                          //ホームへ戻る
                          new MButton(context)
                            .backgroundResource(drawable.home)
                            .click(new OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    AccountBookShowController.submit(activity, "BACK_TO_TOP", null);
                                }

                            })
                            .longClick(new OnLongClickListener() {
                                
                                @Override
                                public boolean onLongClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    UIUtil.longToast(activity, "トップ画面へ");
                                    return false;
                                }
                            })
                          ,
                          //変動費登録画面
                          new MButton(activity, null, drawable.button_design_a1)
                            .backgroundResource(drawable.edit)
                            .click(new OnClickListener() {
                                
                                @Override
                                public void onClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    AccountBookShowController.submit(activity, "EDIT_COST_DETAIL", null);
                                }
                            })
                            .longClick(new OnLongClickListener() {
                                
                                @Override
                                public boolean onLongClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    UIUtil.longToast(activity, "変動費登録画面へ");
                                    return false;
                                }
                            })
                          ,
                          //変動費照会画面
                          new MButton(activity, null, drawable.button_design_a1)
                            .backgroundResource(drawable.book)
                            .click(new OnClickListener() {
                                
                                @Override
                                public void onClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    AccountBookShowController.submit(activity, "SHOW_COST_DETAIL", null);
                                }
                            })
                            .longClick(new OnLongClickListener() {
                                
                                @Override
                                public boolean onLongClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    UIUtil.longToast(activity, "変動費照会画面へ");
                                    return false;
                                }
                            })
                          ,
                          //予定照会画面
                          new MButton(activity, null, drawable.button_design_a1)
                            .backgroundResource(drawable.box)
                            .click(new OnClickListener() {
                                
                                @Override
                                public void onClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    AccountBookShowController.submit(activity, "SHOW_BUDGET_SHOW", null);
                                }
                            })
                            .longClick(new OnLongClickListener() {
                                
                                @Override
                                public boolean onLongClick(View v) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    UIUtil.longToast(activity, "予定照会画面へ");
                                    return false;
                                }
                            })
                          )
                          ,
                          hooterMenu = new HooterMenu(context).getHooterMenu(context, activity)
                );

        // 描画
        layout1.inflateInside();
    }

    private void inputDialogMokuhyouKingaku(final AccountBookDetail a) {

        Calendar c = a.getMokuhyouMonth();
        final MEditText et = new MEditText(context);
        new AlertDialog.Builder(AccountBookShowActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月の目標金額")
                .setView(et)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            Integer mokuhyouKingaku = Integer.parseInt(et.getText().toString());
                            a.setMokuhyouMonthKingaku(mokuhyouKingaku);
                            a.setAutoInputFlag(false);
                            AccountBookShowController.submit(AccountBookShowActivity.this, a);
                        } catch (NumberFormatException e) {
                            UIUtil.longToast(context, "数値を入力してください");
                        }
                    }

                })
                .setNegativeButton("自動入力",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                a.setAutoInputFlag(true);
                                AccountBookShowController.submit(AccountBookShowActivity.this, a);
                            }
                        }).show();
    }

}