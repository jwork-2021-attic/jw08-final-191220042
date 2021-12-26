package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.Serializable;

public class Thing implements Serializable {

    String url;
    protected World world;
    protected GridPane pane;
    protected boolean alive;


    protected int x;
    protected int y;

    transient protected ImageView imageView;

    public String getUrl() {
        return url;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Thing(String url,World world,GridPane pane,int x,int y){
        this.url=url;
        this.world=world;
        this.pane=pane;
        alive=true;
        this.x=x;
        this.y=y;
    }

    public Thing(String url,World world,GridPane pane){
        this.url=url;
        this.world=world;
        this.pane=pane;
        alive=true;
    }


    public void update() {
        Platform.runLater(()->{
            pane.getChildren().remove(imageView);
            if(alive == true) {
                pane.add(imageView, x, y);
            }else{
                world.remove(this);
            }
        });
    }

}
