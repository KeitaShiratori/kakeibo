package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.ArrayList;

/**
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.SumupControlBreakBuilder} の build() の戻り値となるオブジェクトの、合計付きの実装クラス。これを SumupControlBreakBuilder#setResultClass() で指定した場合、合計は SumupControlBreakBuilder のフックによって計算されるので、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.TotalResultGroup}よりも効率がよく、特に対象クラスに（こちらは）依存しない。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class SumupResultGroup extends DefaultResultGroup {
    private int total;
    /** この階層に値をセットする。初期化用途である。
     * @param v 値
     */
    public void setTotal( int v ) {
	total = v;
    }
    /** この階層に値を加える。
     * @param v 加算する値
     */
    public void addTotal( int v ) {
	total += v;
    }
    /** その階層以下の総計を計算する。
     * @return 合計	
     */
    public int getTotal() {
	return total;
    }

    /**
     * デバッグ用途に、階層構造＋階層合計行を出力する。
     * @return 表示
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	List childs = getChilds();
	for( int i = 0; i < childs.size(); i++ ) {
	    if( childs.get(i) instanceof SumupResultGroup ) {
		sb.append( childs.get(i).toString() );
	    }
	}
	Object key = getKey();
	key = (key==null) ? "総計" : key;
	sb.append( key + " total: " + getTotal() + "\n" );
	return sb.toString();
    }
}
