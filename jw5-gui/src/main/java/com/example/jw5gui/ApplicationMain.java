package com.example.jw5gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class ApplicationMain extends Application {
    private SingleGame singleGame;
    private MultiGame multiGame;
    private Pane pane;
    @Override
    public void start(Stage stage) throws IOException {
        pane = new Pane();
        Button btn1 = new Button("开始游戏");
        Button btn2 = new Button("多人游戏");
        Button btn3 = new Button("读取存档");

        btn1.setLayoutX(400);
        btn1.setLayoutY(300);

        btn2.setLayoutX(400);
        btn2.setLayoutY(350);

        btn3.setLayoutX(400);
        btn3.setLayoutY(400);

        btn1.setOnMouseClicked(
                (MouseEvent e)->{
                    singleGame=new SingleGame(pane);
                    pane=singleGame.returnPane();
                    pane.getScene().setOnKeyPressed(
                            (KeyEvent ke)->{
                                if(ke.getCode() == KeyCode.ENTER && singleGame.alive == false)
                                    restartSingleGame();
                                else
                                    singleGame.respondToKeyEvent(ke);
                            }
                    );
                   stage.setOnCloseRequest((event)->{
                       try {
                           singleGame.record();
                       } catch (IOException ex) {
                           ex.printStackTrace();
                       }
                   });
                    new Thread(
                            ()->{
                                while (singleGame.alive == true) {
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException ei) {
                                        ei.printStackTrace();
                                    }
                                    pane = singleGame.update();
                                }
                                pane = singleGame.lose();
                            }
                    ).start();
                }
        );

        btn2.setOnMouseClicked(
                (MouseEvent e)->{
                    multiGame = new MultiGame(pane,1);
                    pane = multiGame.returnPane();

                    pane.getScene().setOnKeyPressed((KeyEvent ei)-> {
                            multiGame.respondToKeyEvent(ei);
                        }
                    );

                    new Thread(
                          ()->{
                                while (multiGame.alive) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException ei) {
                                        ei.printStackTrace();
                                    }

                                    pane = multiGame.update();
                                }
                            }
                    ).start();
                }
        );

        btn3.setOnMouseClicked((e)->{
            singleGame=new SingleGame(pane);
            pane=singleGame.returnPane();

            singleGame.restore();
            pane=singleGame.returnPane();

            pane.getScene().setOnKeyPressed(
                    (KeyEvent ke)->{
                        if(ke.getCode() == KeyCode.ENTER && singleGame.alive == false)
                            restartSingleGame();
                        else
                            singleGame.respondToKeyEvent(ke);
                    }
            );
            stage.setOnCloseRequest((event)->{
                try {
                    singleGame.record();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(
                    ()->{
                        while (singleGame.alive == true) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ei) {
                                ei.printStackTrace();
                            }
                            pane = singleGame.update();
                        }
                        pane = singleGame.lose();
                    }
            ).start();

        });

        pane.getChildren().add(btn1);
        pane.getChildren().add(btn2);
        pane.getChildren().add(btn3);

        Scene scene = new Scene(pane, 1000, 800);
        stage.setTitle("MyGame");
        stage.setScene(scene);
        stage.show();

    }

    public void restartSingleGame(){

        singleGame=new SingleGame(pane);
        pane=singleGame.returnPane();

        new Thread(
                ()->{
                    while (singleGame.alive == true) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ei) {
                            ei.printStackTrace();
                        }
                        pane = singleGame.update();

                    }

                    pane = singleGame.lose();
                }
        ).start();
    }


    public static void main(String[] args) {
        launch();
    }
}