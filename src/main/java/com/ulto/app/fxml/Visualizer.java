package com.ulto.app.fxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Visualizer extends BorderPane {
    @FXML
    Canvas boardCanvas;

    @FXML
    Canvas queenCanvas;
    
    @FXML
    TextField boardSize;

    @FXML
    Button startButton;

    @FXML
    Button pauseButton;

    @FXML
    Slider slider;

    @FXML
    VBox solutionsList;

    public Visualizer() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Visualizer.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Canvas getBoardCanvas() {
        return boardCanvas;
    }

    public Canvas getQueenCanvas() {
        return queenCanvas;
    }

    public TextField getBoardSize() {
        return boardSize;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Slider getSlider() {
        return slider;
    }

    public VBox getSolutionsList() {
        return solutionsList;
    }
}