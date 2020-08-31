package com.rinish.dotsandboxes.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends View {

    private final int OFFSET = px(40);
    private final int STROKE_WIDTH = px(4);
    private final int DOT_WIDTH = px(15);

    private enum Side {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    boolean initialized = false;
    boolean selected = false;

    private int noOfRows;
    private int noOfColumns;
    private int noOfPlayers;

    private float cellWidth;
    private float cellHeight;

    private RectF[][] dots;

    private static class Box {
        Rect rect;
        boolean left, right, top, bottom;
        int player;

        boolean isEnclosed() {
            return left & right & top & bottom;
        }
    }

    private Box[][] boxes;

    private RectF currentDot;
    int currentPlayer = 1;
    int currentRow, currentColumn;

    ArrayList<Integer> turns;

    private ArrayList<Pair<Path, Integer>> paths;

    private ArrayList<Pair<Point, Integer>> blockedEdges;
    private ArrayList<Pair<Box, Side>> edgeHistory;

    private Paint[] linePaints;
    private Paint dotPaint;
    private Paint boxPaint;

    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(int r, int c, int p) {

        paths = new ArrayList<>();
        blockedEdges = new ArrayList<>();
        edgeHistory = new ArrayList<>();
        turns = new ArrayList<>();
        turns.add(1);

        currentPlayer = 1;

        noOfRows = r;
        noOfColumns = c;
        noOfPlayers = p;

        dots = new RectF[r + 1][c + 1];
        boxes = new Box[r][c];

        cellWidth = (float) (getWidth() - 2 * OFFSET - DOT_WIDTH) / noOfColumns;
        cellHeight = (float) (getHeight() - 2 * OFFSET - DOT_WIDTH) / noOfRows;

        for (int i = 0; i < noOfRows + 1; i++) {
            for (int j = 0; j < noOfColumns + 1; j++) {
                dots[i][j] = new RectF(
                        (int) (OFFSET + j * cellWidth),
                        (int) (OFFSET + i * cellHeight),
                        (int) (OFFSET + j * cellWidth + DOT_WIDTH ),
                        (int) (OFFSET + i * cellHeight + DOT_WIDTH )
                );
                if (i != noOfRows && j != noOfColumns) {
                    boxes[i][j] = new Box();
                    boxes[i][j].rect = new Rect(
                            (int) (OFFSET + j * cellWidth + DOT_WIDTH / 2),
                            (int) (OFFSET + i * cellHeight + DOT_WIDTH / 2),
                            (int) (OFFSET + (j + 1) * cellWidth + DOT_WIDTH / 2),
                            (int) (OFFSET + (i + 1) * cellHeight + DOT_WIDTH / 2)
                    );
                }
            }
        }

        changeLineColors();

        dotPaint = new Paint();

        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        initialized = true;
    }

    public void changeLineColors() {
        linePaints = new Paint[noOfPlayers];
        Random random = new Random();
        for (int i = 0; i < linePaints.length; i++) {
            linePaints[i] = new Paint();
            linePaints[i].setStrokeWidth((float) (DOT_WIDTH / Math.sqrt(2)));
            linePaints[i].setStrokeCap(Paint.Cap.ROUND);
            linePaints[i].setStyle(Paint.Style.STROKE);
            linePaints[i].setFlags(Paint.ANTI_ALIAS_FLAG);
            linePaints[i].setColor(Color.HSVToColor(
                    new float[]{random.nextInt(360), 100, 100}));
        }
    }

    public ArrayList<Pair<Integer, Integer>> getScores() {
        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
        int[] playerScores = new int[noOfPlayers];
        for(int i = 0; i < noOfRows; i++) {
            for(int j = 0; j < noOfColumns; j++) {
                if(boxes[i][j].isEnclosed() && boxes[i][j].player > 0) {
                    playerScores[boxes[i][j].player - 1]++;
                }
            }
        }
        for(int i = 0; i < noOfPlayers; i++) {
            list.add(new Pair<>(playerScores[i], linePaints[i].getColor()));
        }
        return list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (initialized) {
            for (int i = 0; i < noOfRows; i++) {
                for (int j = 0; j < noOfColumns; j++) {
                    if (boxes[i][j].isEnclosed()) {
                        boxPaint.setColor(getAlphaColor(linePaints[boxes[i][j].player - 1].getColor()));
                        canvas.drawRect(boxes[i][j].rect, boxPaint);
                    } else {
                        dotPaint.setStrokeWidth(px(0.5f));
                        dotPaint.setStyle(Paint.Style.STROKE);
                        dotPaint.setColor(getAlphaColor(dotPaint.getColor()));
                        canvas.drawRect(boxes[i][j].rect, dotPaint);
                    }
                }
            }
            for (Pair<Path, Integer> path : paths) {
                canvas.drawPath(path.first, linePaints[path.second]);
            }
            for (int i = 0; i < noOfRows + 1; i++) {
                for (int j = 0; j < noOfColumns + 1; j++) {
                    dotPaint.setStyle(Paint.Style.FILL);
                    dotPaint.setStrokeWidth(px(STROKE_WIDTH));
                    dotPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
                    dotPaint.setColor(0x90000000);
                    canvas.drawOval(dots[i][j], dotPaint);
                }
            }
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!selected) {
                for (int i = 0; i < noOfRows + 1; i++) {
                    for (int j = 0; j < noOfColumns + 1; j++) {
                        RectF bigRect = new RectF(
                                dots[i][j].left - px(20),
                                dots[i][j].top - px(20),
                                dots[i][j].right + px(20),
                                dots[i][j].bottom + px(20)
                        );
                        if (bigRect.contains(event.getX(), event.getY())) {
                            currentDot = dots[i][j];
                            currentRow = i;
                            currentColumn = j;
                            selected = true;
                            paths.add(new Pair<>(new Path(), currentPlayer - 1));
                            break;
                        }
                    }
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (selected) {
                paths.remove(paths.size() - 1);
                currentDot = null;
                selected = false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (selected) {
                float dx = event.getX() - getDotX(currentDot);
                float dy = event.getY() - getDotY(currentDot);

                boolean directionHorizontal = Math.abs(dx) > Math.abs(dy);

                float pathX = directionHorizontal ? event.getX()
                        : getDotX(currentDot);
                float pathY = directionHorizontal ? getDotY(currentDot)
                        : event.getY();

                if ((currentColumn == 0 && dx < 0) || (currentColumn == noOfColumns && dx > 1)) {
                    pathX = getDotX(currentDot);
                }

                if ((currentRow == 0 && dy < 0) || (currentRow == noOfRows && dy > 1)) {
                    pathY = getDotY(currentDot);
                }
                int flag = 0;
                Pair<Point, Integer> midpoint = null;
                if ((int) ((pathY - getDotY(currentDot)) / cellHeight) == 0) {
                    if ((int) ((pathX - getDotX(currentDot)) / cellWidth) >= 1) {
                        midpoint = connectTo(currentRow, currentColumn + 1);
                        flag++;
                    } else if ((int) ((pathX - getDotX(currentDot)) / cellWidth) <= -1) {
                        midpoint = connectTo(currentRow, currentColumn - 1);
                        flag++;
                    }

                } else if ((int) Math.abs((pathX - getDotX(currentDot)) / cellWidth) == 0) {
                    if ((int) ((pathY - getDotY(currentDot)) / cellHeight) >= 1) {
                        midpoint = connectTo(currentRow + 1, currentColumn);
                        flag++;
                    } else if ((int) ((pathY - getDotY(currentDot)) / cellHeight) <= -1) {
                        midpoint = connectTo(currentRow - 1, currentColumn);
                        flag++;
                    }
                }
                if (flag == 0) {
                    paths.get(paths.size() - 1).first.reset();
                    paths.get(paths.size() - 1).first.moveTo(
                            getDotX(currentDot),
                            getDotY(currentDot)
                    );
                    paths.get(paths.size() - 1).first.lineTo(pathX, pathY);
                } else {
                    if (midpoint == null) {
                        paths.remove(paths.size() - 1);
                        currentDot = null;
                        selected = false;
                    } else {
                        blockedEdges.add(midpoint);
                        currentDot = null;
                        selected = false;
                    }
                }
            }
        }
        invalidate();
        return true;
    }

    public void popPath() {
        if (paths.size() > 0) {
            paths.remove(paths.size() - 1);
            for(int i = 0; i < blockedEdges.get(blockedEdges.size() - 1).second; i++) {
                Box currentBox = edgeHistory.get(edgeHistory.size() - 1).first;
                switch (edgeHistory.get(edgeHistory.size() - 1).second) {
                    case LEFT:
                        currentBox.left = false;
                        break;
                    case RIGHT:
                        currentBox.right = false;
                        break;
                    case TOP:
                        currentBox.top = false;
                        break;
                    case BOTTOM:
                        currentBox.bottom = false;
                        break;
                }
                currentBox.player = 0;
                edgeHistory.remove(edgeHistory.size() - 1);
            }
            blockedEdges.remove(blockedEdges.size() - 1);
            if(turns.size() > 1) {
                turns.remove(turns.size() - 1);
                currentPlayer = turns.get(turns.size() - 1);
            }
            currentDot = null;
            selected = false;
        }
    }

    private Pair<Point, Integer> connectTo(int x, int y) {
        float fromX = getDotX(currentDot),
                toX = getDotX(dots[x][y]),
                fromY = getDotY(currentDot),
                toY = getDotY(dots[x][y]);
        System.out.println(blockedEdges);
        for (Pair<Point, Integer> point : blockedEdges) {
            if ((int) (fromX + toX) / 2 == point.first.x
                    && (int) (fromY + toY) / 2 == point.first.y) {
                return null;
            }
        }
        int changedEdges = 0;
        boolean retainTurn = false;
        if (fromY == toY) {
            if (toX - fromX < 0) {
                if (currentRow > 0) {
                    boxes[currentRow - 1][currentColumn - 1].top = true;
                    boxes[currentRow - 1][currentColumn - 1].player = currentPlayer;
                    retainTurn = boxes[currentRow - 1][currentColumn - 1].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow - 1][currentColumn - 1], Side.TOP));
                    changedEdges++;
                }
                if (currentRow < noOfRows) {
                    boxes[currentRow][currentColumn - 1].bottom = true;
                    boxes[currentRow][currentColumn - 1].player = currentPlayer;
                    retainTurn |= boxes[currentRow][currentColumn - 1].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow][currentColumn - 1], Side.BOTTOM));
                    changedEdges++;
                }
            } else {
                if (currentRow > 0) {
                    boxes[currentRow - 1][currentColumn].top = true;
                    boxes[currentRow - 1][currentColumn].player = currentPlayer;
                    retainTurn = boxes[currentRow - 1][currentColumn].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow - 1][currentColumn], Side.TOP));
                    changedEdges++;
                }
                if (currentRow < noOfRows) {
                    boxes[currentRow][currentColumn].bottom = true;
                    boxes[currentRow][currentColumn].player = currentPlayer;
                    retainTurn |= boxes[currentRow][currentColumn].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow][currentColumn], Side.BOTTOM));
                    changedEdges++;
                }
            }
        } else if (fromX == toX) {
            if (toY - fromY < 0) {
                if (currentColumn > 0) {
                    boxes[currentRow - 1][currentColumn - 1].right = true;
                    boxes[currentRow - 1][currentColumn - 1].player = currentPlayer;
                    retainTurn = boxes[currentRow - 1][currentColumn - 1].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow - 1][currentColumn - 1], Side.RIGHT));
                    changedEdges++;
                }
                if (currentColumn < noOfColumns) {
                    boxes[currentRow - 1][currentColumn].left = true;
                    boxes[currentRow - 1][currentColumn].player = currentPlayer;
                    retainTurn |= boxes[currentRow - 1][currentColumn].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow - 1][currentColumn], Side.LEFT));
                    changedEdges++;
                }
            } else {
                if (currentColumn > 0) {
                    boxes[currentRow][currentColumn - 1].right = true;
                    boxes[currentRow][currentColumn - 1].player = currentPlayer;
                    retainTurn = boxes[currentRow][currentColumn - 1].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow][currentColumn - 1], Side.RIGHT));
                    changedEdges++;
                }
                if (currentColumn < noOfColumns) {
                    boxes[currentRow][currentColumn].left = true;
                    boxes[currentRow][currentColumn].player = currentPlayer;
                    retainTurn |= boxes[currentRow][currentColumn].isEnclosed();
                    edgeHistory.add(new Pair<>(boxes[currentRow][currentColumn], Side.LEFT));
                    changedEdges++;
                }
            }
        }
        if(!retainTurn) {
            currentPlayer++;
            if (currentPlayer == noOfPlayers + 1) currentPlayer = 1;
        }
        turns.add(currentPlayer);
        paths.get(paths.size() - 1).first.reset();
        paths.get(paths.size() - 1).first.moveTo(fromX, fromY);
        paths.get(paths.size() - 1).first.lineTo(toX, toY);
        return new Pair<>(new Point(
                (int) (getDotX(currentDot) + getDotX(dots[x][y])) / 2,
                (int) (getDotY(currentDot) + getDotY(dots[x][y])) / 2
        ), changedEdges);
    }

    private float getDotX(RectF dot) {
        return (dot.left + dot.right) / 2;
    }

    private float getDotY(RectF dot) {
        return (dot.top + dot.bottom) / 2;
    }

    private int getAlphaColor(int color) {
        return Color.argb(60, Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);

        System.out.println("Size Changed!");

        cellWidth = (float) (w - 2 * OFFSET - DOT_WIDTH) / noOfColumns;
        cellHeight = (float) (h - 2 * OFFSET - DOT_WIDTH) / noOfRows;

        for (int i = 0; i < noOfRows + 1; i++) {
            for (int j = 0; j < noOfColumns + 1; j++) {
                dots[i][j] = new RectF(
                        (int) (OFFSET + j * cellWidth),
                        (int) (OFFSET + i * cellHeight),
                        (int) (OFFSET + j * cellWidth + DOT_WIDTH ),
                        (int) (OFFSET + i * cellHeight + DOT_WIDTH )
                );
                if (i != noOfRows && j != noOfColumns) {
                    boxes[i][j].rect = new Rect(
                            (int) (OFFSET + j * cellWidth + DOT_WIDTH / 2),
                            (int) (OFFSET + i * cellHeight + DOT_WIDTH / 2),
                            (int) (OFFSET + (j + 1) * cellWidth + DOT_WIDTH / 2),
                            (int) (OFFSET + (i + 1) * cellHeight + DOT_WIDTH / 2)
                    );
                }
            }
        }
    }

    private int px(float dp) {
        return (int) (dp * ((float) getResources()
                .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
