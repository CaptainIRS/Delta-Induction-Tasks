package com.rinish.factorfactor.Models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {

    private static final float[] time = {
            /*EASY        */ 10f,
            /*MEDIUM      */ 10f,
            /*HARD_SLOW   */ 15f,
            /*HARD_MEDIUM */ 7f,
            /*HARD_FAST   */ 3f
    };

    private static final int[][] generatingSet = {
            /*EASY        */ {2, 3, 5, 7, 11},
            /*MEDIUM      */ {13, 17, 19, 23, 29},
            /*HARD_SLOW   */ {59, 61, 67, 71, 73, 79, 83, 89, 97},
            /*HARD_MEDIUM */ {13, 17, 19, 23, 29, 31, 41},
            /*HARD_FAST   */ {2, 3, 5, 7, 11},
    };

    private static final int[] numberOfFactors = {
            /*EASY        */ 3,
            /*MEDIUM      */ 2,
            /*HARD_SLOW   */ 2,
            /*HARD_MEDIUM */ 2,
            /*HARD_FAST   */ 4,
    };

    private static final float[] score = {
            /*EASY        */ 20f,
            /*MEDIUM      */ 35f,
            /*HARD_SLOW   */ 50f,
            /*HARD_MEDIUM */ 50f,
            /*HARD_FAST   */ 50f,
    };

    private int number;
    private int opt1;
    private int opt2;
    private int opt3;
    private int correctOption;
    private float totalTime;
    private float totalScore;

    public Question(Difficulty difficulty) {
        int[] set = generateNumbers(difficulty);
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(0, set[0]));
        pairs.add(new Pair<>(1, set[1]));
        pairs.add(new Pair<>(2, set[2]));
        Collections.shuffle(pairs);
        opt1 = pairs.get(0).second;
        opt2 = pairs.get(1).second;
        opt3 = pairs.get(2).second;
        correctOption = pairs.get(0).first == 0 ? opt1 : pairs.get(1).first == 0 ? opt2 : opt3;
        totalTime = time[difficulty.ordinal()];
        totalScore = score[difficulty.ordinal()];
    }

    public Question(int number) {
        this.number = number;
        int[] set = generateNumbers();
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(0, set[0]));
        pairs.add(new Pair<>(1, set[1]));
        pairs.add(new Pair<>(2, set[2]));
        Collections.shuffle(pairs);
        opt1 = pairs.get(0).second;
        opt2 = pairs.get(1).second;
        opt3 = pairs.get(2).second;
        correctOption = pairs.get(0).first == 0 ? opt1 : pairs.get(1).first == 0 ? opt2 : opt3;
        totalTime = 10f;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public int getNumber() {
        return number;
    }

    public int getOpt1() {
        return opt1;
    }

    public int getOpt2() {
        return opt2;
    }

    public int getOpt3() {
        return opt3;
    }

    public float getTotalTime() {
        return totalTime;
    }

    private int[] generateNumbers() {
        int[] set = new int[3];
        int n = number;
        List<Integer> primeFactors = new ArrayList<>();
        while (n % 2 == 0) {
            primeFactors.add(2);
            n /= 2;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            while (n % i == 0) {
                primeFactors.add(i);
                n /= i;
            }
        }
        if (n > 2)
            primeFactors.add(n);
        primeFactors.add(1);
        System.out.println(primeFactors);
        Collections.shuffle(primeFactors);
        System.out.println(primeFactors);
        set[0] = primeFactors.get(0) * primeFactors.get(1);

        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            set[1] = random.nextInt((int) Math.sqrt(number)) + 2;
            if (set[1] == set[0] || number % set[1] == 0) {
                set[1] = 0;
                i = -1;
            }
        }
        for (int i = 0; i < 1; i++) {
            set[2] = random.nextInt((int) Math.sqrt(number)) + 2;
            if (set[2] == set[1] || set[2] == set[0] || number % set[2] == 0) {
                set[2] = 0;
                i = -1;
            }
        }

        return set;
    }

    private int[] generateNumbers(Difficulty difficulty) {
        int[] set = new int[3];
        int d = difficulty.ordinal();
        int noOfFactors = numberOfFactors[d], n;
        Random random = new Random();
        List<Integer> included = new ArrayList<>(), excluded = new ArrayList<>();
        for (int i = 0; i < noOfFactors; i++) {
            included.add(generatingSet[d][random.nextInt(generatingSet[d].length)]);
        }
        for (int i = 0; i < generatingSet[d].length; i++) {
            if (!included.contains(generatingSet[d][i])) {
                excluded.add(generatingSet[d][i]);
            }
        }
        System.out.println(included);
        System.out.println(excluded);

        number = 1;
        for (Integer integer : included) {
            number *= integer;
        }

        set[0] = 1;
        n = random.nextInt(noOfFactors - 1) + 1;
        for (int i = 0; i < n; i++) {
            set[0] *= included.get(i);
        }

        set[1] = 1;
        n = random.nextInt(noOfFactors - 1) + 1;
        Collections.shuffle(excluded);
        for (int i = 0; i < n && set[1] < number; i++) {
            set[1] *= excluded.get(random.nextInt(excluded.size()));
            if (i == n - 1 && (set[1] == set[0] || set[1] > number)) {
                set[1] = 1;
                i = -1;
                n = random.nextInt(noOfFactors - 1) + 1;
                Collections.shuffle(excluded);
            }
        }

        set[2] = 1;
        Collections.shuffle(excluded);
        n = random.nextInt(noOfFactors - 1) + 1;
        for (int i = 0; i < n && set[2] < number; i++) {
            set[2] *= excluded.get(random.nextInt(excluded.size()));
            if (i == n - 1 && (set[2] == set[1] || set[2] == set[0] || set[2] > number)) {
                set[2] = 1;
                i = -1;
                n = random.nextInt(noOfFactors - 1) + 1;
                Collections.shuffle(excluded);
            }
        }
        return set;
    }

}
