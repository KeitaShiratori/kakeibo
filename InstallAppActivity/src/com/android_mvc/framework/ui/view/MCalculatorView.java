package com.android_mvc.framework.ui.view;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android_mvc.framework.ui.view.etc.Calculater;
import com.android_mvc.sample_project.R;

public class MCalculatorView extends MLinearLayout implements TextWatcher {

    protected static final String TAG = MCalculatorView.class.getSimpleName();

    // 結果表示用のTextView
    private TextView mTextView1;
    private TextView mTextView2;
    // 計算機
    Calculater mCalculater;

    /**
     * コンストラクタ
     * 
     * @param context
     *            親のコンテキストをセット
     */
    public MCalculatorView(Context context) {
        super(context);
        init(null);
    }

    /**
     * コンストラクタ
     * 
     * @param context
     *            親のコンテキストをセット
     * @param attrs
     *            外部(XML)から取り込むアトリビュートをセット
     */
    public MCalculatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MCalculatorView(Context context, AttributeSet attrs, Integer initVal) {
        super(context, attrs);
        init(attrs, initVal);
    }

    public void init(AttributeSet attrs) {
        init(attrs, 0);
    }

    public void init(AttributeSet attrs, Integer initVal) {
        Context context = getContext();
        mCalculater = new Calculater(initVal);

        android.support.v7.widget.GridLayout layout = new GridLayout(context);
        layout.setColumnCount(4);
        layout.setRowCount(6);
        layout.setBackgroundResource(R.drawable.calc_background);
        layout.setPadding(10, 10, 10, 10);

        // 画面のインスタンスを取得
        // 0行目
        mTextView1 = new MTextView(context)
                .backgroundResource(R.drawable.disp_background)
                .gravity(Gravity.RIGHT)
                .paddingPx(5)
                .text(Integer.toString(initVal))
                .textsize(36);

        GridLayout.LayoutParams params00 = new GridLayout.LayoutParams();
        params00.columnSpec = GridLayout.spec(0, 4);
        params00.rowSpec = GridLayout.spec(0);
        params00.setGravity(Gravity.FILL_HORIZONTAL);

        mTextView1.setLayoutParams(params00);
        layout.addView(mTextView1);

        mTextView2 = new MTextView(context)
                .gravity(Gravity.TOP | Gravity.LEFT)
                .paddingPx(5)
                .text("")
                .textsize(18);

        GridLayout.LayoutParams params99 = new GridLayout.LayoutParams();
        params99.columnSpec = GridLayout.spec(0, 4);
        params99.rowSpec = GridLayout.spec(0);
        params99.setGravity(Gravity.FILL_HORIZONTAL);

        mTextView2.setLayoutParams(params99);
        layout.addView(mTextView2);

        // 1行目（空白）
        Space space = new Space(context);
        GridLayout.LayoutParams spaceParams = new GridLayout.LayoutParams();
        spaceParams.height = 50;
        spaceParams.columnSpec = GridLayout.spec(0);
        spaceParams.rowSpec = GridLayout.spec(1);
        spaceParams.setGravity(Gravity.FILL_HORIZONTAL);
        space.setLayoutParams(spaceParams);
        layout.addView(space);

        // 2行目
        MButton bt01 = new MButton(context)
                .text("7")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params01 = new GridLayout.LayoutParams();
        params01.columnSpec = GridLayout.spec(0);
        params01.rowSpec = GridLayout.spec(2);
        bt01.setLayoutParams(params01);
        layout.addView(bt01);

        MButton bt02 = new MButton(context)
                .text("8")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params02 = new GridLayout.LayoutParams();
        params02.columnSpec = GridLayout.spec(1);
        params02.rowSpec = GridLayout.spec(2);
        bt02.setLayoutParams(params02);
        layout.addView(bt02);

        MButton bt03 = new MButton(context)
                .text("9")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params03 = new GridLayout.LayoutParams();
        params03.columnSpec = GridLayout.spec(2);
        params03.rowSpec = GridLayout.spec(2);
        bt03.setLayoutParams(params03);
        layout.addView(bt03);

        MButton bt04 = new MButton(context)
                .text("×")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params04 = new GridLayout.LayoutParams();
        params04.columnSpec = GridLayout.spec(3);
        params04.rowSpec = GridLayout.spec(2);
        bt04.setLayoutParams(params04);
        layout.addView(bt04);

        // 3行目
        MButton bt05 = new MButton(context)
                .text("4")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params05 = new GridLayout.LayoutParams();
        params05.columnSpec = GridLayout.spec(0);
        params05.rowSpec = GridLayout.spec(3);
        bt05.setLayoutParams(params05);
        layout.addView(bt05);

        MButton bt06 = new MButton(context)
                .text("5")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params06 = new GridLayout.LayoutParams();
        params06.columnSpec = GridLayout.spec(1);
        params06.rowSpec = GridLayout.spec(3);
        bt06.setLayoutParams(params06);
        layout.addView(bt06);

        MButton bt07 = new MButton(context)
                .text("6")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params07 = new GridLayout.LayoutParams();
        params07.columnSpec = GridLayout.spec(2);
        params07.rowSpec = GridLayout.spec(3);
        bt07.setLayoutParams(params07);
        layout.addView(bt07);

        MButton bt08 = new MButton(context)
                .text("－")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params08 = new GridLayout.LayoutParams();
        params08.columnSpec = GridLayout.spec(3);
        params08.rowSpec = GridLayout.spec(3);
        bt08.setLayoutParams(params08);
        layout.addView(bt08);

        // 4行目
        MButton bt09 = new MButton(context)
                .text("1")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params09 = new GridLayout.LayoutParams();
        params09.columnSpec = GridLayout.spec(0);
        params09.rowSpec = GridLayout.spec(4);
        bt09.setLayoutParams(params09);
        layout.addView(bt09);

        MButton bt10 = new MButton(context)
                .text("2")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params10 = new GridLayout.LayoutParams();
        params10.columnSpec = GridLayout.spec(1);
        params10.rowSpec = GridLayout.spec(4);
        bt10.setLayoutParams(params10);
        layout.addView(bt10);

        MButton bt11 = new MButton(context)
                .text("3")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params11 = new GridLayout.LayoutParams();
        params11.columnSpec = GridLayout.spec(2);
        params11.rowSpec = GridLayout.spec(4);
        bt11.setLayoutParams(params11);
        layout.addView(bt11);

        MButton bt12 = new MButton(context)
                .text("＋")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params12 = new GridLayout.LayoutParams();
        params12.columnSpec = GridLayout.spec(3);
        params12.rowSpec = GridLayout.spec(4);
        bt12.setLayoutParams(params12);
        layout.addView(bt12);

        // 5行目
        MButton bt13 = new MButton(context)
                .text("←")
                .backgroundResource(R.drawable.button_design)
                .click(clear());
        GridLayout.LayoutParams params13 = new GridLayout.LayoutParams();
        params13.columnSpec = GridLayout.spec(0);
        params13.rowSpec = GridLayout.spec(5);
        bt13.setLayoutParams(params13);
        layout.addView(bt13);

        MButton bt14 = new MButton(context)
                .text("0")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params14 = new GridLayout.LayoutParams();
        params14.columnSpec = GridLayout.spec(1);
        params14.rowSpec = GridLayout.spec(5);
        bt14.setLayoutParams(params14);
        layout.addView(bt14);

        MButton bt15 = new MButton(context)
                .text("00")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params15 = new GridLayout.LayoutParams();
        params15.columnSpec = GridLayout.spec(2);
        params15.rowSpec = GridLayout.spec(5);
        bt15.setLayoutParams(params15);
        layout.addView(bt15);

        MButton bt16 = new MButton(context)
                .text("＝")
                .backgroundResource(R.drawable.button_design)
                .click(click(mCalculater));
        GridLayout.LayoutParams params16 = new GridLayout.LayoutParams();
        params16.columnSpec = GridLayout.spec(3);
        params16.rowSpec = GridLayout.spec(5);
        bt16.setLayoutParams(params16);
        layout.addView(bt16);

        this.add(layout);

    }

    private OnClickListener clear() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "1文字クリア");
                String[] dispText = mCalculater.reset();
                if (dispText != null) {
                    mTextView1.setText(dispText[0]);
                    mTextView2.setText(dispText[1]);
                }
            }
        };
    }

    private OnClickListener click(final Calculater c) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                MButton btn = (MButton) v;
                String input = btn.getText().toString();
                Log.d(TAG, "input=" + input);

                String[] dispText = c.putInput(input);
                if (dispText != null) {
                    mTextView1.setText(dispText[0]);
                    mTextView2.setText(dispText[1]);
                }
            }

        };
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        Log.d(TAG, "input=" + input);
        if (input.length() > 0) {
            String[] dispText = mCalculater.putInput(input);
            if (dispText != null) {
                mTextView1.setText(dispText[0]);
                mTextView2.setText(dispText[1]);
            }
            s.clear();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public CharSequence getValue() {
        String[] dispText = mCalculater.putInput("=");
        if (dispText != null) {
            mTextView1.setText(dispText[0]);
            mTextView2.setText(dispText[1]);
        }
        return this.mTextView1.getText();
    }

}
