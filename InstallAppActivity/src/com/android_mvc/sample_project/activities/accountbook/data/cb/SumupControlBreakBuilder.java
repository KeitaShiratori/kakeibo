package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.Stack;

/** コントロールブレーク＋合計を行うビルダ。<br/>
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakBuilder} の上書き例として作成した。フックはこういう風に書く、という例である。<br/>
 * 対象List のオブジェクトは {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue} を実装する必要がある。<br/>
 * 戻り値の実装クラスは {@link com.android_mvc.sample_project.activities.accountbook.data.cb.SumupResultGroup} に固定されている。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class SumupControlBreakBuilder extends ControlBreakBuilder {
    private Class sumupResultClass = SumupResultGroup.class;
	
    /** {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ResultGroup} のインスタンスを生成する。このクラスでは、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.SumupResultGroup} で固定されている。
     * @param key レベルキー（==null ならトップレベル）
     * @return  生成オブジェクト（SumupResultGroup固定）
     */
    protected final ResultGroup getResultInstance(Object key) throws Exception {	ResultGroup ret= (ResultGroup)sumupResultClass.newInstance();
	ret.setKey( key );
	return ret;
    }

    /**
     * 新しいレベルに入った時に呼ばれるフック。onEnd() と対応している。
     * このクラスでは、自身のレベルの総計の初期化を行う。こんな感じである。
     * <pre>
     * protected void onLevel( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
     *	  SumupResultGroup at = (SumupResultGroup)resultStack.peek();
     *	  at.setTotal( 0 );
     * }
     * </pre>
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     */
    protected void onLevel( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
	SumupResultGroup at = (SumupResultGroup)resultStack.peek();
	at.setTotal( 0 );
    }

    /**
     * データを追加する時に呼ばれるフック。<br/>
     * このクラスでは、対象オブジェクト（cbk）の値を取得し、階層を遡ってその値を合計に追加する。だから、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue} を対象オブジェクトとする。実装はこんな感じ。
     * <pre>
     *  protected Object onAdd( int level, PeekableIterator i,
     *                          Stack<ResultGroup> resultStack,
     *			        ControlBreakKey cbk ) {
     *	  int value  = ((ControlBreakKeyAndValue)cbk).getValue();
     *	  for( int j = 0; j < level; j++ ) {
     *	    SumupResultGroup at = (SumupResultGroup)resultStack.get(j);
     *	    at.addTotal( value );
     *	  }
     *	  return cbk;
     *  }
     * </pre>
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     * @param cbk  追加すべきオブジェクト
     * @return 追加されるオブジェクト（==cbk）
     */
    protected Object onAdd( int level, PeekableIterator i, Stack<ResultGroup> resultStack,
			  ControlBreakKey cbk ) {
	int value  = ((ControlBreakKeyAndValue)cbk).getValue();
	for( int j = 0; j < level; j++ ) {
	    SumupResultGroup at = (SumupResultGroup)resultStack.get(j);
	    at.addTotal( value );
	}
	return cbk;
    }

    /**
     * 現在のレベルから抜ける時に呼ばれるフック。onLevel() と対応している。
     * このクラスでは何もしない。
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     */
    protected void onBreak( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
    }
}
