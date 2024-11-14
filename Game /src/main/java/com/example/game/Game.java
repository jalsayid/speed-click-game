package com.example.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game extends Application {
    // Creating the panes needed
    BorderPane borderPane = new BorderPane();
    VBox vBox = new VBox();
    // Creating timeline object for controlling the animation
    Timeline animation = new Timeline(new KeyFrame(Duration.millis(70), new Handler()));
    // creating an image array for the objects
    Image[] objectArray = {new Image("imgObj1.png") ,new Image("imgObj2.png"),new Image("imgObj3.png"),new Image("imgObj4.png")};
    // creating an image view array for displaying more than 1 object at different time
    ImageView[] imageView0bjs = new ImageView[4];
    // creating an image view to view the restart operator
    ImageView imageViewRestart = new ImageView(new Image("restartImage.png"));
    // creating an image view to view the reset operator
    ImageView imageViewReset = new ImageView(new Image("resetImage.png"));
    // Creating a label for the current scores and the remaining objects
    Label currentScoreLabel = new Label("Score: " + (0));
    Label remainingObj = new Label("Remaining objects: " + (30));
    //Creating an array list to store all the scores
    ArrayList<Integer> scores = new ArrayList<>();
    // Initialising the variables needed
    int currentScoreNum = 0, numOfObj = 0;
    // Creating a font for the text in the labels
    Font font = Font.font("Impact",23);


    // overriding the start method Which normally places UI controls in a scene and displays the scene in a stage
    @Override
    public void start(Stage stage) {
        // Styling the border pane and vBox and the labels
        borderPane.setPadding(new Insets(5,5,5,5));
        vBox.setSpacing(3.5);
        currentScoreLabel.setFont(font);
        remainingObj.setFont(font);
        // An Animation with indefinite duration (a cycleCount of INDEFINITE) runs repeatedly until the stop() method is explicitly called
        animation.setCycleCount(Timeline.INDEFINITE);

        /* using a for loop to fill the image view array with images,
         set their properties, and set a different value for each imageview */
        for (int i = 0; i < 4; i++){
            imageView0bjs[i] = new ImageView();
            imageView0bjs[i].setFitHeight(120);
            imageView0bjs[i].setFitWidth(77);
            imageView0bjs[i].setImage(objectArray[i]);
            imageView0bjs[i].setX((int) (Math.random( ) * 650 ));
            imageView0bjs[i].setUserData(i+1);
        }
        // To make the objects fall at different times we set the y position
        imageView0bjs[3].setY(-50);
        imageView0bjs[2].setY(-180);
        imageView0bjs[1].setY(-350);
        imageView0bjs[0].setY(-500);

        // Creating an image view for the background and setting its properties, then add it to the pane
        ImageView imageViewBackground = new ImageView(new Image("backgroundImage.png"));
        imageViewBackground.setFitWidth(700);
        imageViewBackground.setFitHeight(550);
        borderPane.getChildren().add(imageViewBackground);

        // Creating an image view for the start operator and setting its properties and adding it to the pane
        ImageView imageViewStart= new ImageView("startImage.png");
        imageViewStart.setFitWidth(200);
        imageViewStart.setFitHeight(90);
        borderPane.setCenter(imageViewStart);
        // Controlling the start operator
        imageViewStart.setOnMouseClicked(e ->{
            // adding a imageView array and the labels
            borderPane.getChildren().addAll(imageView0bjs);
            borderPane.setLeft(currentScoreLabel);
            borderPane.setRight(remainingObj);
            // remove the start operator then starting the animation
            borderPane.getChildren().remove(imageViewStart);
            animation.play();
        });

        for (int i=0; i<4; i++) {
            final int index = i;
            // Controlling the clicked object
            imageView0bjs[i].setOnMouseClicked(e -> {
                // To remove the object when its clicked and add a new one
                borderPane.getChildren().remove(imageView0bjs[index]);
                // setting the properties for the new object that's going to be added with a random position
                imageView0bjs[index].setY(-50);
                imageView0bjs[index].setX((int) (Math.random( ) * 650 ));
                // setting the new object then adding it
                imageView0bjs[index].setImage(objectArray[index]);
                borderPane.getChildren().add(imageView0bjs[index]);
                // To increase the speed of each new added object
                animation.setRate(animation.getRate() + 0.9);
                // To count the number of objects that got clicked
                numOfObj++;
                // Each object has a different value that will be added to the current score when clicked
                currentScoreNum += (int) imageView0bjs[index].getUserData();
                // Updating the value of currentScoreNum in currentScoreLabel and remaining objects
                currentScoreLabel.setText("Score: " + currentScoreNum);
                remainingObj.setText("Remaining objects: " + (30 - numOfObj));
            });
        }

        // setting the properties for the restart operator
        imageViewRestart.setFitWidth(200);
        imageViewRestart.setFitHeight(70);
        // Controlling the restart operator
        imageViewRestart.setOnMouseClicked(e -> {
            // setting the properties for the objects to restart
            for (int i=0; i<4; i++) {
                imageView0bjs[i].setX((int) (Math.random( ) * 650 ));
                imageView0bjs[i].setImage(objectArray[i]);
            }
            imageView0bjs[3].setY(-50);
            imageView0bjs[2].setY(-200);
            imageView0bjs[1].setY(-350);
            imageView0bjs[0].setY(-500);
            // restarting the speed
            animation.setRate(1);
            // reset the currentScoreNum and numOfObj
            currentScoreNum = 0;
            numOfObj = 0;
            // Updating the current score number in the current score label and remainingObj label
            currentScoreLabel.setText("Score: " + currentScoreNum);
            remainingObj.setText("Remaining objects: " + (30 - numOfObj));
            // adding the objects then playing the animation
            borderPane.getChildren().addAll(imageView0bjs);
            animation.play();
        });

        // setting the properties for the top5 reset operator
        imageViewReset.setFitWidth(100);
        imageViewReset.setFitHeight(35);
        // Controlling the reset operator
        imageViewReset.setOnMouseClicked(e -> {
            File file = new File("scores.txt");
            try {
                FileWriter writer = new FileWriter(file,false);
                //clear the file to have new top5
                writer.write("");
                writer.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        // Creating a scene and setting its properties
        Scene scene = new Scene(borderPane,700, 550);
        // set the scene and styling the stage
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Speed Click Game");
        stage.show();
    }

    // Defining a class handler to handle the animation that implements the EventHandler interface
    class Handler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // when the animation is played the top score and restart operator are removed
            vBox.getChildren().clear();
            for (int i=0; i<4; i++) {
                // To make the object able to fall down by updating its Y position
                imageView0bjs[i].setY(imageView0bjs[i].getY() + 1);
                // if an object reach the bottom it will be removed and a new object will be added
                if (imageView0bjs[i].getY() - 15 > borderPane.getHeight()) {
                    numOfObj++;
                    // the remainingObj label will be updated
                    remainingObj.setText("Remaining objects: " + (30 - numOfObj));
                    borderPane.getChildren().remove(imageView0bjs[i]);
                    // setting the properties for the new object that's going to be added with a random x position
                    imageView0bjs[i].setY(-50);
                    imageView0bjs[i].setX((int) (Math.random( ) * 650 ));
                    // setting a random object then adding it
                    imageView0bjs[i].setImage(objectArray[(int) (Math.random() * 4)]);
                    // Speed increases throughout the game.
                    animation.setRate(animation.getRate() + 0.7);
                    borderPane.getChildren().add(imageView0bjs[i]);
                }
            }
            /* There is one condition to stop the animation:
               if all the objects are pressed or reach the bottom and the total number of objects is 30
            */
            if(numOfObj > 29) {
                stop();
            }
        }
    }
    // Defining the top five method to add the top five scorers in the vBox
    public void top5() {
        // Creating the top five label and setting its properties and then adding it
        Label top5Label= new Label("Top 5 Scores: ");
        top5Label.setFont(font) ;
        vBox.getChildren().add(top5Label);
        FileWriter fileWriter;
        // To store the scores in a file
        try(Scanner read = new Scanner(new File("scores.txt"))) {
            fileWriter = new FileWriter("scores.txt", true);
            // we add the newest value of currentScoreNum
            fileWriter.write(currentScoreNum + "\n");
            fileWriter.close();
            // Clearing the scores ArrayList so that no duplicates( weren't played by the user) are added to the list
            scores.clear();
            while(read.hasNext()) {
                // To add all previous scores
                scores.add(read.nextInt());
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // sort the scores to start with the greatest value in aim to add the top5 scores only
        scores.sort(Collections.reverseOrder());
        // creating a loop to add the top5 scores (unless the scores are not yet 5, it will add only the greatest values available in scores)
        for (int i = 0; i < 5 && i < scores.size() ; i++) {
            Label top5ScoreLabel = new Label("" + scores.get(i)) ;
            top5ScoreLabel.setFont(font) ;
            vBox.getChildren().add(top5ScoreLabel);
        }
    }
    // Defining the stop method to show the top five scores and the restart operator and stop the animation
    public void stop() {
        // to remove the object from the pane then stop
        borderPane.getChildren().removeAll(imageView0bjs);
        animation.stop();
        // calling the top5 method
        top5();
        // adding the restart and the reset operator
        vBox.getChildren().addAll(imageViewReset,imageViewRestart);
        // setting the vbox properties then adding it to the border pane
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
    }
    // The main method to launch the program
    public static void main(String[] args) {
        launch(args);
    }
}