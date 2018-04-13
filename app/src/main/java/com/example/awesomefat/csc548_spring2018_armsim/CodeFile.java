package com.example.awesomefat.csc548_spring2018_armsim;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CodeFile
{
    public String id;
    public ArrayList<String> theCode;
    public String name;

    public CodeFile(){}

    public CodeFile(String name, ArrayList<String> theCode)
    {
        this.name = name;
        this.theCode = theCode;
        this.id = "N/A";
    }



    public void save()
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fileRef = database.getReference("files");
        if(this.id.equals("N/A"))
        {
            fileRef = fileRef.push();
            this.id = fileRef.getKey();
        }
        else
        {
            fileRef = fileRef.child(this.id);
        }
        fileRef.setValue(this);
    }
}
