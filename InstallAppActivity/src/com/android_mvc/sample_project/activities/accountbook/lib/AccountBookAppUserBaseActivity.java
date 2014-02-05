package com.android_mvc.sample_project.activities.accountbook.lib;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.android_mvc.framework.activities.accountbook.AccountBookAppFWBaseActivity;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.schema.RDBSchema;
import com.android_mvc.sample_project.common.AppSettings;
import com.android_mvc.sample_project.db.schema.SchemaDefinition;

/**
 * ユーザがいじる必要のない初期化処理ロジック。
 * APとFWの橋渡しをするため，FW側に置けない部分。
 * @author id:language_and_engineering
 *
 */
public abstract class AccountBookAppUserBaseActivity extends AccountBookAppFWBaseActivity
{

    // NOTE:このクラスをActivityではなくしてロジッククラスにしてしまうと，
    // installアクティビティからロジッククラスを必ず呼び出すように規約が生じてしまう。
    // ユーザのコード量を減らしつつ，なおかつFW側に置けないコードなのでこうなる。

    @Override
    protected void injectAppInfoIntoFW()
    {
        // AP側の設定を，FW側で受理させる。
        receiveAppInfoAsFW( new AppSettings(this) );
            // NOTE: AppSettingを参照するので，このコードはFW側に置けない。
            // NOTE: これはパッケージをまたいだ参照渡しの処理だが，継承階層内で行われる。
            // したがって，本Activityは，APの顔とFWの顔という二面性を持つことになる。邪道だが面白い。

    }
    
}
