package com.alanjet.videorecordertest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SetVideoActivity extends Activity {
    private LinearLayout setClear;
    private LinearLayout setFrameRate;
    private LinearLayout setFocusMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_video);
        setClear= (LinearLayout) findViewById(R.id.ll_set_clear_density);
        setFocusMode= (LinearLayout) findViewById(R.id.ll_set_focus_mode);
        setFrameRate= (LinearLayout) findViewById(R.id.ll_set_frame_rate);
        setClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetVideoActivity.this,"待开发~",Toast.LENGTH_SHORT).show();
            }
        });
        setFocusMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetVideoActivity.this,"待开发~",Toast.LENGTH_SHORT).show();
            }
        });
        setFrameRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetVideoActivity.this,"待开发~",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
