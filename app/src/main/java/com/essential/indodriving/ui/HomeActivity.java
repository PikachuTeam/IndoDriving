package com.essential.indodriving.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.essential.indodriving.R;

public class HomeActivity extends AppCompatActivity {

    private Button buttonLearn;
    private Button buttonTest;
    public final static int LEARN_BUTTON=1, TEST_BUTTON=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();
        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"Aloha",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("button",LEARN_BUTTON);
                startActivity(intent);
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                intent.putExtra("button",TEST_BUTTON);
                startActivity(intent);
            }
        });
    }

    private void findViews(){
        buttonLearn= (Button) findViewById(R.id.buttonLearn);
        buttonTest= (Button) findViewById(R.id.buttonTest);
    }
}
