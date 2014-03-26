package com.android_mvc.sample_project.activities.accountbook;

import java.util.Calendar;

import net.simonvt.numberpicker.NumberPicker;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.view.MButton;
import com.android_mvc.framework.ui.view.MLinearLayout;
import com.android_mvc.framework.ui.view.MTextView;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.accountbook.data.CreditCardData;
import com.android_mvc.sample_project.activities.accountbook.lib.AccountBookAppUserBaseActivity;
import com.android_mvc.sample_project.common.Util;
import com.android_mvc.sample_project.controller.CreditCardController;
import com.android_mvc.sample_project.db.dao.CreditCardSettingDAO;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CreditCardSettingCol;

/**
 * サンプルのDB参照アクティビティ。
 * 
 * @author id:language_and_engineering
 * 
 */
public class CreditCardActivity extends AccountBookAppUserBaseActivity {

    private CreditCardData creditCardData;
    private CreditCardSetting inputData;

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
        final CreditCardActivity activity = this;

        // creditCardDataの初期化
        creditCardData = new CreditCardData();
        creditCardData.init(context);

        // DBからロード
        creditCardData.setCreditCardSetting(new CreditCardSettingDAO(this).findNewestOne());

        // まず親レイアウトを定義
        new UIBuilder(context)
                .setDisplayHeaderText("クレジットカード設定画面")
                .add(
                        creditCardData.getLayout1()
                )
                .display();

        // 変動費明細レコードが取得できた場合、ヘッダ行を表示する
        creditCardData.getLayout1().add(
                makeLabel()
                ,
                creditCardData.makeRecord(this)
                );

        // レコード追加ボタン
        creditCardData.getLayout1().add(
                new MTextView(context)
                        .paddingPx(5)
                        .textsize(1)
                ,
                new MButton(context)
                        .drawableLeft(android.R.drawable.ic_input_add)
                        .backgroundDrawable(R.drawable.button_design_h40_w345)
                        .text("登録")
                        .click(insert(creditCardData.getTv1(), creditCardData.getTv2()))
                );

        // 描画
        creditCardData.getLayout1().inflateInside();

    }

    private MLinearLayout makeLabel() {
        return new MLinearLayout(context)
                .orientationHorizontal()
                .widthFillParent()
                .heightWrapContent()
                .add(
                        new MTextView(context)
                                .text("締日")
                                .backgroundResource(R.drawable.header_design_h30_w173)
                                .gravity(Gravity.CENTER_VERTICAL)
                        ,
                        new MTextView(context)
                                .text("引き去り日")
                                .backgroundResource(R.drawable.header_design_h30_w172)
                                .gravity(Gravity.CENTER_VERTICAL)
                );
    }

    public OnClickListener insert(final MTextView tv1, final MTextView tv2) {
        final CreditCardActivity activity = this;

        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Calendar simeYmd = Calendar.getInstance();
                    Calendar siharaiYmd = Calendar.getInstance();

                    simeYmd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tv1.text()));
                    siharaiYmd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tv2.text()));

                    inputData = new CreditCardSetting();
                    inputData.setSimeYmd(simeYmd);
                    inputData.setSiharaiYmd(siharaiYmd);

                    CreditCardController.submit(activity);
                } catch (Exception e) {
                    UIUtil.longToast(activity, "クレジットカード設定に失敗しました。\n締日と引き去り日を入力してください。");
                }
            }
        };
    }

    @Override
    public ActivityParams toParams() {
        Util.d("toParams 締日：" + inputData.getSimeYmd().toString());
        Util.d("toParams 引き去り日：" + inputData.getSiharaiYmd().toString());
        // 入力された値をすべて回収
        return new ActivityParams()
                .add("締日", CreditCardSettingCol.SIME_YMD, inputData.getSimeYmd())
                .add("引き去り日", CreditCardSettingCol.SIHARAI_YMD, inputData.getSiharaiYmd());
    }

}