package com.android_mvc.framework.ui.view.etc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android_mvc.sample_project.common.Util;

public class Calculater {

    // 入力中の文字列
    String mInputNumber;
    // 初期表示の文字列または計算結果
    String mAnswer;
    // 入力中の演算子
    String mOperator;
    // 計算結果(＝画面表示内容）
    int mResult;
    // 画面表示内容
    String[] mResultText;

    public Calculater() {
        this(0);
    }

    public Calculater(Integer initialDispNumber) {
        // 入力中の文字列（計算対象）
        mInputNumber = new String();
        mAnswer = new String();
        // 入力中の演算子
        mOperator = "";
        // 計算結果
        mResult = initialDispNumber;
        // 画面表示内容（0は数値、1は演算子）
        mResultText = new String[2];

        mAnswer = Integer.toString(mResult);

        mResultText[0] = mAnswer;
        mResultText[1] = mOperator;
    }

    /***
     * パラメーターのKeyが数値ならTRUEを返却
     */
    private boolean isNumber(String key) {
        try {
            Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /***
     * パラメーターのKeyがサポートしている演算子ならTRUEを返却
     */
    private boolean isSupportedOperator(String key) {
        if (key.equals("+") || key.equals("＋")) {
            return true;
        } else if (key.equals("-") || key.equals("－")) {
            return true;
        } else if (key.equals("*") || key.equals("×")) {
            return true;
        } else if (key.equals("/") || key.equals("÷")) {
            return true;
        } else if (key.equals("=") || key.equals("＝")) {
            return true;
        }
        return false;
    }

    /***
     * 演算を実施
     */
    private void doCalculation(String ope) {
        if (mInputNumber != null && mInputNumber.length() > 0) {
            if (ope.equals("+") || ope.equals("＋")) {
                mResult = Integer.parseInt(mAnswer) + Integer.parseInt(mInputNumber.toString());
            } else if (ope.equals("-") || ope.equals("－")) {
                mResult = Integer.parseInt(mAnswer) - Integer.parseInt(mInputNumber.toString());
            } else if (ope.equals("*") || ope.equals("×")) {
                mResult = Integer.parseInt(mAnswer) * Integer.parseInt(mInputNumber.toString());
            } else if (ope.equals("/") || ope.equals("÷")) {
                mResult = Integer.parseInt(mAnswer) / Integer.parseInt(mInputNumber.toString());
            }
            mInputNumber = new String();
            mAnswer = Integer.toString(mResult);
        }
        else {

        }
    }

    /***
     * クリア処理（一文字消去）
     */
    public String[] reset() {
        if (mOperator != null && !mOperator.isEmpty()) {
            // 演算子が入力済みの場合
            if (mInputNumber.length() > 0) {
                // 入力中の数字があれば、1文字減らす。
                mInputNumber = mInputNumber.substring(0, mInputNumber.length() - 1);
                // mAnswerが0文字になった場合は、0にする。
                if (mInputNumber.length() == 0) {
                    mInputNumber = "0";
                }
                mResultText[0] = mInputNumber;
                mResultText[1] = mOperator;
            }
            else {
                // 入力中の数字がない場合、演算子をリセットする。
                mOperator = "";
                mResultText[0] = mAnswer;
                mResultText[1] = mOperator;
            }
        }
        else {
            // 演算子が入力されていない場合、結果を1文字減らす。
            if (mAnswer.length() > 0) {
                mAnswer = mAnswer.substring(0, mAnswer.length() - 1);
            }

            // mAnswerが0文字になった場合は、0にする。
            if (mAnswer.length() == 0) {
                mAnswer = "0";
            }

            mResult = Integer.parseInt(mResultText[0]);
            mResultText[0] = mAnswer;
        }
        return mResultText;
    }

    /***
     * 入力された文字をもとに処理を行い、ディスプレイに表示する結果を返却する。
     */
    public String[] putInput(String key) {
        if (isNumber(key)) {
            // 数値の場合次の入力をまつ
            if (mOperator != null && !mOperator.isEmpty()) {
                // 演算子が入力されていれば、処理対象の数値に入力値を付与する。
                mInputNumber = removeHeadZero(mInputNumber);
                mInputNumber += key;
                mResultText[0] = mInputNumber;
            }
            else {
                // 演算子が入力されていない場合は、画面表示値に入力値を付与する。
                // mResultText[0]の先頭が0の場合、取り除く。
                mAnswer = removeHeadZero(mAnswer);
                mAnswer += key;
                mResultText[0] = mAnswer;
            }
        } else if (isSupportedOperator(key)) {
            // サポートしている演算子の場合、入力中の数値をもとに演算を行う
            if (key.equals("=") || key.equals("＝")) {
                // ＝なら演算を行い結果を返却する
                if (mOperator != null && !mOperator.isEmpty()) {
                    doCalculation(mOperator);
                }
                mOperator = "";
                mResultText[0] = mAnswer;
                mResultText[1] = mOperator;
            }
            else {
                if (mOperator != null && !mOperator.isEmpty()) {
                    // 入力中の演算子があるなら前回の結果を用いて演算を行う
                    doCalculation(mOperator);
                } else if (mInputNumber.length() > 0) {
                    // はじめての演算子なら入力中の数値を設定する
                    doCalculation(mOperator);
                    mInputNumber = new String();
                }
                else{
                    mResult = Integer.parseInt(mAnswer);
                }
                mOperator = key;

                mResultText[0] = Integer.toString(mResult);
                mResultText[1] = mOperator;
            }
        } else {
            return null;
        }
        return mResultText;
    }

    /**
     * 文字列の先頭の0を削除する
     * 
     * @param string
     */
    private String removeHeadZero(String input) {
        String ret;
        Pattern reg_pattern = Pattern.compile("^0+");
        Matcher reg_matcher = reg_pattern.matcher(input);
        Util.d("replace Text is Found: " + reg_matcher.find());
        ret = reg_matcher.replaceFirst("");
        return ret;
    }

}
