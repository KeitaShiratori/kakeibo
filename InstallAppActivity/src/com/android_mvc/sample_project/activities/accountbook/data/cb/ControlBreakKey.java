package com.android_mvc.sample_project.activities.accountbook.data.cb;

/** コントロールブレークを行う汎用ビルダの対象クラスとなる interface。階層レベルのキーを getLeveledKey() として実装する。
 * @author  K.Sugiura
 * @version 1.0, 2007.8.26
 * @since   1.0
 */
public interface ControlBreakKey {
    /** 階層レベルに対応したキーを返す。<br/>
     * level == 0 ならば、トップレベルなので、これの戻り値は null でいい。<br/>
     * レベルの終了は、値として null を返せばいい。null を返すレベルが末端レベルとなり、それ以上深いレベルには入らない。
     * @param level 階層レベル
     * @return キーオブジェクト
     */
    Object getLeveledKey( int level );
}
