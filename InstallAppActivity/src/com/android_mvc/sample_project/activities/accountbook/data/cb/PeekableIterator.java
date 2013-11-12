package com.android_mvc.sample_project.activities.accountbook.data.cb;

import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

/** peek() を持つ Iterator。
 * Meyer の言うことは正しい。Iterator は OOP 的にはあまり良くない設計だ。
 * このパッケージでの利用としては、{@link com.android_mvc.sample_project.activities.accountbook.data.cb.PeekableIteratorBuilder} が任意の java.util.List を受け取って、PeekableIterator を implements した内部クラスを返すことを想定しているが、単にこれを implements したオブジェクトを構築して使う利用法もあるだろう。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public interface PeekableIterator<T> extends Iterator<T> {
    /** Iterator.hasNext と同様に、「次」のデータがあるか問い合わせる。
     * @return  true==次のデータあり、false==次のデータなし
     */
    public boolean hasNext();

    /** 次のデータを取得し、ポインタを一つ進ませる。
     * @return 次のデータ
     */
    public T next();
	
    /** 次のデータを取得するが、ポインタは進ませない。
     * だから、next() を呼ばない限り、同じデータを返し続ける。
     * @return 次のデータ
     */
    public T peek();

    /** 実装なしのつもり。
     */
    public void remove();
}
