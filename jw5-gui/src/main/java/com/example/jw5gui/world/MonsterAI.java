package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;


import java.util.Random;

public class MonsterAI extends CreatureAI{
    private CreatureFactory factory;

    public MonsterAI(Creature creature, GridPane pane, CreatureFactory factory){
        super(creature,pane);
        this.factory = factory;
    }

    @Override
    public  void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    @Override
    public void onUpdate() {
        Platform.runLater(()->{
            pane.getChildren().remove(creature.getImageView());
            pane.add(creature.getImageView(),creature.getX(),creature.getY());
            }
        );
    }


    @Override
    public void run(){
        while(this.creature.getHp()>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Random rand = new Random();
            switch(rand.nextInt(4)){
                case 0:
                    this.creature.moveBy(1,0);
                    break;
                case 1:
                    this.creature.moveBy(-1,0);
                    break;
                case 2:
                    this.creature.moveBy(0,1);
                    break;
                case 3:
                    this.creature.moveBy(0,-1);
                    break;
            }
        }
    }
}