package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.Stack;

/** コントロールブレークを行う汎用ビルダ
 * キーを指定するメソッドを備えた {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKey} interface を implements したオブジェクトの List を引数として build(List) に渡すと、キーに対応した Composite な {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ResultGroup} オブジェクトを構築して返す。<br/>
 * 戻り値の実装クラスは、setResultClass(Class) で与える。もし、これを呼ばずに build() を呼べば、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.DefaultResultGroup} で構築する。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class ControlBreakBuilder {
    private boolean debug = false;
    /** デバッグ用途。true がセットされればフックを呼ぶ時にデバッグ出力をする。
     * @param debug  デバッグフラグ
     */
    public void setDebug(boolean debug) { this.debug = debug; }

    private Class resultClass = DefaultResultGroup.class;
    /** {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ResultGroup} を implements したクラスを与え、結果戻り値の Composite の実装クラスを指定する。
     * @param clazz 実装クラス
     */
    public void setResultClass( Class clazz ) {
	resultClass = clazz;
    }

    /** setResultClass() で与えられた {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ResultGroup} implements クラスから、インスタンスを生成する。上書き用フックである。<br/>
     *  特に独自の例外は投げないが、Class.newInstance() を呼んでオブジェクトを生成する都合上、java.lang.Exception を投げることにしている。
     * @param key レベルキー（==null ならトップレベル）
     * @return  生成オブジェクト
     */
    protected ResultGroup getResultInstance(Object key) throws Exception {
	ResultGroup ret= (ResultGroup)resultClass.newInstance();
	ret.setKey( key );
	return ret;
    }

    /** 引数 List のデータを、階層化して返す。<br/>
     *  特に独自の例外は投げないが、戻り値オブジェクトを生成するのに、getResultInstance() を呼び、
     *  その中で Class.newInstance() が呼ばれてオブジェクトを生成する都合上、java.lang.Exception を投げることにしている。
     * @param in 構築対象の List
     * @return  階層化された結果
     */
    public ResultGroup build( List in ) throws Exception {
	PeekableIterator iterator = new PeekableIteratorBuilder().build( in );
	ResultGroup top = getResultInstance(null);
	Stack<ResultGroup> resultStack = new Stack<ResultGroup>();
	resultStack.add( top );
	onLevel( 0, iterator, resultStack );
	while( iterator.hasNext() ) {
	    proc( 1, iterator, resultStack );
	}
	onBreak( 0, iterator, resultStack );
	return top;
    }

    private void proc( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) throws Exception {
	ControlBreakKey at = (ControlBreakKey)i.peek();
	Object saveKey = at.getLeveledKey( level );
	List childs = resultStack.peek().getChilds();
	if( saveKey == null ) {
	    i.next(); // ここで読み捨てて進ませる
	    Object o = onAdd( level, i, resultStack, at );
	    if( o != null ) {
		childs.add( o );
	    }
	    return;
	}

	ResultGroup newGroup = getResultInstance( saveKey );
	resultStack.push( newGroup );
	onLevel( level, i, resultStack );

	try {
	    while( true ) {
		proc( level + 1, i, resultStack );
		if( i.hasNext() ) {
		    ControlBreakKey cbk = (ControlBreakKey)i.peek();
		    if( checkKeys( level, cbk, resultStack ) == false ) { 
			return;
		    }
		} else {
		    return;
		}
	    }
	} finally {
	    onBreak( level, i, resultStack );
	    if( newGroup.getChilds().size() > 0 ) {
		childs.add( newGroup );
	    }
	    resultStack.pop();
	}
    }

    private boolean checkKeys( int level, ControlBreakKey cbk, Stack<ResultGroup> resultStack ) {
	for( int i = level; i > 0; i-- ) {
	    Object key = cbk.getLeveledKey( i );
	    ResultGroup at = resultStack.get(i);
	    if( at.getKey().equals(key) == false ) {
		return false;
	    }
	}
	return true;
    }

    /**
     *  階層キーを表示用に置換する。
     * @param resultStack  現在呼ばれている階層のスタック
     * @return  表示用階層名
     */
    protected String getKeys( Stack<ResultGroup> resultStack ) {
	String ret = "";
	String delim = "";
	for( int i = 1; i < resultStack.size(); i++ ) {
	    ret += delim + resultStack.get(i).getKey().toString();
	    delim = ">";
	}
	return ret;
    }

    /**
     * 新しいレベルに入った時に呼ばれるフック。onEnd() と対応している。
     * デフォルトでは何もしないが、setDebug(true) の影響を受ける。
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     */
    protected void onLevel( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
	if( debug ) {
	    String pop = getKeys( resultStack );
	    System.out.println( "onLevel(" + level + "): key=" + pop );
	}
    }

    /**
     * データを追加する時に呼ばれるフック。<br/>
     * デフォルトでは引数 cbk をそのまま返す。setDebug(true) の影響を受ける。
     * だから、ここで「追加すべきオブジェクト」を本当の追加すべきオブジェクトに編集することができる。もし、戻り値が null ならば、追加は行われない。
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     * @param cbk  追加すべきオブジェクト
     * @return 追加されるオブジェクト
     */
    protected Object onAdd( int level, PeekableIterator i, Stack<ResultGroup> resultStack,
			  ControlBreakKey cbk ) {
	if( debug ) {
	    System.out.println( "onAdd(" + level + "):" + cbk.toString() );
	}
	return cbk;
    }

    /**
     * 現在のレベルから抜ける時に呼ばれるフック。onLevel() と対応している。
     * デフォルトでは何もしないが、setDebug(true) の影響を受ける。
     * @param level  階層レベル
     * @param i 未処理データの Iterator
     * @param resultStack  現在呼ばれている階層のスタック
     */
    protected void onBreak( int level, PeekableIterator i, Stack<ResultGroup> resultStack ) {
	if( debug ) {
	    String pop = getKeys( resultStack );
	    System.out.println( "onBreak(" + level + "): key=" + pop );
	}
    }
}
