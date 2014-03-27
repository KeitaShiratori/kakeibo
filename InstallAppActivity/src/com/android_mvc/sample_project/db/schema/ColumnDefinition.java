package com.android_mvc.sample_project.db.schema;

public final class ColumnDefinition {

    public class CostDetailCol {
        public static final String ID = "id";
        public static final String CATEGORY_TYPE = "category_type";
        public static final String PAY_TYPE = "pay_type";
        public static final String BUDGET_YMD = "budget_ymd";
        public static final String BUDGET_COST = "budget_cost";
        public static final String SETTLE_COST = "settle_cost";
        public static final String DIVIDE_NUM = "divide_num";
    }

    public class IncomeDetailCol {
        public static final String ID = "id";
        public static final String CATEGORY_TYPE = "category_type";
        public static final String PAY_TYPE = "pay_type";
        public static final String BUDGET_YMD = "budget_ymd";
        public static final String BUDGET_INCOME = "budget_income";
        public static final String SETTLE_INCOME = "settle_income";
    }

    public class MyWalletCol {
        public static final String ID = "id";
        public static final String YMD = "ymd";
        public static final String KINGAKU = "kingaku";
        public static final String ZANDAKA = "zandaka";
        public static final String HIKIDASHI = "hikidashi";
        
        
    }

    public class AccountBookCol {
        public static final String ID = "id";
        public static final String MOKUHYOU_KINGAKU = "mokuhyou_kingaku";
        public static final String MOKUHYOU_KIKAN = "mokuhyou_kikan";
        public static final String START_DATE = "start_date";
    }

    public class AccountBookDetailCol {
        public static final String ID = "id";
        public static final String MOKUHYOU_MONTH_KINGAKU = "mokuhyou_month_kingaku";
        public static final String MOKUHYOU_MONTH = "mokuhyou_month";
        public static final String AUTO_INPUT_FLAG = "auto_input_flag";
        public static final String SIME_FLAG = "sime_flag";
    }

    public class AccountSheetCol {
        public static final String ID = "id";
        public static final String YMD = "ymd";
    }
    public class AccountSheetCellCol {
        public static final String ID = "id";
        public static final String YMD = "ymd";
        public static final String BUDGET_KINGAKU = "budget_kingaku";
        public static final String SETTLE_KINGAKU = "settle_kingaku";
    }
    public class AccountListCol {
        public static final String ID = "id";
        public static final String YMD = "ymd";
        public static final String BUDGET_KINGAKU = "budget_kingaku";
        public static final String SETTLE_KINGAKU = "settle_kingaku";
    }
    public class CategoryTypeCol {
        public static final String ID = "id";
        public static final String CATEGORY_TYPE_NAME = "category_type_name";
        public static final String PARENT_CATEGORY_TYPE = "parent_category_type";
    }

    public class PayTypeCol {
        public static final String ID = "id";
        public static final String PAY_TYPE_NAME = "pay_type_name";
    }
    
    public class CreditCardSettingCol { 
        public static final String ID = "id";
        public static final String SIME_YMD = "sime_ymd";
        public static final String SIHARAI_YMD = "siharai_ymd";
        
    }
}