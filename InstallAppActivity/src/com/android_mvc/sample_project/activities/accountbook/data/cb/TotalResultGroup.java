package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.ArrayList;

/**
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakBuilder} の build() の戻り値となるオブジェクトの、合計付きの実装クラス。これを ControlBreakBuilder#setResultClass() で指定した場合、toString() で表示する時に子要素を合計して階層の最後に総計行として出力する例である。toString() 時に合計するので、効率は良くない。{@link com.android_mvc.sample_project.activities.accountbook.data.cb.TotalControlBreakBuilder} でこれを指定すべきであり、対象クラスとしては、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue} 用に書かれている。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class TotalResultGroup extends DefaultResultGroup {
    /** その階層以下の総計を計算する。
     * @return 合計	
     */
    public int getTotal() {
	List ch = getChilds();
	int total = 0;
	for( int i = 0; i < ch.size(); i++ ) {
	    if( ch.get(i) instanceof TotalResultGroup ) {
		TotalResultGroup at = (TotalResultGroup)ch.get(i);
		total += at.getTotal();
	    } else if( ch.get(i) instanceof ControlBreakKeyAndValue ) {
		ControlBreakKeyAndValue at = (ControlBreakKeyAndValue)ch.get(i);
		total += at.getValue();
	    }
	}
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
	    if( childs.get(i) instanceof TotalResultGroup ) {
		sb.append( childs.get(i).toString() );
	    } else {
		sb.append( childs.get(i).toString() + "\n" );
	    }
	}
	sb.append( getKey() + " total: " + getTotal() + "\n" );
	return sb.toString();
    }
}
