package com.android_mvc.sample_project.db.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android_mvc.framework.db.schema.AbstractSchemaDefinition;
import com.android_mvc.framework.db.schema.RDBColumn;
import com.android_mvc.framework.db.schema.RDBTable;
import com.android_mvc.sample_project.db.entity.AccountBook;
import com.android_mvc.sample_project.db.entity.AccountBookDetail;
import com.android_mvc.sample_project.db.entity.AccountSheet;
import com.android_mvc.sample_project.db.entity.AccountSheetCell;
import com.android_mvc.sample_project.db.entity.CategoryType;
import com.android_mvc.sample_project.db.entity.CostDetail;
import com.android_mvc.sample_project.db.entity.CreditCardSetting;
import com.android_mvc.sample_project.db.entity.IncomeDetail;
import com.android_mvc.sample_project.db.entity.MyWallet;
import com.android_mvc.sample_project.db.entity.PayType;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountBookDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCellCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.AccountSheetCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CategoryTypeCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CostDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.CreditCardSettingCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.IncomeDetailCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.MyWalletCol;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.PayTypeCol;

/**
 * AP側のテーブル構造と初期データを定義。
 * 
 * @author id:language_and_engineering
 * 
 */
public class SchemaDefinition extends AbstractSchemaDefinition
{
    // NOTE:
    // ・各テーブルの主キーは「id」。
    // ・SQLiteのカラムで定義可能な型については，下記を参照
    // http://www.sqlite.org/datatype3.html
    // ・アプリのインストール（初期化）アクティビティから呼び出される。

    @Override
    public void define_tables()
    {
        // 変動費明細テーブル
        new RDBTable(this)
                .nameIs(new CostDetail().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(CostDetailCol.ID).primaryKey(),
                        new RDBColumn().nameIs(CostDetailCol.CATEGORY_TYPE).comment("カテゴリ種別").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(CostDetailCol.PAY_TYPE).comment("支払種別").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(CostDetailCol.BUDGET_YMD).comment("予算年月日").typeIs("text").notNull(),
                        new RDBColumn().nameIs(CostDetailCol.BUDGET_COST).comment("予算費用").typeIs("text").notNull(),
                        new RDBColumn().nameIs(CostDetailCol.SETTLE_COST).comment("決算費用").typeIs("text")
                })
                .create();

        // 収入明細テーブル
        new RDBTable(this)
                .nameIs(new IncomeDetail().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(IncomeDetailCol.ID).primaryKey(),
                        new RDBColumn().nameIs(IncomeDetailCol.CATEGORY_TYPE).comment("カテゴリ種別").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(IncomeDetailCol.PAY_TYPE).comment("支払種別").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(IncomeDetailCol.BUDGET_YMD).comment("予算年月日").typeIs("text").notNull(),
                        new RDBColumn().nameIs(IncomeDetailCol.BUDGET_INCOME).comment("予算収入").typeIs("text").notNull(),
                        new RDBColumn().nameIs(IncomeDetailCol.SETTLE_INCOME).comment("決算収入").typeIs("text"),
                })
                .create();

        // 財布の中身テーブル
        new RDBTable(this)
                .nameIs(new MyWallet().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(MyWalletCol.ID).primaryKey(),
                        new RDBColumn().nameIs(MyWalletCol.YMD).comment("年月日").typeIs("text").notNull(),
                        new RDBColumn().nameIs(MyWalletCol.KINGAKU).comment("金額").typeIs("text"),
                        new RDBColumn().nameIs(MyWalletCol.ZANDAKA).comment("残高").typeIs("text"),
                        new RDBColumn().nameIs(MyWalletCol.HIKIDASHI).comment("引き出し").typeIs("text")
                })
                .create();

        // 家計簿テーブル
        new RDBTable(this)
                .nameIs(new AccountBook().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(AccountBookCol.ID).primaryKey(),
                        new RDBColumn().nameIs(AccountBookCol.MOKUHYOU_KINGAKU).comment("目標金額").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(AccountBookCol.MOKUHYOU_KIKAN).comment("目標期間").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(AccountBookCol.START_DATE).comment("開始日").typeIs("string").notNull(),
                })
                .create();

        // 家計簿明細テーブル
        new RDBTable(this)
                .nameIs(new AccountBookDetail().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(AccountBookDetailCol.ID).primaryKey(),
                        new RDBColumn().nameIs(AccountBookDetailCol.MOKUHYOU_MONTH_KINGAKU).comment("目標月金額").typeIs("integer").notNull(),
                        new RDBColumn().nameIs(AccountBookDetailCol.MOKUHYOU_MONTH).comment("目標月").typeIs("string").notNull(),
                        new RDBColumn().nameIs(AccountBookDetailCol.AUTO_INPUT_FLAG).comment("自動入力フラグ").typeIs("integer")
                })
                .create();

        new RDBTable(this)
                .nameIs(new CategoryType().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(CategoryTypeCol.ID).primaryKey(),
                        new RDBColumn().nameIs(CategoryTypeCol.CATEGORY_TYPE_NAME).comment("カテゴリ種別名").typeIs("string").notNull(),
                        new RDBColumn().nameIs(CategoryTypeCol.PARENT_CATEGORY_TYPE).comment("親カテゴリ種別").typeIs("integer")
                })
                .create();

        new RDBTable(this)
                .nameIs(new PayType().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(PayTypeCol.ID).primaryKey(),
                        new RDBColumn().nameIs(PayTypeCol.PAY_TYPE_NAME).comment("支払方法種別名").typeIs("string").notNull(),
                })
                .create();

    }

    @Override
    public void init_db_data(SQLiteDatabase db, Context context)
    {
        /*
         * マスタ系テーブルに必要なデータを投入する。
         */
        // カテゴリ種別テーブル
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (1, '食費','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (2, '日用雑貨','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (3, '交通費','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (4, '交際費','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (5, 'エンタメ','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (6, '教育・教養','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (7, '美容・服飾','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (8, '医療・保険','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (9, '通信','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (10, '水道・光熱','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (11, '住まい','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (12, '税金','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (13, '大型出費','0' );");
        db.execSQL("insert into category_type (id, category_type_name, parent_category_type) values (14, 'その他','0' );");

        // 支払方法種別テーブル
        db.execSQL("insert into pay_type (id, pay_type_name) values (1, '現金' );");
        db.execSQL("insert into pay_type (id, pay_type_name) values (2, 'クレジット' );");

    }

    public void onUpgrade2() {
        // 貯金シート
        new RDBTable(this)
                .nameIs(new AccountSheet().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(AccountSheetCol.ID).primaryKey(),
                        new RDBColumn().nameIs(AccountSheetCol.YMD).typeIs("text")
                })
                .create();

        // 貯金シートセル
        new RDBTable(this)
                .nameIs(new AccountSheetCell().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(AccountSheetCellCol.ID).primaryKey(),
                        new RDBColumn().nameIs(AccountSheetCellCol.YMD).typeIs("text").notNull(),
                        new RDBColumn().nameIs(AccountSheetCellCol.BUDGET_KINGAKU).typeIs("integer"),
                        new RDBColumn().nameIs(AccountSheetCellCol.SETTLE_KINGAKU).typeIs("integer")
                })
                .create();
    }

    public void onUpgrade3(SQLiteDatabase db) {
        // クレジットカード設定
        new RDBTable(this)
                .nameIs(new CreditCardSetting().tableName())
                .columns(new RDBColumn[] {
                        new RDBColumn().nameIs(CreditCardSettingCol.ID).primaryKey(),
                        new RDBColumn().nameIs(CreditCardSettingCol.SIME_YMD).typeIs("text"),
                        new RDBColumn().nameIs(CreditCardSettingCol.SIHARAI_YMD).typeIs("text")
                })
                .create();

        db.execSQL("alter table cost_detail add column divide_num integer;");
        db.execSQL("update cost_detail set divide_num = 1 where pay_type = 2;");
    }

    public void onUpgrade4(SQLiteDatabase db) {
        db.execSQL("alter table "
                + new AccountBookDetail().tableName()
                + " add column "
                + AccountBookDetailCol.SIME_FLAG
                + " integer;");

        db.execSQL("update "
                + new AccountBookDetail().tableName()
                + " set "
                + AccountBookDetailCol.SIME_FLAG
                + " = 0");

        db.execSQL("insert into "
                + new CategoryType().tableName()
                + " (id, category_type_name, parent_category_type) values (15, '繰り越し','9' );");
    }
}
