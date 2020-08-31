package com.rinish.factorfactor.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rinish.factorfactor.R;

public class Stats {

    private static final String HIGH_SCORE_KEY = "HIGH_SCORE";
    private static final String FASTEST_RESPONSE_KEY = "FASTEST_RESPONSE";
    private static final String LONGEST_STREAK_KEY = "LONGEST_STREAK";

    public static int getHighestScore(Context context) {
        return Integer.parseInt(getStat(HIGH_SCORE_KEY, context));
    }

    public static float getFastestResponse(Context context) {
        System.out.println(getStat(FASTEST_RESPONSE_KEY, context));
        return Float.parseFloat(getStat(FASTEST_RESPONSE_KEY, context));
    }

    public static int getLongestStreak(Context context) {
        return Integer.parseInt(getStat(LONGEST_STREAK_KEY, context));
    }

    public static void setHighestScore(int value, Context context) {
        updateStat(HIGH_SCORE_KEY, Integer.toString(value), context);
    }

    public static void setFastestResponse(float value, Context context) {
        updateStat(FASTEST_RESPONSE_KEY, Float.toString(value), context);
    }

    public static void setLongestStreak(int value, Context context) {
        updateStat(LONGEST_STREAK_KEY, Integer.toString(value), context);
    }

    private static void setDefaultKeys(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HIGH_SCORE_KEY, "0");
        editor.putString(FASTEST_RESPONSE_KEY, Float.toString(Float.MAX_VALUE));
        editor.putString(LONGEST_STREAK_KEY, "0");
        editor.apply();
    }

    private static void updateStat(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (!preferences.contains(key)) setDefaultKeys(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStat(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (!preferences.contains(key)) setDefaultKeys(context);
        return preferences.getString(key, null);
    }
}
