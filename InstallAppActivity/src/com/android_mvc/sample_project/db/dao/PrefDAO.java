package com.android_mvc.sample_project.db.dao;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.dao.BasePrefDAO;
import com.android_mvc.sample_project.db.entity.lib.LPUtil;

/**
 * アプリのプリファレンスを扱うクラス。
 * 
 * @author id:language_and_engineering
 * 
 */
public class PrefDAO extends BasePrefDAO
{
    // ----------------- 最終更新日 -----------------

    /**
     * 最終更新日を更新
     */
    public void updateLastUpdateYMD(Context context, Calendar c)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString("LastUpdateYMD", LPUtil.encodeCalendarToTextYMD(c));
        editor.commit();

        FWUtil.d("最終更新日を変更：" + c);
    }

    /**
     * 最終更新日を取得
     */
    public Calendar getLastUpdateYMD(Context context)
    {
        SharedPreferences settings = getSettings(context);
        String temp = settings.getString("LastUpdateYMD", LPUtil.encodeCalendarToText(Calendar.getInstance()));
        Calendar ret = LPUtil.decodeTextToCalendarYMD(temp);
        FWUtil.d("最終更新日は" + ret);
        return ret;
    }

    // ----------------- その他 -----------------

}
