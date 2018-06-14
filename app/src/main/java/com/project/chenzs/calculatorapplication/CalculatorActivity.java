package com.project.chenzs.calculatorapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        final Button clearBtn = findViewById(R.id.clear);
        RelativeLayout numberGroup = findViewById(R.id.numberGroup);

        historyText.setMovementMethod(new ScrollingMovementMethod());
        currentText.setText("0");
        oldTextSize = currentText.getTextSize()/currentText.getPaint().density;

        //处理数值按钮的点击事件
        for(int i=0;i<numberGroup.getChildCount();i++){
            final Button btn = (Button)numberGroup.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(curCleared){//如果清除按钮点击过了，需要还原它的文本
                        clearBtn.setText(getString(R.string.opt_c));
                    }
                    if(btn.getId() == R.id.separator){
                        if(!currentText.getText().toString().contains(btn.getText())){
                            addCurrentText(currentText, btn.getText().toString());
                        }
                    }else {
                        addCurrentText(currentText, btn.getText().toString());
                    }
                }
            });
        }

        //清除按钮的事件
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curCleared){
                    historyText.setText("");
                }else{
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
                deleteCurrentText(currentText);
            }
        });

        //divide按钮的事件
        Button divideBtn = findViewById(R.id.divide);
        divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOperation(R.id.divide);
            }
        });
        //multiply按钮的事件
        Button multiplyBtn = findViewById(R.id.multiply);
        multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOperation(R.id.multiply);
            }
        });
        //minus按钮的事件
        Button minusBtn = findViewById(R.id.minus);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOperation(R.id.minus);
            }
        });
        //plus按钮的事件
        Button plusBtn = findViewById(R.id.plus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOperation(R.id.plus);
            }
        });
        //equal按钮的事件
        Button equalBtn = findViewById(R.id.equal);
        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEqualOpt();
            }
        });
    }

    /**
     * 添加当前输入的字符
     * @param currentText 当前输入字符的输入框
     * @param text 当前输入的字符
     */
    private void addCurrentText(EditText currentText, String text){
        curCleared = false;
        if(currentText.getText().toString().equals("0")
                &&!text.equals(getString(R.string.num_separator))){
            currentText.setText("");
        }
        String curText = currentText.getText().toString();
        String newText = curText.concat(text);
        float textWidth = currentText.getPaint().measureText(newText);
        if(textWidth >= (currentText.getWidth()
                - currentText.getPaddingStart() - currentText.getPaddingEnd())){
            float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
            float textSize = currentText.getTextSize()/scaledDensity;
            currentText.setTextSize(textSize - 2);
        }
        currentText.append(text);
    }

    /**
     * 删除当前输入框中的最后一个字符
     * @param currentText 当前输入框
     */
    private void deleteCurrentText(EditText currentText){
        String curText = currentText.getText().toString();
        if(curText.length() > 1) {
            currentText.getText().delete(curText.length() - 1, curText.length());
        }else{
            currentText.setText("0");
        }
        curText = currentText.getText().toString();
        float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
        float textSize = currentText.getTextSize()/scaledDensity;
        if(textSize < oldTextSize) {
            currentText.setTextSize(textSize + 2);
        }
        float textWidth = currentText.getPaint().measureText(curText);
        if(textWidth > currentText.getWidth()){
            currentText.setTextSize(textSize - 2);
        }
    }

    /**
     * 加减乘除操作按钮点击事件
     * @param opt 操作状态值
     */
    private void doOperation(int opt){
        optStatus = opt;
        TextView currentText = findViewById(R.id.currentText);
        TextView historyText = findViewById(R.id.history);
        historyText.append(currentText.getText().toString()+"\n");
        switch (optStatus){
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
        currentText.setTextSize(oldTextSize);
        currentText.setText("0");
    }

    /**
     * 等于按钮点击事件
     */
    private void doEqualOpt(){

    }
}
