package com.example.weight;

import android.app.Activity;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.angel.R;
import com.example.util.DateChange;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        TextView time = (TextView)findViewById(R.id.time);
         final String str = simpleDateFormat.format(date);
        time.setText("今天是"+str);
        TextView textView = (TextView)findViewById(R.id.button_1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et1=(EditText)findViewById(R.id.edittext2);
                EditText et2=(EditText)findViewById(R.id.edittext1);
                Double height= Double.parseDouble(et1.getText().toString());
                Double weight= Double.parseDouble(et2.getText().toString());
                Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                intent.putExtra("data_height",height);
                intent.putExtra("data_weight",weight);
                startActivity(intent);
            }
        });
    }
}
