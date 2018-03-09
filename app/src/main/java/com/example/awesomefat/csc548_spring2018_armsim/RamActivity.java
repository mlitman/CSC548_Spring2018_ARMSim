package com.example.awesomefat.csc548_spring2018_armsim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RamActivity extends AppCompatActivity
{
    private EditText[] theMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        this.theMemory = new EditText[CORE.NUMBER_OF_BYTES_IN_RAM];
        ViewGroup memoryLayout = (ViewGroup)this.findViewById(R.id.memoryLayout);

        //Generates RAM visual representation
        EditText temp;
        LinearLayout row;
        TextView byteNum;
        int byteCounter = 0;
        for(int i = 0; i < CORE.NUMBER_OF_BYTES_IN_RAM; i++)
        {
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            byteNum = new TextView(this);
            byteNum.setText("     " + byteCounter + ":     ");
            byteCounter += 8;
            byteNum.setTextSize(20);
            temp = new EditText(this);
            temp.setHint("Enter a Byte");
            this.theMemory[i] = temp;

            row.addView(byteNum);
            row.addView(temp);

            memoryLayout.addView(row);
        }

        //update Memory with current values
        for(int i = 0; i < CORE.NUMBER_OF_BYTES_IN_RAM; i++)
        {
            this.theMemory[i].setText(CORE.ram[i]);
        }
    }

    public void onSaveButtonPressed(View v)
    {
        for(int i = 0; i < CORE.NUMBER_OF_BYTES_IN_RAM; i++)
        {
            CORE.ram[i] = this.theMemory[i].getText().toString();
        }
        Toast.makeText(this, "RAM Saved....", Toast.LENGTH_SHORT).show();
    }
}
