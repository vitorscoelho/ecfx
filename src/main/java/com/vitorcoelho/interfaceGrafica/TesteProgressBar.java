/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.interfaceGrafica;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class TesteProgressBar extends Application {
     
    final int TASK_MAX = 10000;
    Task task = new Task<Void>() {
 
        @Override
        protected Void call() throws Exception {
 
            for (int i=1; i <= TASK_MAX; i++) {
                updateProgress(i, TASK_MAX);
                 
                Platform.runLater(new Runnable(){
 
                    @Override
                    public void run() {
                        drawSomethingOnMyWritableImage();
                    }
                });
                 
                try {
                    Thread.sleep(1);
                } catch (InterruptedException interrupted) {
                }
            }
            return null;
        }
    };
 
     
    WritableImage myWritableImage;
    PixelWriter myPixelWriter;
    final int IMG_WIDTH = 255;
    final int IMG_HEIGHT = 255;
     
    ProgressBar progressBar;
    Button startButton;
     
    @Override
    public void start(Stage primaryStage) {
         
        prepareMyWritableImage();
         
        ImageView myImage = new ImageView();        
        myImage.setImage(myWritableImage);
         
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(IMG_WIDTH);
        progressBar.progressProperty().bind(task.progressProperty());
         
        startButton = new Button("Start");
         
        VBox root = new VBox();
        root.getChildren().addAll(myImage, progressBar, startButton);
         
        Scene scene = new Scene(root, 300, 300);
         
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
         
         
         
        startButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                new Thread(task).start();
            }
        });
    }
 
    public static void main(String[] args) {
        launch(args);
    }
     
    private void prepareMyWritableImage(){
         
        myWritableImage = new WritableImage(IMG_WIDTH, IMG_HEIGHT);
        myPixelWriter = myWritableImage.getPixelWriter();
         
        //fill with background blue
        for(int x = 0; x < IMG_WIDTH; x++){
            for(int y = 0; y < IMG_HEIGHT; y++){
                myPixelWriter.setColor(x, y, Color.GRAY);
            }
        }
    }
     
    private void drawSomethingOnMyWritableImage(){
         
        int x = (int)(Math.random() * IMG_WIDTH);
        int y = (int)(Math.random() * IMG_HEIGHT);
        double r = Math.random();
        double g = Math.random();
        double b = Math.random();
         
        myPixelWriter.setColor(x, y, new Color(r, g, b, 1.0));
    }
}
