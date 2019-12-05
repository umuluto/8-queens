package com.ulto.app;

import com.ulto.app.fxml.Visualizer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Visualizer visualizer = new Visualizer();
        Drawer.init(visualizer.getBoardCanvas(), visualizer.getQueenCanvas());

        Scene scene = new Scene(visualizer);
        stage.setScene(scene);
        stage.setTitle("n queens");
        stage.show();

        Controller controller = new Controller();
        controller.setVisualizer(visualizer);

        visualizer.getPauseButton().setOnAction(e -> controller.pause());

        TextField boardSize = visualizer.getBoardSize();
        boardSize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    boardSize.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        visualizer.getStartButton().setOnAction(e -> {
            String text = boardSize.getText();
            if (text.isEmpty()) return;
            controller.solveFor(Integer.parseInt(text));
        });

        Slider slider = visualizer.getSlider();
        controller.delayProperty().bind(slider.valueProperty());
        slider.valueProperty().addListener(e -> {
            if (slider.valueProperty().get() < 16)
                controller.syncOff();
            else controller.syncOn();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
