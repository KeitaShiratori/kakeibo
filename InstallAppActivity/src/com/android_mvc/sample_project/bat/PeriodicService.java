package com.android_mvc.sample_project.bat;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android_mvc.framework.bat.BasePeriodicService;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.ISequentialRunnable;
import com.android_mvc.framework.task.RunnerFollower;
import com.android_mvc.framework.task.SequentialAsyncTask;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.sample_project.R;
import com.android_mvc.sample_project.activities.main.TopActivity;
import com.android_mvc.sample_project.common.Util;

/**
 * 常駐型サービスのサンプル。 現在地を定期的にDBに記録する。
 * 
 * @author id:language_and_engineering
 * 
 */
public class PeriodicService extends BasePeriodicService
{
    // 画面から常駐を解除したい場合のために，常駐インスタンスを保持
    public static BasePeriodicService activeService;

    @Override
    protected long getIntervalMS()
    {
        // 現在時刻と次回起動時刻の差分を計算する。
        long now = System.currentTimeMillis();
        long next = getNextProcTime().getTimeInMillis();
        long ret = next - now;

        return ret;
    }

    private Calendar getNextProcTime() {
        // 現在時刻を取得する
        Calendar ret = Calendar.getInstance();

        // 20時以降なら、起動時刻を翌日にする。
        if (ret.get(Calendar.HOUR_OF_DAY) > 19) {
            ret.add(Calendar.DATE, 1);
        }

        // 起動時刻を20時に設定する。
        ret.set(Calendar.HOUR_OF_DAY, 20);
        ret.set(Calendar.MINUTE, 0);
        ret.set(Calendar.SECOND, 0);
        
        Util.d("次回起動時刻:" + ret.get(Calendar.DATE));

        return ret;
    }

    @Override
    protected void execTask()
    {
        // インスタンスを保持
        activeService = this;

        // TODO
        // ここに、通知をするか否かの条件分岐を記述する。
        boolean procflag = true;

        final Context context = getContext();
        if (procflag) {
            new AsyncTasksRunner(new ISequentialRunnable[] {

                    new SequentialAsyncTask() {
                        public boolean main() {
//                            UIUtil.longToastByHandler(getHandler(), context, "test");

                            KakeiboNotification kn = new KakeiboNotification();
                            kn.putNotice(context);
                            return true;
                        }
                    }

            })
                    .withoutDialog()
                    .whenAllTasksCompleted(new RunnerFollower() {
                        @Override
                        protected void exec() {
                            // 次回の実行について計画を立てる
                            makeNextPlan();
                            Util.d("次回起動時刻：" + getNextProcTime().getTime());
                        }
                    })
                    .begin();
        }
    }

    @Override
    public void makeNextPlan()
    {
        this.scheduleNextTime();
    }

    /**
     * もし起動していたら，サービスの常駐を解除する。 既に起動済みのタスクがある場合，タスクは中断されない。
     */
    public static void stopResidentIfActive(Context context) {
        if (activeService != null)
        {
            activeService.stopResident(context);
        }
    }

}
