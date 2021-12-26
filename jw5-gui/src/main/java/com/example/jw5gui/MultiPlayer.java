package com.example.jw5gui;

import com.example.jw5gui.world.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class MultiPlayer {
    private Pane myPane;
    private GridPane gridPane;
    private Rectangle rect1;
    private Rectangle rect2;
    public World myWorld;

    public Creature player;
    public Creature player2;

    int width = 12;
    int height = 12;
    public Boolean alive = true;


    public MultiPlayer(Pane pane){
        this.myPane=pane;
        myPane.getChildren().clear();

        myWorld=createWorld();

        gridPane = new GridPane();
        gridPane.setLayoutX(30);
        gridPane.setLayoutY(30);

        for(int i=0; i<width+1; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(22));
            gridPane.getRowConstraints().add(new RowConstraints(22));
        }

        load();

        ThingFactory thingFactory = new ThingFactory(this.myWorld,gridPane);
        creatThings(thingFactory);
        CreatureFactory creatureFactory = new CreatureFactory(this.myWorld,gridPane,thingFactory);
        createCreatures(creatureFactory);

        setHpUI(player,550,rect1,"Player1");
        setHpUI(player2,650,rect2,"Player2");

        myPane.getChildren().add(gridPane);
    }

    public void respondToKeyEvent(String key) {
        if(key.equals("Left")) {
            player.moveBy(-1, 0);
            player.lastDir = 3;
        }else if(key.equals("Right")) {
            player.moveBy(1, 0);
            player.lastDir = 4;
        }else if(key.equals("Up")) {
            player.moveBy(0, -1);
            player.lastDir = 1;
        }else if(key.equals("Down")){
            player.moveBy(0, 1);
            player.lastDir = 2;
        }else {
            ((PlayerAI)player.getAI()).skill(key);
        }
    }

    public void respondToOtherKeyEvent(String key) {
        if(key.equals("Left")) {
            player2.moveBy(-1, 0);
            player2.lastDir = 3;
        }else if(key.equals("Right")) {
            player2.moveBy(1, 0);
            player2.lastDir = 4;
        }else if(key.equals("Up")) {
            player2.moveBy(0, -1);
            player2.lastDir = 1;
        }else if(key.equals("Down")){
            player2.moveBy(0, 1);
            player2.lastDir = 2;
        }else {
            ((PlayerAI)player2.getAI()).skill(key);
        }
    }

    private void creatThings(ThingFactory thingFactory){

    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer();
        player.setX(0);
        player.setY(0);

        this.player2 = creatureFactory.newOtherPlayer();
        player2.setX(width-1);
        player2.setY(height-1);

        Image image = new Image(player.getUrl());
        player.setImageView(new ImageView(image));
        gridPane.add(player.getImageView(),player.getX(),player.getY());

        Image image2 = new Image(player2.getUrl());
        player2.setImageView(new ImageView(image2));
        gridPane.add(player2.getImageView(),player2.getX(),player2.getY());

//        for (int i = 0; i < 8; i++) {
//            Creature creature = creatureFactory.newMonster();
//            Image mimage = new Image(creature.getUrl());
//            creature.setImageView(new ImageView(mimage));
//            gridPane.add(creature.getImageView(),creature.getX(),creature.getY());
//            Thread t = new Thread(creature.getAI());
//            t.start();
//        }//幽灵小怪
    }

    void setHpUI(Creature myPlayer,int i,Rectangle rect,String name){
        Label hp = new Label(name+"HP:");
        hp.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        hp.setLayoutX(20);
        hp.setLayoutY(i);
        myPane.getChildren().add(hp);

        Rectangle r = new Rectangle(150, i, myPlayer.getHp()*2 , 20);
        r.setArcHeight(15);
        r.setArcWidth(15);
        myPane.getChildren().add(r);//血条底框


//        rect = new Rectangle(50, i, myPlayer.getHp()*2 , 20);
//        rect.setArcHeight(15);
//        rect.setArcWidth(15);
//        rect.setFill(Color.RED);
//        myPane.getChildren().add(rect);//血条
    }



    public void HpUpdate(){
        Platform.runLater(()->{
            myPane.getChildren().remove(rect1);
            rect1 = new Rectangle(150, 550, player.getHp()*2 , 20);
            rect1.setArcHeight(15);
            rect1.setArcWidth(15);
            rect1.setFill(Color.RED);
            myPane.getChildren().add(rect1);


            myPane.getChildren().remove(rect2);
            rect2 = new Rectangle(150, 650, player2.getHp()*2 , 20);
            rect2.setArcHeight(15);
            rect2.setArcWidth(15);
            rect2.setFill(Color.RED);
            myPane.getChildren().add(rect2);
        });
    }

    public World createWorld(){
        return  new  WorldBuilder(width, height).makeCaves().build();
    }

    public void load(){
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                Image image = new Image(myWorld.tile(i,j).getUrl());
                ImageView imageView = new ImageView(image);
                gridPane.add(imageView,i,j);
            }
    }

    public Pane update(){
        myWorld.update();
        HpUpdate();

        if(player.getHp() <= 0 || player2.getHp()<0)
            alive=false;

        return myPane;
    }

    public Pane returnPane(){
        return myPane;
    }


}
