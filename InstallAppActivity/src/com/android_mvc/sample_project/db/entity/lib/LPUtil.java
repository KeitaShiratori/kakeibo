package com.android_mvc.sample_project.db.entity.lib;

import com.android_mvc.framework.db.entity.BaseLPUtil;

/**
 * ORMのLP変換を補助するカスタムロジック集。
 * @author id:language_and_engineering
 *
 */
public class LPUtil extends BaseLPUtil
{
    // 必要に応じて追加する
	
	//　実績金額など、DB必須項目ではない数値型の変換（Integer⇒text）
	public static String encodeIntegerToText(Integer i){
		return i.toString();
	}
	
	// 実績金額など、DB必須項目ではない数値型の変換（text⇒Integer）
	public static Integer decodeTextToInteger(String s){
		Integer ret = null;
		if(!s.isEmpty()){
			ret = Integer.parseInt(s);
		}
		return ret;
	}
}
