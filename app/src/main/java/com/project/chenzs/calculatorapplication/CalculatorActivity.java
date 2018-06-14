package com.project.chenzs.calculatorapplication;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    private Boolean curCleared = false;
    private float oldTextSize = -1;
    private int optStatus = -1;
    private String optPreString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        initView();
    }

    private void initView() {
        final EditText currentText = findViewById(R.id.currentText);
        final TextView historyText = findViewById(R.id.history);
        final TextView calcResultText = findViewById(R.id.calcResult);
        final Button clearBtn = findViewById(R.id.clear);
        RelativeLayout numberGroup = findViewById(R.id.numberGroup);

        historyText.setMovementMethod(new ScrollingMovementMethod());
        historyText.addTextChangedListener(new MyTextWatcher());
        historyText.setLineSpacing(-15, 1.0f);
        currentText.setText("0");
        oldTextSize = currentText.getTextSize() / currentText.getPaint().density;

        //处理数值按钮的点击事件
        for (int i = 0; i < numberGroup.getChildCount(); i++) {
            final Button btn = (Button) numberGroup.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrate();
                    if (curCleared) {//如果清除按钮点击过了，需要还原它的文本
                        clearBtn.setText(getString(R.string.opt_c));
                    }
                    if (btn.getId() == R.id.separator) {
                        if (!currentText.getText().toString().contains(btn.getText())) {
                            addCurrentText(currentText, btn.getText().toString());
                        }
                    } else {
                        addCurrentText(currentText, btn.getText().toString());
                    }
                }
            });
        }

        //清除按钮的事件
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                if (curCleared) {
                    historyText.setText("");
                    calcResultText.setText("");
                    optPreString = null;
                    optStatus = -1;
                } else {
                    currentText.setText("0");
                    currentText.setTextSize(oldTextSize);
                    clearBtn.setText(R.string.opt_ac);
                    curCleared = true;
                }
            }
        });

        //Del按钮的事件
        Button delBtn = findViewById(R.id.delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                deleteCurrentText(currentText);
            }
        });

        //divide按钮的事件
        Button divideBtn = findViewById(R.id.divide);
        divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                doOperation(R.id.divide);
            }
        });
        //multiply按钮的事件
        Button multiplyBtn = findViewById(R.id.multiply);
        multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                doOperation(R.id.multiply);
            }
        });
        //minus按钮的事件
        Button minusBtn = findViewById(R.id.minus);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                doOperation(R.id.minus);
            }
        });
        //plus按钮的事件
        Button plusBtn = findViewById(R.id.plus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                doOperation(R.id.plus);
            }
        });
        //equal按钮的事件
        Button equalBtn = findViewById(R.id.equal);
        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                doEqualOpt();
            }
        });
    }


    //滚动History部分的文本，使其显示到最后一行
    private void scrollHistoryText(TextView textView) {
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        } else if (textView.getText().length() == 0) {
            textView.scrollTo(0, 0);
        }
    }

    /**
     * 添加当前输入的字符
     *
     * @param currentText 当前输入字符的输入框
     * @param text        当前输入的字符
     */
    private void addCurrentText(EditText currentText, String text) {
        curCleared = false;
        if (currentText.getText().toString().equals("0")
                && !text.equals(getString(R.string.num_separator))) {
            currentText.setText("");
        }
        String curText = currentText.getText().toString();
        String newText = curText.concat(text);
        float textWidth = currentText.getPaint().measureText(newText);
        if (textWidth >= (currentText.getWidth()
                - currentText.getPaddingStart() - currentText.getPaddingEnd())) {
            float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
            float textSize = currentText.getTextSize() / scaledDensity;
            currentText.setTextSize(textSize - 2);
        }
        currentText.append(text);
    }

    /**
     * 删除当前输入框中的最后一个字符
     *
     * @param currentText 当前输入框
     */
    private void deleteCurrentText(EditText currentText) {
        String curText = currentText.getText().toString();
        if (curText.length() > 1) {
            currentText.getText().delete(curText.length() - 1, curText.length());
        } else {
            currentText.setText("0");
        }
        curText = currentText.getText().toString();
        float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
        float textSize = currentText.getTextSize() / scaledDensity;
        if (textSize < oldTextSize) {
            currentText.setTextSize(textSize + 2);
        }
        float textWidth = currentText.getPaint().measureText(curText);
        if (textWidth > (currentText.getWidth()
                - currentText.getPaddingStart() - currentText.getPaddingEnd())) {
            currentText.setTextSize(textSize - 2);
        }
    }

    /**
     * 加减乘除操作按钮点击事件
     *
     * @param opt 操作状态值
     */
    private void doOperation(int opt) {
        TextView currentText = findViewById(R.id.currentText);
        TextView historyText = findViewById(R.id.history);
        TextView calcResultText = findViewById(R.id.calcResult);
        historyText.append(currentText.getText().toString() + "\n");
        if (optPreString == null) {
            optPreString = currentText.getText().toString();
        }
        switch (opt) {
            case R.id.divide:
                historyText.append("÷\n");
                break;
            case R.id.multiply:
                historyText.append("×\n");
                break;
            case R.id.minus:
                historyText.append("-\n");
                break;
            case R.id.plus:
                historyText.append("+\n");
                break;
        }
        if (optStatus != -1) {
            optPreString = calc(optPreString,currentText.getText().toString(),optStatus);
            calcResultText.setText(" = " + optPreString);
            if(optPreString.equals("Infinity")){
                optPreString = "0";
            }
        }
        optStatus = opt;
        currentText.setTextSize(oldTextSize);
        currentText.setText("0");
    }

    /**
     * 等于按钮点击事件
     */
    private void doEqualOpt() {
        if (optPreString == null && optStatus == -1) return;
        TextView currentText = findViewById(R.id.currentText);
        TextView historyText = findViewById(R.id.history);
        TextView calcResultText = findViewById(R.id.calcResult);
        String calcResult = calc(optPreString, currentText.getText().toString(), optStatus);
        historyText.append(currentText.getText().toString() + "\n");
        historyText.append("=\n" + calcResult + "\n");
        historyText.append("------------------------------------\n");
        currentText.setText(calcResult);
        calcResultText.setText("");
        optPreString = null;
        optStatus = -1;
    }

    private String calc(String pre,String cur,int opt){
        float preValue = Float.parseFloat(pre);
        float curValue = Float.parseFloat(cur);
        String result = "";
        switch (opt) {
            case R.id.divide:
                result = "" + (preValue / curValue);
                break;
            case R.id.multiply:
                result = "" + (preValue * curValue);
                break;
            case R.id.minus:
                result = "" + (preValue - curValue);
                break;
            case R.id.plus:
                result = "" + (preValue + curValue);
        }
        return result;
    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null)
            vibrator.vibrate(50);
    }

    /**
     * 文本监听，主要是注册给History，监听其文本变化后，滚动到最后一行
     */
    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            scrollHistoryText((TextView) findViewById(R.id.history));
        }
    }
}
