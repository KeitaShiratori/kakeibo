package com.android_mvc.sample_project.bat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;

import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.main.TopActivity;

public class KakeiboNotification {

    public static final int NOTIFICATION_ID = 1523;

    private static Notification createNotification(Context context) {
        Notification notification = new Notification(
                android.R.drawable.ic_menu_day, // ステータスに置くスモールアイコン
                "", // アイコン横のツールテキスト無し
                System.currentTimeMillis() // システム時刻
        );

        // PendingIntent.getActivity(context, requestCode, intent, flags)
        PendingIntent pi = PendingIntent.getActivity(
                context,
                0, // requestCode
                new Intent(context, TopActivity.class),
                0 // Default flags
                );
        notification.setLatestEventInfo(
                context,
                context.getString(R.string.app_name),
                context.getString(R.string.pandingIntent_name),
                pi
                );
        // // Set Notification To StatusBar
        // //※これがポイント
        // // フラグを設定しないと通知領域に表示されてしまう
        // // 通知領域に表示するのが目的なら flags の設定は不要になる
        // notification.flags = notification.flags
        // | Notification.FLAG_NO_CLEAR // クリアボタンを表示しない ※ユーザがクリアできない
        // | Notification.FLAG_ONGOING_EVENT; // 継続的イベント領域に表示 ※「実行中」領域
        notification.number = 0;
        return notification;
    }

    // 1. ステータスバーに通知
    // 1-1. ブロードキャストレシーバーから起動時に呼び出す
    // 1-2. アプリの終了時に呼び出す
    public static void putNotice(Context context) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(context);
        nm.notify(KakeiboNotification.NOTIFICATION_ID, notification);
    }

    // 2. ステータスバーから削除
    // 2-1. アプリの起動時に呼び出す
    public static void removeNotice(Context context) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

}
