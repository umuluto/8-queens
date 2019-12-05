package com.ulto.app;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NQueensSolver {
    private int n;
    private int[] columns;
    private int[] topLeftDiagonals;
    private int[] topRightDiagonals;
    
    private int[] buffer;

    private volatile boolean inSync = false;
    private volatile boolean paused = false;
    private volatile boolean cancelled = false;

    private Consumer<int[]> syncHandler = null;
    private Consumer<int[]> pushSolutionHandler = null;
    private BiConsumer<Integer, Integer> showConflictHandler = null;

    public NQueensSolver(int n) {
        this.n = n;

        columns = new int[n];
        topLeftDiagonals = new int[2*n-1];
        topRightDiagonals = new int[2*n-1];
        buffer = new int[n];

        Arrays.fill(columns, -1);
        Arrays.fill(topLeftDiagonals, -1);
        Arrays.fill(topRightDiagonals, -1);
        Arrays.fill(buffer, -1);
    }

    private synchronized void _backtrack(int i) {
        if (i == n) {
            pushSolution();
            return;
        }
        
        for (int j = 0; j < n; ++j) {
            try {
                while (paused)
                    wait();
            } catch (Exception e) {
                cancelled = true;
            }

            if (cancelled) return;

            bufferSet(i, j);
            
            if (columns[j] != -1) {
                showConflict(columns[j], j);
                bufferSet(i, -1);
                continue;
            }

            if (topLeftDiagonals[j - i + n - 1] != -1) {
                showConflict(topLeftDiagonals[j - i + n - 1], j - i + topLeftDiagonals[j - i + n - 1]);
                bufferSet(i, -1);
                continue;
            }

            if (topRightDiagonals[i + j] != -1) {
                showConflict(topRightDiagonals[i + j], i + j - topRightDiagonals[i + j]);
                bufferSet(i, -1);
                continue;
            }

            columns[j] = topLeftDiagonals[j - i + n - 1]
                       = topRightDiagonals[i + j] = i;

            _backtrack(i + 1);

            bufferSet(i, -1);

            columns[j] = topLeftDiagonals[j - i + n - 1]
                       = topRightDiagonals[i + j] = -1;
        }
    }

    public void solve() {
        _backtrack(0);
    }

    public void bufferSet(int i, int j) {
        buffer[i] = j;
        sync();
    }

    public void setSync(boolean inSync) {
        this.inSync = inSync;
    }

    public void sync() {
        if (!inSync || syncHandler == null) return;
        syncHandler.accept(buffer);
    }

    public void setSyncHandler(Consumer<int[]> func) {
        syncHandler = func;
    }

    public void pushSolution() {
        if (pushSolutionHandler == null) return;
        pushSolutionHandler.accept(buffer);
    }

    public void setPushSolutionHandler(Consumer<int[]> func) {
        pushSolutionHandler = func;
    }

    private void showConflict(int i, int j) {
        if (!inSync || showConflictHandler == null) return;
        showConflictHandler.accept(i, j);
    }

    public void setShowConflictHandler(BiConsumer<Integer, Integer> func) {
        showConflictHandler = func;
    }

    public void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    public synchronized void cancel() {
        cancelled = true;
        notify();
    }

    public boolean isRunning() {
        return !paused;
    }
}