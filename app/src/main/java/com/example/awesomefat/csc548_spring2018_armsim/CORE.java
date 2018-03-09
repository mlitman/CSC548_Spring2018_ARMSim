package com.example.awesomefat.csc548_spring2018_armsim;

/**
 * Created by awesomefat on 3/1/18.
 */

public class CORE
{
    static int NUMBER_OF_REGISTERS = 8;
    static int NUMBER_OF_BYTES_IN_RAM = 256;
    static String[] registerNames = {"X0","X1","X2","X3","X4","X5","X6","X7"};
    static String[] registerValues = new String[NUMBER_OF_REGISTERS];
    static String[] ram = new String[NUMBER_OF_BYTES_IN_RAM];

    static int getValueOfRegister(String registerName)
    {
        for(int i = 0; i < registerNames.length; i++)
        {
            if(registerName.equalsIgnoreCase(registerNames[i]))
            {
                return Integer.parseInt(registerValues[i]);
            }
        }
        throw new RuntimeException("Register Not Found");
    }

    static boolean setValueOfRegister(String registerName, int value)
    {
        for(int i = 0; i < registerNames.length; i++)
        {
            if(registerName.equalsIgnoreCase(registerNames[i]))
            {
                registerValues[i] = "" + value;
                return true;
            }
        }
        return false;
    }

    //bit based address converted to byte based value
    static int getValueAtMemoryAddress(int address)
    {
        return Integer.parseInt(ram[address/8]);
    }

    static boolean setValueAtMemoryAddress(int address, int value)
    {
        ram[address/8] = "" + value;
        return true;
    }

    static void initializeRam()
    {
        for(int i = 0; i < ram.length; i++)
        {
            ram[i] = "0";
        }
    }

    static void initializeRegisters()
    {
        for(int i = 0; i < registerValues.length; i++)
        {
            registerValues[i] = "0";
        }
    }
}
