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

    // ----------------- 画面サイズ -----------------

    /**
     * 画面サイズ（幅）を更新
     */
    public void updateDisplaySizeWidth(Context context, int width)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("DisplayWidth", width);
        editor.commit();

        FWUtil.d("画面サイズ（幅）を変更：" + width);
    }

    /**
     * 画面サイズ（幅）を取得
     */
    public int getDisplaySizeWidth(Context context)
    {
        SharedPreferences settings = getSettings(context);
        int ret = settings.getInt("DisplayWidth", 0);
        FWUtil.d("画面サイズ（幅）は" + ret);
        return ret;
    }

    /**
     * 画面サイズ（高さ）を更新
     */
    public void updateDisplaySizeHeight(Context context, int height)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt("DisplayHeight", height);
        editor.commit();

        FWUtil.d("画面サイズ（高さ）を変更：" + height);
    }

    /**
     * 画面サイズ（高さ）を取得
     */
    public int getDisplaySizeHeight(Context context)
    {
        SharedPreferences settings = getSettings(context);
        int ret = settings.getInt("DisplayHeight", 0);
        FWUtil.d("画面サイズ（高さ）は" + ret);
        return ret;
    }

    // ----------------- チュートリアル -----------------

    /**
     * 目標金額設定画面のチュートリアルフラグを更新
     */
    public void updateTutorialDoneFlagInstallComplete(Context context, boolean done)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("tutorialFlagInstallComplete", done);
        editor.commit();

        FWUtil.d("目標金額設定画面のチュートリアルフラグを変更：" + done);
    }

    /**
     * 目標金額設定画面のチュートリアルフラグを取得
     */
    public boolean getTutorialDoneFlagInstallComplete(Context context)
    {
        SharedPreferences settings = getSettings(context);
        boolean ret = settings.getBoolean("tutorialFlagInstallComplete", false);
        FWUtil.d("目標金額設定画面のチュートリアルフラグは" + ret);
        return ret;
    }
    
    /**
     * 家計簿照会画面のチュートリアルフラグを更新
     */
    public void updateTutorialDoneFlagAccountBookShow1(Context context, boolean done)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("tutorialFlagAccountBookShow1", done);
        editor.commit();

        FWUtil.d("家計簿照会画面のチュートリアルフラグを変更：" + done);
    }

    /**
     * 家計簿照会画面のチュートリアルフラグをを取得
     */
    public boolean getTutorialDoneFlagAccountBookShow1(Context context)
    {
        SharedPreferences settings = getSettings(context);
        boolean ret = settings.getBoolean("tutorialFlagAccountBookShow1", false);
        FWUtil.d("家計簿照会画面のチュートリアルフラグは" + ret);
        return ret;
    }
    
    /**
     * 家計簿照会画面のチュートリアルフラグを更新
     */
    public void updateTutorialDoneFlagAccountBookShow2(Context context, boolean done)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("tutorialFlagAccountBookShow2", done);
        editor.commit();

        FWUtil.d("家計簿照会画面のチュートリアルフラグを変更：" + done);
    }

    /**
     * 家計簿照会画面のチュートリアルフラグをを取得
     */
    public boolean getTutorialDoneFlagAccountBookShow2(Context context)
    {
        SharedPreferences settings = getSettings(context);
        boolean ret = settings.getBoolean("tutorialFlagAccountBookShow2", false);
        FWUtil.d("家計簿照会画面のチュートリアルフラグは" + ret);
        return ret;
    }

    // ----------------- その他 -----------------

}
