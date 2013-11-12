package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.Stack;

/** コントロールブレーク＋合計を行うビルダ。<br/>
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakBuilder} の上書き例として作成した。フックはこういう風に書く、という例である。<br/>
 * 対象List のオブジェクトは {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKeyAndValue} を実装する必要がある。<br/>
 * このクラスでは、合計表示だけを行い、積極的に戻り値を構築しない例である。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class TotalControlBreakBuilder extends ControlBreakBuilder {
    private Class sumupResultClass = SumupResultGroup.class;
	
    /** {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ResultGroup} のインスタンスを生成する。このクラスでは、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.SumupResultGroup} で固定されている。
     * <pre>
     * private Class sumupResultClass = SumupResultGroup.class;
     * protected final ResultGroup getResultInstance(Object key) throws Exception {
     *    ResultGroup ret= (ResultGroup)sumupResultClass.newInstance();
     *    ret.setKey( key );
     *    return ret;
     * }
     * </pre>
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
     *    SumupResultGroup at = (SumupResultGroup)resultStack.peek();
     *    at.setTotal( 0 );
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
     *                          ControlBreakKey cbk ) {
     *    SumupResultGroup at = (SumupResultGroup)resultStack.peek();
     *    int value  = ((ControlBreakKeyAndValue)cbk).getValue();
     *    at.addTotal( value );
     *    System.out.println( cbk );
     *    return null;
     * }
     * </pre>
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     * @param cbk  追加すべきオブジェクト
     * @return 追加されるオブジェクト（==cbk）
     */
    protected Object onAdd( int level, PeekableIterator i, Stack<ResultGroup> resultStack,
			  ControlBreakKey cbk ) {
	SumupResultGroup at = (SumupResultGroup)resultStack.peek();
	int value  = ((ControlBreakKeyAndValue)cbk).getValue();
	at.addTotal( value );
	System.out.println( cbk );
	return null;
    }

    /**
     * 現在のレベルから抜ける時に呼ばれるフック。onLevel() と対応している。<br/>
     * このクラスでは、この階層以下の合計を計算し、それを出力するのと同時に、親オブジェクトの合計に値を加える。
     * <pre>
     * protected void onBreak( int level, PeekableIterator i,
     *                         Stack<ResultGroup> resultStack ) {
     *    SumupResultGroup at = (SumupResultGroup)resultStack.peek();
     *    SumupResultGroup parent = (SumupResultGroup)resultStack.get(level - 1);
     *    parent.addTotal( at.getTotal() );
     *    System.out.println( at.getKey() + " 合計:" + at.getTotal() );
     * }
     * </pre>
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     */
    protected void onBreak( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
	SumupResultGroup at = (SumupResultGroup)resultStack.peek();
	SumupResultGroup parent = (SumupResultGroup)resultStack.get(level - 1);
	parent.addTotal( at.getTotal() );
	System.out.println( at.getKey() + " 合計:" + at.getTotal() );
    }
}
