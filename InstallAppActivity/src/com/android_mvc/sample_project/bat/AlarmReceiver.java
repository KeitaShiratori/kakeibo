package com.android_mvc.sample_project.bat;

import com.android_mvc.framework.common.FWUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {  
    
    @Override  
    public void onReceive(Context context, Intent intent) {  
//        Toast.makeText(context, "Alarm Received!", Toast.LENGTH_SHORT).show();  
        Log.d("AlarmReceiver", "Alarm Received! : " + intent.getIntExtra(Intent.EXTRA_ALARM_COUNT, 0));  

        // 次の通知予約を実施
        FWUtil.setAlarmManager(context, intent);

        try{
            new PeriodicService().startResident(context);
            
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }  
}  
