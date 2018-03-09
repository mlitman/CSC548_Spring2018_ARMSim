package com.example.awesomefat.csc548_spring2018_armsim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText inputET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.inputET = (EditText)this.findViewById(R.id.inputET);

        CORE.initializeRegisters();
        CORE.initializeRam();
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

    public void onRunButtonClicked(View v)
    {
        //Support 2 instructions: ADD and ADDI
        //ADD assumes 2 registerValues
        //ADDI assumes 1 register and one numeric literal
        String instruction = this.inputET.getText().toString().trim();
        System.out.println(instruction);

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
        Toast.makeText(this, opCode + " complete", Toast.LENGTH_SHORT).show();

    }
}
