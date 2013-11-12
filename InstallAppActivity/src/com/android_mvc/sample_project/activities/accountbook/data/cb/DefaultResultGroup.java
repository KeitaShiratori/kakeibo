package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.ArrayList;

/**
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakBuilder} の build() の戻り値となるオブジェクトの単純な実装クラス。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class DefaultResultGroup implements ResultGroup {
    private Object key;

    /**
     * この階層のキーを返す。
     * @return 階層キー
     */
    public Object getKey() {
	return (key==null) ? "" : key;
    }

    /**
     * この階層のキーをセットする。
     * @param key 階層キー
     */
    public void setKey( Object key ) { this.key = key; }

    private List childs = new ArrayList();

    /**
     * 子要素の List を取得する。
     * @return 子要素の List
     */
    public List getChilds() {
	return childs;
    }


    /**
     * デバッグ用途に、階層構造を出力する。
     * @return 表示
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append( " ----- " + getKey() + " start --------\n" );
	for( int i = 0; i < childs.size(); i++ ) {
	    if( childs.get(i) instanceof DefaultResultGroup ) {
		sb.append( childs.get(i).toString() );
	    } else {
		sb.append( childs.get(i).toString() + "\n" );
	    }
	}
	sb.append( " ----- " + getKey() + " end --------\n" );
	return sb.toString();
    }
}
