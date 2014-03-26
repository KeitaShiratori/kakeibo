package com.android_mvc.framework.controller.validation;

import java.util.Calendar;

/**
 * 数値の比較検証に用いる述語クラス。
 * @author id:language_and_engineering
 *
 */
public class CalendarComparator
{
	// 演算の引数となる数値
    Calendar value;

    // 演算タイプを格納 TODO: enum
    String type_code;

    public CalendarComparator( Calendar v, String type_code )
    {
        value = v;
        this.type_code = type_code;
    }
}
