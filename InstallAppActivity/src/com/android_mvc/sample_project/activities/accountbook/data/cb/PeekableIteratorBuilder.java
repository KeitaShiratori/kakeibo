package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.RandomAccess;
import java.util.Iterator;
import java.util.ListIterator;

/** PeekableIterator を構築するクラス。PeekableIterator のprivateな実装例を含む。
 * 対象リストがマーカーの java.util.RandomAccess を実装しているかどうかによって、実装クラスが変わるので、効率がいいはず。今回は Generics を使わないが、Generics 対応である。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public class PeekableIteratorBuilder<T> {
    /** {@link com.android_mvc.sample_project.activities.accountbook.data.cb.PeekableIterator} を
     *  implements した Iterator を、引数List から構築する。<br/>
     * List が java.util.RandomAccess を implements するか否かで、PeekableIterator
     * 実装クラスを切替えて、List実装のやり方による peek() の不効率を回避している。
     * @param list 対象 List 
     */
    public PeekableIterator<T> build( List<T> list ) {
	if( list instanceof RandomAccess ) {
	    return new RandomAccessPeekableIterator<T>( list );
	} else {
	    return new SequentialListPeekableIterator<T>( list );
	}
    }

    private class RandomAccessPeekableIterator<T> implements PeekableIterator<T> {
	private List<T> list;
	int size = 0;
	private int p = 0;
	private RandomAccessPeekableIterator( List<T> list ) { 
	    this.list = list;
	    p = 0;
	    size = list.size();
	}

	public boolean hasNext() {
	    if( p < size ) {
		return true;
	    } else {
		return false;
	    }
	}

	public T next() {
	    return list.get(p++);
	}
	
	public T peek() {
	    return list.get(p);
	}

	public void remove() {
	    /* NOP */
	}
    }

    private class SequentialListPeekableIterator<T> implements PeekableIterator<T> {
	private ListIterator<T> iterator;
	private SequentialListPeekableIterator( List<T> list ) { 
	    iterator = list.listIterator(0);
	}

	public boolean hasNext() {
	    return iterator.hasNext();
	}

	public T next() {
	    return iterator.next();
	}
	
	public T peek() {
	    T ret = iterator.next();
	    iterator.previous();
	    return ret;
	}

	public void remove() {
	    /* NOP */
	}
    }
}
