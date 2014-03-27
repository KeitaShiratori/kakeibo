package com.android_mvc.framework.common;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.sample_project.bat.AlarmReceiver;


/**
 * FW内でのコアな共通処理
 * @author id:language_and_engineering
 *
 */
public class FWUtil extends BaseUtil
{

    /**
     * アプリ起動時に，AP側の設定情報を受け取り，FW側に注入・初期化する。
     * 端末ブート時に自動起動するサービスからも利用される。
     */
    public static void receiveAppInfoAsFW( AbstractAppSettings settings )
    {
        // NOTE: FW側に存在する基底クラスIAppSettingsとして受理しているため，
        // 設定クラスでstaticメンバを使用できない点に注意。
        // もしstaticにすると，AP側でなくFW側のダミー値が返ってきてしまう。


        // ログのタグを初期化
        FWUtil.initAppTag( settings.getAppTagForLog() );


        // 開発モードのセット
        FWUtil.initDebuggingMode( settings.isDebuggingFlag() );

        // 開発時プリファレンスクリア設定のセット
        FWUtil.setForgetPrefOnDebug( settings.isForgetPrefsOnDebug() );

        // 開発時RDBクリア設定のセット
        FWUtil.setForgetRdbOnDebug( settings.isForgetRdbOnDebug() );


        // RDBの名称を登録
        DBHelper.setDB_NAME( settings.getDbName() );

        // RDBのファイルパスを登録
        DBHelper.setDB_FULLPATH( settings.getDbFullpath() );


        // GoogleMapsのAPI keyをセット
        FWUtil.setGoogleMapsAPIKey( settings.getGoogleMapsAPIKey() );

        // 他にAPから渡されるFW側の初期化処理

    }

    /**
     * 定刻起動サービスの開始
     */
    public static void setAlarmManager(Context context, Intent intent) {
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if(calendar.get(Calendar.HOUR_OF_DAY) > 19){
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // one shot
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//        Toast.makeText(BaseNormalActivity.this, "Start Alarm!", Toast.LENGTH_SHORT).show();

    }


}
