package com.io.cloudonix.test.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.io.cloudonix.test.R;
import com.io.cloudonix.test.ui.fragment.main.MainFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}