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

import java.io.*;
import java.util.Arrays;
import java.util.List;


public class SingleGame {
    private Pane myPane;
    private GridPane gridPane;
    private Rectangle rect;
    World myWorld;
    private Creature player;

    int width = 35;
    int height = 30;
    Boolean alive = true;

    ObjectOutputStream os = null;
    ObjectInputStream is = null;

    CreatureFactory creatureFactory;


    public SingleGame(Pane pane){
        this.myPane=pane;
        myPane.getChildren().clear();

        myWorld=createWorld();

        gridPane = new GridPane();
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(20);

        for(int i=0; i<width+1; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(22));
            gridPane.getRowConstraints().add(new RowConstraints(22));
        }

        load();

        ThingFactory thingFactory = new ThingFactory(this.myWorld,gridPane);
        creatThings(thingFactory);
        creatureFactory = new CreatureFactory(this.myWorld,gridPane,thingFactory);
        createCreatures(creatureFactory);

        setHpUI();

        myPane.getChildren().add(gridPane);
    }

    void restore(){
        try {
            is = new ObjectInputStream(new FileInputStream("a.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object manyModels = null;
        try {
            manyModels = is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ManyModels restoreModels = ((ManyModels)manyModels);

        for (int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Model model = restoreModels.myModels[i*height+j];
                if(model.url.equals("floor.png"))
                    myWorld.tiles[i][j] = Tile.FLOOR;
                else
                    myWorld.tiles[i][j] = Tile.WALL;
            }

        myWorld.getCreatures().clear();
        myWorld.getThings().clear();
        myWorld.getCreatures().add(player);

        for(int i = height*width;i<restoreModels.myModels.length;i++ ){
            Model model = restoreModels.myModels[i];
            if(model.url.equals("monster.png")){
                Creature creature = creatureFactory.newMonster(model.x, model.y);
                Image mimage = new Image(creature.getUrl());
                creature.setImageView(new ImageView(mimage));
                gridPane.add(creature.getImageView(),creature.getX(),creature.getY());
                Thread t = new Thread(creature.getAI());
                t.start();
            }else if(model.url.equals("blood.png")) {
                creatureFactory.getThingFactory().newBlood(model.x, model.y);
            }else {
                player.setX(model.x);
                player.setY(model.y);

            }

        }

        load();

    }

    public void respondToKeyEvent(KeyEvent key) {

       if(key.getCode() == KeyCode.LEFT) {
           player.moveBy(-1, 0);
           player.lastDir = 3;
       }else if(key.getCode() == KeyCode.RIGHT) {
           player.moveBy(1, 0);
           player.lastDir = 4;
       }else if(key.getCode() == KeyCode.UP) {
           player.moveBy(0, -1);
           player.lastDir = 1;
       }else if(key.getCode() == KeyCode.DOWN){
           player.moveBy(0, 1);
           player.lastDir = 2;
       }else {
           ((PlayerAI)player.getAI()).skill(key.getCode().getName());

       }
    }

    private void creatThings(ThingFactory thingFactory){

    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newOtherPlayer();
        player.setX(0);
        player.setY(0);

        Image image = new Image(player.getUrl());
        player.setImageView(new ImageView(image));
        gridPane.add(player.getImageView(),player.getX(),player.getY());

        for (int i = 0; i < 4; i++) {
            Creature creature = creatureFactory.newMonster();
            Image mimage = new Image(creature.getUrl());
            creature.setImageView(new ImageView(mimage));
            gridPane.add(creature.getImageView(),creature.getX(),creature.getY());
            Thread t = new Thread(creature.getAI());
            t.start();
        }//幽灵小怪

        for (int i = 0; i < 4; i++) {
           creatureFactory.getThingFactory().newBlood();
        }
    }

    void setHpUI(){
        Label hp = new Label("HP:");
        hp.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        hp.setLayoutX(20);
        hp.setLayoutY(750);
        myPane.getChildren().add( hp);

        Rectangle r = new Rectangle(50, 750, player.getHp()*2 , 20);
        r.setArcHeight(15);
        r.setArcWidth(15);
        myPane.getChildren().add(r);//血条底框


        rect = new Rectangle(50, 750, player.getHp()*2 , 20);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.RED);
        myPane.getChildren().add(rect);//血条
    }

    public void HpUpdate(){
        Platform.runLater(()->{
            myPane.getChildren().remove(rect);
            rect = new Rectangle(50, 750, player.getHp()*2 , 20);
            rect.setArcHeight(15);
            rect.setArcWidth(15);
            rect.setFill(Color.RED);
            myPane.getChildren().add(rect);
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

        if(player.getHp() <= 0)
            alive=false;

        return myPane;
    }

    public  Pane lose(){
        Platform.runLater(()->{
            myPane.getChildren().clear();
            Label lose = new Label("YOU LOSE");
            lose.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            lose.setLayoutX(420);
            lose.setLayoutY(450);
            myPane.getChildren().add(lose);
        });

        return myPane;
    }

    public Pane returnPane(){
        return myPane;
    }

    public void record() throws IOException {

//        try {
//            os = new ObjectOutputStream(new FileOutputStream("a.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for(int i = 0; i < width; i++)
//            for(int j = 0; j < height; j++) {
//               os.writeObject(myWorld.tiles[i][j]);
//            }
//
//
//        myWorld.getCreatures().stream().forEach((cre)->{
//            try {
//                os.writeObject(cre);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        myWorld.getThings().stream().forEach((thi)->{
//            try {
//                os.writeObject(thi);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        os.flush();
//        os.close();
        List<Creature> creatures = myWorld.getCreatures();
        List<Thing> things = myWorld.getThings();
        ManyModels manyModels = new ManyModels();
        manyModels.myModels = new Model[width*height+creatures.size()+things.size()];

        for (int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Model model = new Model(i,j,myWorld.tile(i,j).getUrl());
                manyModels.myModels[i*height+j] = model;
            }

        for (int i = 0; i < creatures.size(); i++){
            Creature creature = creatures.get(i);
            Model model = new Model(creature.getX(),creature.getY(),creature.getUrl());
            manyModels.myModels[width*height+i] = model;
        }

        for (int i = 0; i < things.size(); i++){
            Thing thing = things.get(i);
            Model model = new Model(thing.getX(),thing.getY(),thing.getUrl());
            manyModels.myModels[width*height+creatures.size()+i] = model;
        }


        try {
            os = new ObjectOutputStream(new FileOutputStream("a.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        os.writeObject(manyModels);
        os.flush();
        os.close();
    }
}
