package com.example.awesomefat.csc548_spring2018_armsim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SaveFileActivity extends AppCompatActivity
{
    private TextView readOnlyCodeViewTV;
    private EditText fileNameET;
    private ArrayList<String> theCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file);

        this.fileNameET = (EditText)this.findViewById(R.id.fileNameET);
        this.readOnlyCodeViewTV = this.findViewById(R.id.readOnlyCodeViewTV);
        theCode = this.getIntent().getStringArrayListExtra("theCode");
        String code = "";
        for(String s : theCode)
        {
            code += s + "\n";
        }
        this.readOnlyCodeViewTV.setText(code.trim());
    }

    public void onSaveButtonPressed(View v)
    {
        if(this.fileNameET.getText().toString().length() > 0)
        {
            CodeFile cf = new CodeFile(this.fileNameET.getText().toString().trim(), this.theCode);
            cf.save();
            System.out.println("************** " + cf.id);
        }

    }

}
