package com.rinish.factorfactor.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.rinish.factorfactor.Models.Difficulty;
import com.rinish.factorfactor.Models.Question;
import com.rinish.factorfactor.R;
import com.rinish.factorfactor.Utils.Stats;
import com.rinish.factorfactor.databinding.ActivityGameBinding;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;

    private enum Type {
        MANUAL,
        CHALLENGE,
        PRACTICE,
    }

    private static final String LOG_TAG = "GameActivity";

    private ActivityGameBinding binding;

    private View root;
    private boolean mVisible;

    private Type type;
    private CountDownTimer countDownTimer;
    private Intent intent;
    private float fastestTime;
    private float currentTime;
    private int streak = 0;
    private int longestStreak = 0;
    private int questionNo = 0;
    private int livesRemaining;
    private int score = 0;

    private int overallHighScore = 0;
    private float overallFastestResponse = 0;
    private int overallLongestStreak = 0;

    private Question question;
    private ValueAnimator angleAnimator;
    private ValueAnimator hueAnimator;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        root = binding.getRoot();

        setContentView(root);

        basicInit();
        resetCounters();
    }

    private void fetchStats() {
        overallHighScore = Stats.getHighestScore(this);
        overallFastestResponse = Stats.getFastestResponse(this);
        overallLongestStreak = Stats.getLongestStreak(this);
    }

    private void basicInit() {
        int orientation = this.getResources().getConfiguration().orientation;
        setLayout(orientation);

        mVisible = true;
        root.setOnClickListener(view -> toggle());

        intent = getIntent();
        type = Type.valueOf(Objects.requireNonNull(intent.getExtras()).getString("TYPE"));

        binding.nextButton.setOnClickListener(v -> {
            if (type == Type.CHALLENGE && livesRemaining == 0) {
                gameOver();
            } else {
                GameActivity.this.nextQuestion();
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        binding.streakCountTextView.setText(String.valueOf(streak));
        binding.gameLayoutRoot.setBackgroundColor(Color.WHITE);

        binding.continueButton.setOnClickListener(v -> {
            binding.highScoreTextView.setVisibility(View.GONE);
            binding.scoreLayoutView.setVisibility(View.GONE);
            resetCounters();
            nextQuestion();
            binding.gameLayoutRoot.setVisibility(View.VISIBLE);
        });
        binding.closeButton.setOnClickListener(v -> GameActivity.this.finish());

        if (type == Type.MANUAL) {
            initManualEntry();
        } else {
            init();
        }
    }

    private void gameOver() {
        binding.gameLayoutRoot.setVisibility(View.GONE);

        binding.finalScoreTextView.setText(String.valueOf(score));
        binding.fastestColumn.setText(getResources().getString(R.string.current_fastest, fastestTime));
        binding.longestStreakColumn.setText(String.valueOf(longestStreak));

        if (score > overallHighScore) {
            binding.highScoreTextView.setVisibility(View.VISIBLE);
            Stats.setHighestScore(score, this);
        }
        if (fastestTime < overallFastestResponse) {
            Stats.setFastestResponse(fastestTime, this);
        }
        if (longestStreak > overallLongestStreak) {
            Stats.setLongestStreak(longestStreak, this);
        }
        binding.scoreLayoutView.setVisibility(View.VISIBLE);
    }

    private void resetCounters() {
        currentTime = fastestTime = Float.MAX_VALUE;
        streak = longestStreak = 0;
        livesRemaining = 5;
        score = 0;
        questionNo = 0;

        fetchStats();

        if (type == Type.CHALLENGE) {
            binding.lifeTextView.setText(String.valueOf(livesRemaining));
            binding.scoreTextView.setText(String.valueOf(score));
        } else {
            binding.lifeTextView.setText("∞");
        }
    }

    private void setLayout(int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                if (binding.contentsLinearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
                    binding.contentsLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    binding.questionContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            1
                    ));
                    binding.optionContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            1
                    ));
                    binding.verticalSeparatorView.setVisibility(View.GONE);
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                if (binding.contentsLinearLayout.getOrientation() == LinearLayout.VERTICAL) {
                    binding.contentsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    binding.questionContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1
                    );
                    params.gravity = Gravity.CENTER_VERTICAL;
                    binding.optionContainer.setLayoutParams(params);
                    binding.verticalSeparatorView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void nextQuestion() {
        binding.wrongTextView.setVisibility(View.GONE);
        binding.nextButton.setVisibility(View.GONE);
        binding.opt1Button.setClickable(true);
        binding.opt2Button.setClickable(true);
        binding.opt3Button.setClickable(true);
        styleButton(binding.opt1Button, getResources().getColor(R.color.button_gradient));
        styleButton(binding.opt2Button, getResources().getColor(R.color.button_gradient));
        styleButton(binding.opt3Button, getResources().getColor(R.color.button_gradient));
        if (type == Type.MANUAL) {
            binding.optionContainer.setVisibility(View.INVISIBLE);
            binding.questionEditText.setText("");
            binding.questionEditText.requestFocus();
        } else {
            init();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        setLayout(newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide();
    }

    private void initManualEntry() {
        binding.questionTextView.setVisibility(View.GONE);
        binding.questionEditText.setVisibility(View.VISIBLE);

        binding.questionEditText.requestFocus();

        binding.questionEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                int input;
                try {
                    input = Integer.parseInt(binding.questionEditText.getText().toString());
                    if (input < 10) {
                        GameActivity.this.showAlert("Number too small!");
                    } else if (binding.questionEditText.getText().toString().length() > 7) {
                        GameActivity.this.showAlert("Oh genius!");
                    } else {
                        GameActivity.this.setManualQuestion(
                                Integer.parseInt(binding.questionEditText.getText().toString()));
                    }
                } catch (Exception e) {
                    GameActivity.this.showAlert("Invalid number!");
                    Log.e(LOG_TAG, Arrays.toString(e.getStackTrace()));
                }
            }
            return false;
        });

        binding.optionContainer.setVisibility(View.INVISIBLE);
    }

    private void init() {
        binding.gameLayoutRoot.setBackgroundColor(0xffffffff);
        if (type != Type.MANUAL) {
            setQuestion();
        }

        styleButton(binding.opt1Button, getResources().getColor(R.color.button_gradient));
        binding.opt1Button.setText(String.valueOf(question.getOpt1()));
        binding.opt1Button.setOnClickListener(v -> evaluate(question, binding.opt1Button));

        styleButton(binding.opt2Button, getResources().getColor(R.color.button_gradient));
        binding.opt2Button.setText(String.valueOf(question.getOpt2()));
        binding.opt2Button.setOnClickListener(v -> evaluate(question, binding.opt2Button));

        styleButton(binding.opt3Button, getResources().getColor(R.color.button_gradient));
        binding.opt3Button.setText(String.valueOf(question.getOpt3()));
        binding.opt3Button.setOnClickListener(v -> evaluate(question, binding.opt3Button));

        binding.questionTextView.setText(String.valueOf(question.getNumber()));

        if (angleAnimator != null) cancelAngleAnimator();
        startAngleAnimator((int) question.getTotalTime());

        if (hueAnimator != null) cancelHueAnimator();
        startHueAnimator((int) question.getTotalTime());

        if (countDownTimer != null) cancelTimer();
        startTimer((int) question.getTotalTime());

        questionNo++;
    }

    private void styleButton(Button button, int gradientColor) {
        GradientDrawable drawable = (GradientDrawable) button.getBackground();
        drawable.mutate();
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setGradientRadius(150f);
        drawable.setColors(new int[]{0xffffffff, 0xffffffff, gradientColor});
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setManualQuestion(int number) {
        question = new Question(number);
        binding.optionContainer.setVisibility(View.VISIBLE);
        init();
    }

    private void setQuestion() {
        switch (type) {
            case CHALLENGE:
                setChallengeQuestion();
                break;
            case PRACTICE:
                assert intent.getExtras() != null;
                assert intent.getExtras().getString("DIFFICULTY") != null;
                setPracticeQuestion(intent.getExtras().getString("DIFFICULTY"));
                break;
        }

    }

    private void setPracticeQuestion(@Nullable String d) {
        if (d == null) {
            Log.e(LOG_TAG, "No Difficulty Intent Key Provided!");
            return;
        }
        Difficulty difficulty;
        if (d.equals("EASY")) {
            difficulty = Difficulty.EASY;
        } else if (d.equals("MEDIUM")) {
            difficulty = Difficulty.MEDIUM;
        } else {
            Random random = new Random();
            difficulty = Difficulty.values()[random.nextInt(3) + 2];
        }

        question = new Question(difficulty);
    }

    private void setChallengeQuestion() {
        Difficulty difficulty;
        if (questionNo < 15) {
            difficulty = Difficulty.EASY;
        } else if (questionNo < 30) {
            difficulty = Difficulty.MEDIUM;
        } else {
            Random random = new Random();
            difficulty = Difficulty.values()[random.nextInt(3) + 2];
        }

        question = new Question(difficulty);

    }

    private void evaluate(Question question, Button opt) {
        if (opt.getText().toString().equals(Integer.toString(question.getCorrectOption()))) {
            correct();
            ValueAnimator animator = ValueAnimator.ofInt(0xf3, 0xc0);
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.setRepeatCount(1);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.addUpdateListener(animation -> styleButton(opt,
                    Color.argb(
                            0xff,
                            (int) animation.getAnimatedValue(),
                            0xf3,
                            (int) animation.getAnimatedValue())));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    if (type != Type.MANUAL) {
                        init();
                    } else {
                        binding.questionEditText.setText("");
                        binding.questionEditText.requestFocus();
                    }
                }
            });
            animator.setDuration(200);
            animator.start();
            if (type == Type.MANUAL) binding.optionContainer.setVisibility(View.INVISIBLE);
        } else {
            wrong();
            binding.wrongTextView.setText(R.string.wrong);
            Animation animation = AnimationUtils
                    .loadAnimation(GameActivity.this, R.anim.shake);
            opt.startAnimation(animation);
            ValueAnimator wrongAnimator = ValueAnimator.ofInt(0xf3, 0xc0).setDuration(500);
            wrongAnimator.addUpdateListener(a -> styleButton(opt,
                    Color.argb(
                            0xff,
                            0xf3,
                            (int) a.getAnimatedValue(),
                            (int) a.getAnimatedValue())));
            wrongAnimator.setInterpolator(new FastOutSlowInInterpolator());
            wrongAnimator.setRepeatCount(0);
            wrongAnimator.start();
            opt.setText(getResources().getString(R.string.wrong_string, opt.getText()));
        }
    }

    private void displayCorrectView() {
        Button correct = (question.getCorrectOption() == question.getOpt1()) ? binding.opt1Button
                : (question.getCorrectOption() == question.getOpt2()) ? binding.opt2Button
                : binding.opt3Button;
        ValueAnimator correctAnimator = ValueAnimator.ofInt(0xf3, 0xc0).setDuration(500);
        correctAnimator.addUpdateListener(a -> styleButton(correct,
                Color.argb(
                        0xff,
                        (int) a.getAnimatedValue(),
                        0xf3,
                        (int) a.getAnimatedValue())));
        correctAnimator.setInterpolator(new FastOutSlowInInterpolator());
        correctAnimator.setRepeatCount(0);
        correctAnimator.start();
        correct.setText(getResources()
                .getString(R.string.correct_string, correct.getText(),
                        question.getNumber() / question.getCorrectOption()));
    }

    private void startHueAnimator(int time) {
        hueAnimator = ObjectAnimator
                .ofFloat(binding.countdownCustomView, "hue", 120.0f, 0.0f)
                .setDuration(time * 1000);
        hueAnimator.setInterpolator(new AccelerateInterpolator());
        hueAnimator.start();
    }

    private void cancelHueAnimator() {
        hueAnimator.cancel();
    }

    private void startBackgroundColourAnimator(int r, int g) {
        int alphaVal = (r > 0) ? 30 : 20;
        ValueAnimator backgroundColourAnimator = ValueAnimator.ofInt(0, alphaVal);
        backgroundColourAnimator.setInterpolator(new FastOutSlowInInterpolator());
        backgroundColourAnimator.setDuration(200);
        backgroundColourAnimator.addUpdateListener(animation -> changeBackgroundColor(Color.argb((int) animation.getAnimatedValue(), r, g, 0)));
        if (g > 0) {
            backgroundColourAnimator.setRepeatMode(ValueAnimator.REVERSE);
            backgroundColourAnimator.setRepeatCount(1);
        } else {
            backgroundColourAnimator.setRepeatCount(0);
        }
        backgroundColourAnimator.start();
    }

    private void changeBackgroundColor(int argb) {
        binding.gameLayoutRoot.setBackgroundColor(argb);
    }

    private void startAngleAnimator(int time) {
        angleAnimator = ObjectAnimator
                .ofFloat(binding.countdownCustomView, "angle", 10.0f, 170.0f)
                .setDuration(time * 1000);
        angleAnimator.setInterpolator(new AccelerateInterpolator());
        angleAnimator.start();
    }

    private void cancelAngleAnimator() {
        angleAnimator.cancel();
    }

    private void startTimer(final int time) {
        countDownTimer = new CountDownTimer(time * 1000, 100) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime = time - millisUntilFinished / 1000f;
                binding.timerTextView.setText(String.format("%.1f", millisUntilFinished / 1000f));
            }

            @Override
            public void onFinish() {
                binding.wrongTextView.setText(R.string.timeout);
                wrong();
            }
        };
        countDownTimer.start();
    }

    private void cancelTimer() {
        countDownTimer.cancel();
    }

    private void correct() {
        streak++;
        binding.streakCountTextView.setText(String.valueOf(streak));

        if (type == Type.CHALLENGE) {
            score += (question.getTotalTime() - currentTime) / question.getTotalTime() * question.getTotalScore();
            binding.scoreTextView.setText(String.valueOf(score));
        }

        startBackgroundColourAnimator(0, 255);

        cancelTimer();
        cancelAngleAnimator();
        cancelHueAnimator();

        if (currentTime < fastestTime) {
            fastestTime = currentTime;
        }
    }

    private void wrong() {
        if (streak > longestStreak) longestStreak = streak;
        streak = 0;
        vibrator.vibrate(100);
        binding.streakCountTextView.setText("0");
        if (type == Type.CHALLENGE) {
            livesRemaining--;
            binding.lifeTextView.setText(String.valueOf(livesRemaining));
        }

        startBackgroundColourAnimator(255, 0);
        cancelTimer();
        if (angleAnimator != null) cancelAngleAnimator();
        if (hueAnimator != null) cancelHueAnimator();
        binding.countdownCustomView.setAngle(180f);
        binding.countdownCustomView.setHue(0f);

        binding.nextButton.setVisibility(View.VISIBLE);
        binding.wrongTextView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.next);
        binding.nextButton.startAnimation(animation);
        binding.opt1Button.setClickable(false);
        binding.opt2Button.setClickable(false);
        binding.opt3Button.setClickable(false);
        displayCorrectView();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
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
