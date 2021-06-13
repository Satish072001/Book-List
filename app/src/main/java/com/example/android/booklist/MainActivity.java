package com.example.android.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField= (EditText)findViewById(R.id.search_bar);
                String s=textField.getText().toString();
                Toast.makeText(v.getContext(),"Your search result",Toast.LENGTH_SHORT).show();
                Intent listitem= new Intent(MainActivity.this,List.class);
                listitem.putExtra("key", s); //Optional parameters
                startActivity(listitem);
            }
        });
    }
}