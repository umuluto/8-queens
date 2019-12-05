package com.ulto.app;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Drawer {
    private static int n;
    private static Canvas boardCanvas;
    private static Canvas queenCanvas;
    private static GraphicsContext boardContext;
    private static GraphicsContext queenContext;

    private static final Image queen = new Image("/images/queen.png");
    public static void init(Canvas bCanvas, Canvas qCanvas) {
        boardCanvas = bCanvas;
        queenCanvas = qCanvas;

        boardContext = boardCanvas.getGraphicsContext2D();
        queenContext = queenCanvas.getGraphicsContext2D();
    }

    public static void setBoardSize(int size) {
        n = size;
    }

    public static void drawBoard() {
        double boardWidth = boardCanvas.getWidth();
        double cellWidth = boardWidth / n;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if ((i - j) % 2 != 0) {
                    boardContext.setFill(Color.BURLYWOOD);
                } else {
                    boardContext.setFill(Color.CHOCOLATE);
                }
                boardContext.fillRect(j * cellWidth, i * cellWidth, cellWidth, cellWidth);
            }
        }
    }
    
    public static void sync(int[] buffer) {
        double boardWidth = boardCanvas.getWidth();
        double cellWidth = boardWidth / n;

        queenContext.setFill(Color.BLUE);
        queenContext.clearRect(0, 0, boardWidth, boardWidth);
        for (int i = 0; i < n; ++i) {
            if (buffer[i] == -1) break;
            queenContext.drawImage(queen, buffer[i] * cellWidth, i * cellWidth, cellWidth, cellWidth);
        }
    }

    public static void drawConflict(int i, int j) {
        double boardWidth = boardCanvas.getWidth();
        double cellWidth = boardWidth / n;
        
        queenContext.setFill(Color.rgb(255, 0, 0, 0.8));
        queenContext.fillRect(j * cellWidth, i * cellWidth, cellWidth, cellWidth);
    }
}