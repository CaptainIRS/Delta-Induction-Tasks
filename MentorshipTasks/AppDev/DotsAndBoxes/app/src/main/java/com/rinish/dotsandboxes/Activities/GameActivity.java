package com.rinish.dotsandboxes.Activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.rinish.dotsandboxes.R;
import com.rinish.dotsandboxes.databinding.ActivityGameBinding;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    private int noOfRows;
    private int noOfColumns;
    private int noOfPlayers;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noOfRows = getIntent().getIntExtra("ROWS", 3);
        noOfColumns = getIntent().getIntExtra("COLUMNS", 3);
        noOfPlayers = getIntent().getIntExtra("PLAYERS", 2);

        binding.gameBoard.initialize(noOfRows, noOfColumns, noOfPlayers);

        binding.undoButton.setOnClickListener(v -> {
            binding.gameBoard.popPath();
            updateScores();
            binding.gameBoard.invalidate();
        });

        binding.gameBoard.setOnTouchListener((v, event) -> {
            updateScores();
            return false;
        });

        binding.colorButton.setOnClickListener(v -> {
            binding.gameBoard.changeLineColors();
            binding.gameBoard.invalidate();
            updateScores();
        });

        binding.clearButton.setOnClickListener(v -> {
            binding.gameBoard.initialize(noOfRows, noOfColumns, noOfPlayers);
            binding.gameBoard.invalidate();
            updateScores();
        });

        updateScores();

    }

    private void updateScores() {
        ArrayList<Pair<Integer, Integer>> scores = binding.gameBoard.getScores();
        binding.scoreContainer.removeAllViews();
        for (int i = 0; i < scores.size(); i++) {
            Pair<Integer, Integer> pair = scores.get(i);
            TextView textView = scoreTextView(pair.first, pair.second);
            if (binding.gameBoard.getCurrentPlayer() == i + 1) {
                textView.setBackground(getResources().getDrawable(R.drawable.rounded));
                Drawable drawable = textView.getBackground();
                DrawableCompat.setTint(drawable, getAlphaColor(pair.second));
            }
            binding.scoreContainer.addView(textView);
        }
    }

    private int getAlphaColor(int color) {
        return Color.argb(60, Color.red(color), Color.green(color), Color.blue(color));
    }

    private TextView scoreTextView(int score, int color) {
        TextView scoreTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            scoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        scoreTextView.setLayoutParams(layoutParams);
        scoreTextView.setText(String.valueOf(score));
        scoreTextView.setTextColor(color);
        scoreTextView.setTextSize(40);

        return scoreTextView;
    }
}