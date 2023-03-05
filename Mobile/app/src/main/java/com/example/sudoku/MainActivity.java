package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    CustomButton clickedCustomButton = null;
    CustomButton[][] buttons = new CustomButton[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TableRow 의 레이아웃 속성 설정
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams (
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f
        );
        tableRowParams.setMargins(10, 10, 10, 10);

        //**************************************************************************//

        //넘버패드 가져오기
        TableLayout numberPad = (TableLayout) findViewById(R.id.numberPad);
        Button[][] padButtons = new Button[5][4];

        int index = 1;
        for (int i = 0; i < 5; i++) {
            TableRow tableRow = new TableRow(this);

            //0번째 줄
            if (i == 0) {
                TextView textView = new TextView(this);
                textView.setText("Input Number");
                textView.setLayoutParams(tableRowParams);
                tableRow.addView(textView);
            } else if (i != 4) {
                for (int j = 0; j < 3; j++, index++) {
                    padButtons[i - 1][j] = new Button(this);
                    padButtons[i - 1][j].setLayoutParams(tableRowParams);
                    padButtons[i - 1][j].setText(String.valueOf(index));
                    padButtons[i - 1][j].setBackgroundColor(Color.parseColor("#7401DF"));
                    padButtons[i - 1][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button clickedNum = (Button) view;
                            String number = (String) clickedNum.getText();
                            clickedCustomButton.set(Integer.parseInt(number));
                            clickedCustomButton.memo.setVisibility(View.INVISIBLE);
                            numberPad.setVisibility(View.INVISIBLE);
                            checkNumber(clickedCustomButton);
                        }
                    });
                    tableRow.addView(padButtons[i - 1][j]);
                }
            } else {
                padButtons[4][0] = new Button(this);
                padButtons[4][0].setText("CANCEL");
                padButtons[4][0].setBackgroundColor(Color.parseColor("#7401DF"));
                padButtons[4][0].setLayoutParams(tableRowParams);
                padButtons[4][0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numberPad.setVisibility(View.INVISIBLE);
                    }
                });

                padButtons[4][1] = new Button(this);
                padButtons[4][1].setText("DEL");
                padButtons[4][1].setBackgroundColor(Color.parseColor("#7401DF"));
                padButtons[4][1].setLayoutParams(tableRowParams);
                padButtons[4][1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedCustomButton.set(0);
                        numberPad.setVisibility(View.INVISIBLE);
                        clickedCustomButton.unsetConflict();
                    }
                });

                tableRow.addView(padButtons[4][0]);
                tableRow.addView(padButtons[4][1]);
            }

            numberPad.addView(tableRow);
            numberPad.setVisibility(View.INVISIBLE);
        }

        //**************************************************************************//

        //메모패드 가져오기
        TableLayout memoPad = (TableLayout) findViewById(R.id.memoPad);
        ToggleButton[] memoButtons = new ToggleButton[9];
        Button[] memoMenus = new Button[3];

        int memoIndex = 1;
        for (int i = 0; i < 5; i++) {
            TableRow tableRow = new TableRow(this);

            //0번째 줄
            if (i == 0) {
                TextView textView = new TextView(this);
                textView.setText("Memo");
                textView.setLayoutParams(tableRowParams);
                tableRow.addView(textView);
            } else if (i != 4) {
                for (int j = 0; j < 3; j++, memoIndex++) {
                    memoButtons[memoIndex-1] = new ToggleButton(this);
                    memoButtons[memoIndex-1].setLayoutParams(tableRowParams);
                    memoButtons[memoIndex-1].setText(String.valueOf(memoIndex));
                    memoButtons[memoIndex-1].setTextOn(String.valueOf(memoIndex));
                    memoButtons[memoIndex-1].setTextOff(String.valueOf(memoIndex));

                    tableRow.addView(memoButtons[memoIndex-1]);
                }
            } else {
                for(int j = 0; j < 3; j++) {
                    memoMenus[j] = new Button(this);
                    memoMenus[j].setLayoutParams(tableRowParams);
                    memoMenus[j].setBackgroundColor(Color.parseColor("#00ff0000"));
                    memoMenus[j].setTextColor(Color.parseColor("#7401DF"));
                    tableRow.addView(memoMenus[j]);
                }
                memoMenus[0].setText("DELETE");
                memoMenus[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedCustomButton.memo.setVisibility(View.INVISIBLE);
                        memoPad.setVisibility(View.INVISIBLE);
                    }
                });

                memoMenus[1].setText("CANCEL");
                memoMenus[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        memoPad.setVisibility(View.INVISIBLE);
                    }
                });

                memoMenus[2].setText("OK");
                memoMenus[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean[] memoCheck = new Boolean[9];
                        for(int j = 0; j < 9; j++)
                            if(memoButtons[j].isChecked()) memoCheck[j] = true;
                            else memoCheck[j] = false;
                        clickedCustomButton.setMemo(memoCheck);
                        memoPad.setVisibility(View.INVISIBLE);
                    }
                });

            }

            memoPad.addView(tableRow);
            memoPad.setVisibility(View.INVISIBLE);
        }

        //**************************************************************************//

        //보드 생성
        BoardGenerator boardGenerator = new BoardGenerator();

        //테이블_레이아웃 가져오기
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams (
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1.0f
        );

        //테이블_로우 * 9
        for (int col = 0; col < 9; col++) {
            //테이블_로우 생성 및 배치
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableParams);
            tableLayout.addView(tableRow);

            //버튼 * 9*9
            for (int row = 0; row < 9; row++) {
                //버튼 생성 및 레이아웃 속성 추가
                buttons[col][row] = new CustomButton(this, row, col);
                buttons[col][row].setLayoutParams(tableRowParams);

                int buttonNumber = boardGenerator.get(col, row);
                //25%는 비우기
                if ((Math.random() * (100 - 1 + 1) + 1) > 25) {
                    //버튼 텍스트(숫자) 설정
                    buttons[col][row].set(buttonNumber);
                } else {
                    buttons[col][row].set(0);
                    buttons[col][row].value = buttonNumber;
                    buttons[col][row].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickedCustomButton = (CustomButton) view;
                            numberPad.setVisibility(View.VISIBLE);
                        }
                    });
                    buttons[col][row].setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            clickedCustomButton = (CustomButton) view;
                            memoPad.setVisibility(View.VISIBLE);
                            return true;
                        }
                    });
                }
                //버튼 배치
                tableRow.addView(buttons[col][row]);
            }
        }

    }

    public int checkNumber(CustomButton changeButton) {
        String number = String.valueOf(changeButton.value);
        int row = changeButton.row;
        int col = changeButton.col;
        Toast.makeText(this, row+ ", " + col, Toast.LENGTH_SHORT).show();

        for (int c = 0; c < 9; c++) {
            if (c != col && (String) buttons[c][row].textView.getText() == number) {
                changeButton.setConflict();
                return 0;
            }
        }

        for (int r = 0; r < 9; r++) {
            if (r != row && (String) buttons[col][r].textView.getText() == number) {
                changeButton.setConflict();
                return 0;
            }
        }

        int minRow;
        int maxRow;
        if (row >= 0 && row <= 2) {
            minRow = 0;
            maxRow = 2;
        } else if (row <= 5) {
            minRow = 3;
            maxRow = 5;
        } else {
            minRow = 6;
            maxRow = 8;
        }
        int minCol;
        int maxCol;
        if (col >= 0 && col <= 2) {
            minCol = 0;
            maxCol = 2;
        } else if (col <= 5) {
            minCol = 3;
            maxCol = 5;
        } else {
            minCol = 6;
            maxCol = 8;
        }
        for (int r = minRow; r <= maxRow; r++) {
            for (int c = minCol; c <= maxCol; c++) {
                if (c != col && r != row && (String) buttons[c][r].textView.getText() == number) {
                    changeButton.setConflict();
                    return 0;
                }
            }
        }

        changeButton.unsetConflict();
        return 1;
    }

}