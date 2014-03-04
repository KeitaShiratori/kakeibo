package com.android_mvc.framework.activities.accountlist;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.common.AbstractAppSettings;
import com.android_mvc.framework.db.dao.FWPrefDAO;
import com.android_mvc.framework.db.dao.IFWDAO;
import com.android_mvc.framework.db.DBHelper;
import com.android_mvc.framework.task.AsyncTasksRunner;
import com.android_mvc.framework.task.SequentialAsyncTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * アプリ初期化アクティビティの基底クラス
 * 
 * @author id:language_and_engineering
 * 
 */
public abstract class AccountListAppFWBaseActivity extends BaseNormalActivity
{

    /**
     * 端末内に格納したFWのメタ情報にアクセスするオブジェクト
     */
    private IFWDAO fwDAO = new FWPrefDAO();

    /**
     * アプリを動かすためのクラス関係などをFW側で初期化。 簡易DIみたいなもの。アプリ側からFW側に情報を注入する。
     */
    protected abstract void injectAppInfoIntoFW();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // NOTE:基底クラスのonCreate処理が呼ばれるよりも前に，
        // ロガーのタグ等を初期化しておく必要がある。
        injectAppInfoIntoFW();

        super.onCreate(savedInstanceState);

    }


    /**
     * アプリ起動時に，AP側の設定情報を受け取り，FW側に注入・初期化する。
     */
    protected void receiveAppInfoAsFW(AbstractAppSettings settings)
    {
        FWUtil.receiveAppInfoAsFW(settings);
    }

}