package com.example.jw5gui;


import com.example.jw5gui.MultiPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.nio.ByteBuffer;

public class ParallelGame extends Application {
    public static MultiPlayer multiPlayer;
    Pane pane;
    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        Scene scene = new Scene(pane, 1000, 800);
        stage.setTitle("ParallelGame");
        stage.setScene(scene);
        stage.show();

        multiPlayer = new MultiPlayer(pane);
        pane = multiPlayer.returnPane();

        new Thread(
                ()->{
                    while (multiPlayer.alive) {
                        try {
                            Thread.sleep(125);
                        } catch (InterruptedException ei) {
                            ei.printStackTrace();
                        }

                        pane = multiPlayer.update();

                    }
                }
        ).start();


    }

    public static void responseToUser(String s){
        multiPlayer.respondToKeyEvent(s);
    }

    public static void responseToOtherUser(String s){
        multiPlayer.respondToOtherKeyEvent(s);
    }

}
