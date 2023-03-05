package com.example.sudoku;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

//Record your position (row, col) and the value of the button (value)
//Add TextView to FrameLayout to show the value
//Clickable
//Set Selector to change color when clicked
//Change the text of the TextView when selecting a number
//Change the background color in case of collision with other numbers


public class CustomButton  extends FrameLayout {

    int row;
    int col;
    int value;
    TextView textView;

    LayoutInflater layoutInflater;
    TableLayout memo;
    TextView[] memos;

    public CustomButton(@NonNull Context context, int row, int col)
    {
        super(context);

        this.row = row;
        this.col = col;

        textView = new TextView(context);
       textView.setGravity(Gravity.CENTER);
        addView(textView);
        setBackgroundResource(R.drawable.button_selector);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memo = (TableLayout)  layoutInflater.inflate(R.layout.layout_memo, null);
        addView(memo);
        memo.setVisibility(View.INVISIBLE);

        memos = new TextView[9];
        int k = 0;
        for(int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for(int j = 0; j < 3; j++, k++) {
                memos[k] = (TextView) tableRow.getChildAt(j);
            }
        }
    }

    public void set(int a)
    {
        if(a == 0)
            this.textView.setText("");
        else
            this.textView.setText(Integer.toString(a));
        value = a;
    }

    public void setConflict() { setBackgroundColor(Color.RED); }
    public void unsetConflict() { setBackgroundColor(Color.WHITE); }

    public void setMemo(Boolean[] memoCheck)
    {
        memo.setVisibility(View.VISIBLE);
        for(int i = 0; i < 9; i++) {
            if(memoCheck[i] == false)
                memos[i].setVisibility(View.INVISIBLE);
            else
                memos[i].setVisibility(View.VISIBLE);
        }

    }
}
