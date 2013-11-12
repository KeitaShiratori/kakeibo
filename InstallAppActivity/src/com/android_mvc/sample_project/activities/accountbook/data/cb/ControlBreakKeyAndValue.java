package com.android_mvc.sample_project.activities.accountbook.data.cb;

/** コントロールブレークを行う汎用ビルダの対象クラスとなる interface {@link com.android_mvc.sample_project.activities.accountbook.data.cb.ControlBreakKey} を拡張し、合計用の数値取得メソッドを備えた interface。<br/>
 * {@link com.android_mvc.sample_project.activities.accountbook.data.cb.TotalControlBreakBuilder} と {@link com.android_mvc.sample_project.activities.accountbook.data.cb.SumupControlBreakBuilder} の build(List) の引数である対象 List は、このインターフェイスを implements する必要がある。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public interface ControlBreakKeyAndValue extends ControlBreakKey {
    /** 合計を計算するための数値を返す。<br/>
     * @return 合計される数値
     */
    int getValue();
}
