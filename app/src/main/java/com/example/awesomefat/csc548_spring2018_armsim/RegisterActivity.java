package com.example.awesomefat.csc548_spring2018_armsim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity
{
    private EditText[] theRegisters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.theRegisters = new EditText[CORE.NUMBER_OF_REGISTERS];
        this.theRegisters[0] = (EditText)this.findViewById(R.id.X0ET);
        this.theRegisters[1] = (EditText)this.findViewById(R.id.X1ET);
        this.theRegisters[2] = (EditText)this.findViewById(R.id.X2ET);
        this.theRegisters[3] = (EditText)this.findViewById(R.id.X3ET);
        this.theRegisters[4] = (EditText)this.findViewById(R.id.X4ET);
        this.theRegisters[5] = (EditText)this.findViewById(R.id.X5ET);
        this.theRegisters[6] = (EditText)this.findViewById(R.id.X6ET);
        this.theRegisters[7] = (EditText)this.findViewById(R.id.X7ET);

        //fill our registerValues visually from CORE
        for(int i = 0; i < CORE.NUMBER_OF_REGISTERS; i++)
        {
            this.theRegisters[i].setText(CORE.registerValues[i]);
        }
    }

    public void saveButtonPressed(View v)
    {
        for(int i = 0; i < CORE.NUMBER_OF_REGISTERS; i++)
        {
            CORE.registerValues[i] = this.theRegisters[i].getText().toString();
        }
        Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
    }
}
