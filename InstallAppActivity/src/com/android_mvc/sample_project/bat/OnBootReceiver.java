package com.android_mvc.sample_project.bat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android_mvc.framework.bat.BaseOnBootReceiver;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.common.AppSettings;
import com.android_mvc.sample_project.common.Util;

/**
 * 端末起動時の処理。
 * 
 * @author id:language_and_engineering
 * 
 */
public class OnBootReceiver extends BaseOnBootReceiver
{
    @Override
    protected void onDeviceBoot(Context context)
    {
        // アプリ側の設定を初期化
        FWUtil.receiveAppInfoAsFW(new AppSettings(context));
    }

    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // プリファレンスの設定を確認する
            boolean checked = true;
            if (checked) {
                // 端末ブート完了時にアプリアイコンを置く
                KakeiboNotification.putNotice(context);
            }
        }
    }

}
