package com.rinish.factorfactor.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rinish.factorfactor.R;
import com.rinish.factorfactor.Utils.Stats;
import com.rinish.factorfactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;

    private enum Screen {
        MAIN,
        STATS,
        TYPE,
        MODE,
        DIFFICULTY
    }

    private ActivityMainBinding binding;

    private View root;

    private boolean mVisible;

    private Button manualTypeButton;
    private Button classicTypeButton;
    private Button challengeModeButton;
    private Button practiceModeButton;
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    private ImageButton backButton;

    private int highestScore = 0;
    private float fastestResponse = 0;
    private int longestStreak = 0;

    private Screen currentScreen = Screen.MAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        root = binding.getRoot();
        setContentView(root);

        mVisible = true;
        root.setOnClickListener(view -> toggle());

        styleButton(binding.playButton, getResources().getColor(R.color.button_gradient));
        styleButton(binding.statsButton, getResources().getColor(R.color.button_gradient));

        binding.statsButton.setOnClickListener(v -> transitionTo(Screen.STATS));

        binding.aboutButton.setOnClickListener(v -> {
            View messageView = getLayoutInflater().inflate(R.layout.dialog_about,
                    findViewById(R.id.about_dialog_root), false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.app_name);
            builder.setView(messageView);
            builder.create();
            builder.show();
        });

        updateStats();

        init();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide();
    }

    private void init() {

        binding.playButton.setOnClickListener(v -> transitionTo(Screen.TYPE));

        manualTypeButton = createButton("MANUAL");
        manualTypeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("TYPE", "MANUAL");
            startActivity(intent);
        });

        classicTypeButton = createButton("CLASSIC");
        classicTypeButton.setOnClickListener(v -> transitionTo(Screen.MODE));

        challengeModeButton = createButton("CHALLENGE");
        challengeModeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("TYPE", "CHALLENGE");
            startActivity(intent);
        });

        practiceModeButton = createButton("PRACTICE");
        practiceModeButton.setOnClickListener(v -> transitionTo(Screen.DIFFICULTY));

        easyButton = createButton("EASY");
        easyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("TYPE", "PRACTICE");
            intent.putExtra("DIFFICULTY", "EASY");
            startActivity(intent);
        });

        mediumButton = createButton("MEDIUM");
        mediumButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("TYPE", "PRACTICE");
            intent.putExtra("DIFFICULTY", "MEDIUM");
            startActivity(intent);
        });

        hardButton = createButton("HARD");
        hardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("TYPE", "PRACTICE");
            intent.putExtra("DIFFICULTY", "HARD");
            startActivity(intent);
        });

        LinearLayout.LayoutParams backButtonLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        backButtonLayoutParams.setMargins(0, px(20f), 0, 0);
        backButtonLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        backButton = new ImageButton(this);
        backButton.setBackgroundColor(0x00ffffff);
        backButton.setLayoutParams(backButtonLayoutParams);
        backButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        backButton.setOnClickListener(v -> navigateBackward());

    }

    private void updateStats() {
        highestScore = Stats.getHighestScore(this);
        fastestResponse = Stats.getFastestResponse(this);
        longestStreak = Stats.getLongestStreak(this);
    }

    private void navigateBackward() {
        switch (currentScreen) {
            case STATS:
                binding.statsLayoutView.removeView(backButton);
            case TYPE:
                transitionTo(Screen.MAIN);
                break;
            case MODE:
                transitionTo(Screen.TYPE);
                break;
            case DIFFICULTY:
                transitionTo(Screen.MODE);
                break;
        }
    }

    public int px(float dp) {
        return (int) (dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void transitionTo(Screen screen) {
        switch (screen) {
            case MAIN:
                binding.statsScreen.setVisibility(View.GONE);
                binding.customScreen.setVisibility(View.GONE);
                binding.buttonContainer.removeAllViews();
                binding.mainScreen.setVisibility(View.VISIBLE);
                currentScreen = Screen.MAIN;
                break;
            case STATS:
                binding.mainScreen.setVisibility(View.GONE);
                updateStats();
                binding.highestScoreColumn.setText(String.valueOf(highestScore));
                if (fastestResponse != Float.MAX_VALUE) {
                    binding.fastestColumn.setText(getResources()
                            .getString(R.string.current_fastest, fastestResponse));
                } else {
                    binding.fastestColumn.setText("∞");
                }
                binding.longestStreakColumn.setText(String.valueOf(longestStreak));
                binding.statsScreen.setVisibility(View.VISIBLE);
                binding.statsLayoutView.addView(backButton);
                currentScreen = Screen.STATS;
                break;
            case TYPE:
                binding.mainScreen.setVisibility(View.GONE);
                binding.customScreen.setVisibility(View.GONE);
                binding.buttonContainer.removeAllViews();
                binding.buttonContainer.addView(manualTypeButton);
                binding.buttonContainer.addView(classicTypeButton);
                binding.buttonContainer.addView(backButton);
                binding.customScreen.setVisibility(View.VISIBLE);
                currentScreen = Screen.TYPE;
                break;
            case MODE:
                binding.mainScreen.setVisibility(View.GONE);
                binding.customScreen.setVisibility(View.GONE);
                binding.buttonContainer.removeAllViews();
                binding.buttonContainer.addView(challengeModeButton);
                binding.buttonContainer.addView(practiceModeButton);
                binding.buttonContainer.addView(backButton);
                binding.customScreen.setVisibility(View.VISIBLE);
                currentScreen = Screen.MODE;
                break;
            case DIFFICULTY:
                binding.mainScreen.setVisibility(View.GONE);
                binding.customScreen.setVisibility(View.GONE);
                binding.buttonContainer.removeAllViews();
                binding.buttonContainer.addView(easyButton);
                binding.buttonContainer.addView(mediumButton);
                binding.buttonContainer.addView(hardButton);
                binding.buttonContainer.addView(backButton);
                binding.customScreen.setVisibility(View.VISIBLE);
                currentScreen = Screen.DIFFICULTY;
                break;
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayout.setMargins(0, 0, 0, px(20));
        button.setLayoutParams(buttonLayout);
        button.setTextSize(20);
        styleButton(button, getResources().getColor(R.color.button_gradient));
        return button;
    }

    private void styleButton(Button button, int gradientColor) {
        button.setBackgroundResource(R.drawable.button);
        GradientDrawable drawable = (GradientDrawable) button.getBackground();
        drawable.mutate();
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setGradientRadius(150f);
        drawable.setColors(new int[]{0xffffffff, 0xffffffff, gradientColor});
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = () -> {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = this::hide;

    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }

}
