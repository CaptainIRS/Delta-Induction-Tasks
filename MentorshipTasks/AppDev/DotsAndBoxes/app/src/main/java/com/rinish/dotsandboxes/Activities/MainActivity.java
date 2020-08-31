package com.rinish.dotsandboxes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.rinish.dotsandboxes.R;
import com.rinish.dotsandboxes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int noOfRows;
    private int noOfColumns;
    private int noOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        (new Handler()).postDelayed(() -> {
            binding.splash.setVisibility(View.GONE);
            binding.mainContentRoot.setVisibility(View.VISIBLE);
        }, 2000);

        binding.rowSpinner.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                new Integer[] {3, 4, 5, 6, 7}
        ));
        binding.rowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfRows = position + 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noOfRows = 3;
            }
        });

        binding.columnSpinner.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                new Integer[] {3, 4, 5, 6, 7}
        ));
        binding.columnSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfColumns = position + 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noOfColumns = 3;
            }
        });

        binding.playersSpinner.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                new Integer[] {2, 3, 4, 5}
        ));
        binding.playersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfPlayers = position + 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                noOfPlayers = 2;
            }
        });

        binding.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("ROWS", noOfRows);
            intent.putExtra("COLUMNS", noOfColumns);
            intent.putExtra("PLAYERS", noOfPlayers);
            startActivity(intent);
        });

    }
}
