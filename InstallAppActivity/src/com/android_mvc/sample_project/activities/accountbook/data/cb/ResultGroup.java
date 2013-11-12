package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;

/**
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakBuilder} の build() の戻り値となるオブジェクトのinterface。Composite デザインパターンのシンプルな interface である。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public interface ResultGroup {
    /**
     * 子要素の List を取得する。
     * @return 子要素の List
     */
    List getChilds();

    /**
     * この階層のキーをセットする。
     * @param key 階層キー
     */

    void setKey( Object key );
    /**
     * この階層のキーを返す。
     * @return 階層キー
     */
    Object getKey();
}
