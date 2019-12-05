package com.ulto.app;

import java.util.ArrayList;
import java.util.List;

import com.ulto.app.fxml.Visualizer;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;

public class Controller {
    private int n = 1;
    private IntegerProperty delay = new SimpleIntegerProperty(500);
    private boolean inSync = true;

    List<int[]> solutions = new ArrayList<>();

    NQueensSolver solver = null;
    private Visualizer visualizer;

    public void solveFor(int n) {
        if (solver != null)
            solver.cancel();

        this.n = n;
        solutions.clear();
        visualizer.getSolutionsList().getChildren().clear();
        Drawer.setBoardSize(n);
        Drawer.drawBoard();
        solver = new NQueensSolver(n);
        if (inSync) solver.setSync(true);
        else solver.setSync(false);

        solver.setSyncHandler(this::onSync);
        solver.setPushSolutionHandler(this::onSolutionFound);
        solver.setShowConflictHandler(this::onConflict);
        solver.setSync(true);

        Thread solverThread = new Thread(() -> solver.solve());
        solverThread.start();
    }

    public void onSync(int[] buffer) {
        Platform.runLater(() -> Drawer.sync(buffer));
        waitFor(delay.get());
    }

    public void onSolutionFound(int[] buffer) {
        solutions.add(buffer.clone());
        Button button = new Button("Solution " + solutions.size());
        int currentSize = solutions.size();
        button.setOnAction(e -> Drawer.sync(solutions.get(currentSize - 1)));
        Platform.runLater(() -> visualizer.getSolutionsList().getChildren().add(button));
        waitFor(delay.get());
    }

    public void onConflict(int i, int j) {
        Platform.runLater(() -> Drawer.drawConflict(i, j));
        waitFor(2 * delay.get() / 3);
    }

    public void cancel() {
        solver.cancel();
    }

    public void pause() {
        if (solver.isRunning())
            solver.pause();
        else solver.resume();
    }

    public void syncOn() {
        inSync = true;
        solver.setSync(true);
    }

    public void syncOff() {
        inSync = false;
        solver.setSync(false);
    }

    public void setDelay(int delay) {
        this.delay = new SimpleIntegerProperty(delay);
    }

    public IntegerProperty delayProperty() {
        return delay;
    }

    public void waitFor(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.exit(0);
        }
    }

	public void setVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
	}
}