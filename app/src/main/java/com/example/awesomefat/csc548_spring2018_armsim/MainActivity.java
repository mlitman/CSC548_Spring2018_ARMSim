package com.example.awesomefat.csc548_spring2018_armsim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private EditText inputET;
    private ViewGroup codeList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inputET = (EditText)this.findViewById(R.id.inputET);
        this.codeList = (ViewGroup)this.findViewById(R.id.codeList);

        //empty codelist of the placeholder values
        this.codeList.removeAllViews();

        CORE.initializeRegisters();
        CORE.initializeRam();
    }

    public void addCodeButtonPressed(View v)
    {
        if(this.inputET.getText().toString().length() > 0)
        {
            TextView tv = new TextView(this);
            tv.setText(this.inputET.getText().toString());
            this.codeList.addView(tv);
            this.inputET.setText("");
        }
    }

    public void onRegistersButtonClicked(View v)
    {
        Intent i = new Intent(this, RegisterActivity.class);
        this.startActivity(i);
    }

    public void onRamButtonClicked(View v)
    {
        Intent i = new Intent(this, RamActivity.class);
        this.startActivity(i);
    }

    private void executeInstruction(String instruction)
    {
        String opCode;
        String rd, rn, rm;
        //ADD X0, X1, X2

        //Get OpCode
        int locOf1stSpace = instruction.indexOf(' ');
        opCode = instruction.substring(0, locOf1stSpace).trim();
        String rest = instruction.substring(locOf1stSpace).trim();

        if(opCode.equalsIgnoreCase("ADD") ||
                opCode.equalsIgnoreCase("ADDI") ||
                opCode.equalsIgnoreCase("SUB") ||
                opCode.equalsIgnoreCase("SUBI"))
        {
            String[] parts = rest.split(",");
            rd = parts[0].trim();
            rn = parts[1].trim();
            rm = parts[2].trim();

            int valOfRN = CORE.getValueOfRegister(rn);

            //rm might be an immediate, so try to look up the register
            //and fallback on parsing the immediate value
            int valOfRM;
            try
            {
                valOfRM = CORE.getValueOfRegister(rm);
            }
            catch(Exception e)
            {
                //parse the numeric component of #Immediate
                valOfRM = Integer.parseInt(rm.substring(1));
            }

            if(opCode.toUpperCase().contains("ADD"))
            {
                CORE.setValueOfRegister(rd, valOfRN + valOfRM);
            }
            else
            {
                CORE.setValueOfRegister(rd, valOfRN - valOfRM);
            }
        }
        else if(opCode.equalsIgnoreCase("MOVZ"))
        {
            //rest = "X2, #100"
            String[] parts = rest.split(",");
            rd = parts[0].trim();
            rm = parts[1].trim();
            CORE.setValueOfRegister(rd, Integer.parseInt(rm.substring(1)));
        }
        else if(opCode.equalsIgnoreCase("LDUR"))
        {
            //rest = X2 , [X3, #100]
            //rest = X2 ,  X3, #100
            rest = rest.replace('[', ' ');
            rest = rest.replace(']', ' ');
            String[] parts = rest.split(",");
            rd = parts[0].trim();
            rn = parts[1].trim();
            rm = parts[2].trim();
            int regVal = CORE.getValueOfRegister(rn);
            if(regVal % 8 != 0)
            {
                regVal = regVal - (regVal % 8);
                Toast.makeText(this, "Base Addresses must be multiples of 8.  Adjusted base address to: " + regVal, Toast.LENGTH_SHORT);
            }
            int offset = Integer.parseInt(rm.substring(1));
            if(offset % 8 != 0)
            {
                offset = offset - (offset % 8);
                Toast.makeText(this,"Offsets must be multiples of 8.  Adjusted offset to: " + offset, Toast.LENGTH_SHORT).show();
            }
            int memAddress = regVal + offset;
            CORE.setValueOfRegister(rd, CORE.getValueAtMemoryAddress(memAddress));
        }
        else if(opCode.equalsIgnoreCase("STUR"))
        {
            //rest = X2 , [X3, #100]
            //rest = X2 ,  X3, #100
            rest = rest.replace('[', ' ');
            rest = rest.replace(']', ' ');
            String[] parts = rest.split(",");
            rd = parts[0].trim();
            rn = parts[1].trim();
            rm = parts[2].trim();
            int regVal = CORE.getValueOfRegister(rn);
            if(regVal % 8 != 0)
            {
                regVal = regVal - (regVal % 8);
                Toast.makeText(this, "Base Addresses must be multiples of 8.  Adjusted base address to: " + regVal, Toast.LENGTH_SHORT);
            }
            int offset = Integer.parseInt(rm.substring(1));
            if(offset % 8 != 0)
            {
                offset = offset - (offset % 8);
                Toast.makeText(this,"Offsets must be multiples of 8.  Adjusted offset to: " + offset, Toast.LENGTH_SHORT).show();
            }
            int memAddress = regVal + offset;
            int valueToStore = CORE.getValueOfRegister(rd);
            CORE.setValueAtMemoryAddress(memAddress, valueToStore);
        }
    }

    public void onRemLastButtonPressed(View v)
    {
        if(this.codeList.getChildCount() > 0)
        {
            this.codeList.removeViewAt(this.codeList.getChildCount()-1);
        }
    }

    public void onClearButtonPressed(View v)
    {
        this.codeList.removeAllViews();
    }

    public void onSaveButtonPressed(View v)
    {
        if(CORE.mainActivityMode.equals("NEW"))
        {
            //goto new file save screen
            Intent i = new Intent(this, SaveFileActivity.class);
            ArrayList<String> theCode = new ArrayList<String>();
            for(int index = 0; index < this.codeList.getChildCount(); index++)
            {
                theCode.add(((TextView)this.codeList.getChildAt(index)).getText().toString().trim());
            }
            i.putExtra("theCode", theCode);
            this.startActivity(i);
        }
        else if(CORE.mainActivityMode.equals("UPDATE"))
        {
            //save this file using the same name and unique id as before
        }
    }

    public void onRunButtonClicked(View v)
    {
        //Support 2 instructions: ADD and ADDI
        //ADD assumes 2 registerValues
        //ADDI assumes 1 register and one numeric literal
        TextView temp;
        for(int i = 0; i < this.codeList.getChildCount(); i++)
        {
            temp = (TextView)this.codeList.getChildAt(i);
            this.executeInstruction(temp.getText().toString().trim());
        }
        Toast.makeText(this, "instructions complete", Toast.LENGTH_SHORT).show();

    }
}
