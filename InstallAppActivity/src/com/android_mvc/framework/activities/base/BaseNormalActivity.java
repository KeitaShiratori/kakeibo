package com.android_mvc.framework.activities.base;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android_mvc.framework.activities.CommonActivityUtil;
import com.android_mvc.framework.activities.IBaseActivity;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.controller.action.ActionResult;
import com.android_mvc.framework.controller.validation.ActivityParams;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.menu.OptionMenuBuilder;
import com.android_mvc.framework.ui.menu.OptionMenuDescription;
import com.android_mvc.sample_project.bat.AlarmReceiver;
import com.android_mvc.sample_project.bat.KakeiboNotification;
import com.android_mvc.sample_project.bat.PeriodicService;
import com.android_mvc.sample_project.controller.MainController;
import com.android_mvc.sample_project.db.dao.CostDetailDAO;
import com.android_mvc.sample_project.db.dao.PrefDAO;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;

/**
 * 非Map系Activityの基底クラス。
 * 
 * @author id:language_and_engineering
 * 
 */
public abstract class BaseNormalActivity extends Activity implements IBaseActivity
{

    // ここから下はBase系Activity間で共通

    // ----- 一般メンバ -----

    // Activityの共通便利クラス
    protected CommonActivityUtil<BaseNormalActivity> $;

    // prefDAO
    protected PrefDAO pref;

    // UI構築用
    protected Activity context;

    @Override
    public ActivityParams toParams() {
        return null;
    }

    @Override
    public void afterBLExecuted(ActionResult ares)
    {
    }

    // ----- 画面初期化関連 -----

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        $ = new CommonActivityUtil<BaseNormalActivity>();
        $.onActivityCreated(this);

        // 日替わり処理
        pref = new PrefDAO();
        Calendar lastUpdateYMD = pref.getLastUpdateYMD(context);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_YEAR) > lastUpdateYMD.get(Calendar.DAY_OF_YEAR)
                && now.get(Calendar.YEAR) == lastUpdateYMD.get(Calendar.YEAR)) {
            UIUtil.longToast(context, "日が変わりました。");
        }
        else if (now.get(Calendar.YEAR) > lastUpdateYMD.get(Calendar.YEAR)) {
            // 昨日分の変動費明細を取得
            CostDetailDAO cdDao = new CostDetailDAO(context);
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            List<CostDetail> cDetails = cdDao.findWhereIn(CostDetailCol.BUDGET_YMD, yesterday.toString());

            boolean flag = false;
            int costSum = 0;
            for (CostDetail c : cDetails) {
                if (c.getSettleCost() == null) {
                    flag = true;
                    break;
                }
                costSum += c.getSettleCost();
            }

            if (flag) {
                UIUtil.longToast(context, "日が変わりました。");
            }

        }

        // // 画面サイズ取得処理
        // int height = pref.getDisplaySizeHeight(context);
        // int width = pref.getDisplaySizeWidth(context);
        //
        // if (height == 0 || width == 0) {
        // // ウィンドウマネージャのインスタンス取得
        // WindowManager wm = (WindowManager)
        // getSystemService(Context.WINDOW_SERVICE);
        // // ディスプレイのインスタンス生成
        // Display disp = wm.getDefaultDisplay();
        // pref.updateDisplaySizeHeight(context, disp.getHeight());
        // pref.updateDisplaySizeWidth(context, disp.getWidth());
        // }
        //

        // 通知予約をセット
        Intent intent = new Intent(BaseNormalActivity.this, AlarmReceiver.class);
        FWUtil.setAlarmManager(context, intent);

        // 通知を削除
        KakeiboNotification kn = new KakeiboNotification();
        kn.removeNotice(context);

    }

    @Override
    public void procAsyncBeforeUI() {
    }

    @Override
    public boolean requireProcBeforeUI() {
        return false;
    }

    @Override
    public void afterViewDefined()
    {
    }

    @Override
    public void onPause() {
        new PrefDAO().updateLastUpdateYMD(context, Calendar.getInstance());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        new PrefDAO().updateLastUpdateYMD(context, Calendar.getInstance());
        super.onDestroy();
    }

    // ------ メニュー関連 ------

    @Override
    public OptionMenuBuilder defineMenu()
    {
        final BaseNormalActivity activity = this;

        // オプションメニューを構築
        return new OptionMenuBuilder(context)
            .add(
                new OptionMenuDescription()
                {
                    @Override
                    protected String displayText() {return "簡単使い方ガイド";}

                    @Override
                    protected void onSelected() {
                        // 画面遷移
                        MainController.submit(activity, "GUIDE");
                    }
                }
            )
            .add(
                new OptionMenuDescription()
                {
                    @Override
                    protected String displayText() {return "FAQ";}

                    @Override
                    protected void onSelected() {
                        // 画面遷移
                        MainController.submit(activity, "FAQ");
                    }
                }
            )
        ;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu = $.renderOptionMenuAsDescribed(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        $.onOptionItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    // 便利メソッド
    protected String s(int resorceId) {
        return getResources().getString(resorceId);
    }

    /**
     * 常駐サービスを起動
     */
    public void startPeriodicService() {
        new PeriodicService().startResident(context);
    }


}
