package com.example.myep1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_1).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.bt_1:
                    Toast.makeText(MainActivity.this,"Hello All",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    public void click(View v){
        switch (v.getId()){
            case R.id.bt_1:
                Toast.makeText(MainActivity.this,"Button点击事件2",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void myClick(View v){
        EditText name = (EditText)findViewById(R.id.name);
        TextView textView = (TextView)findViewById(R.id.textView);

        textView.setText("log in Success");
        name.setText("success!");

    }



}
