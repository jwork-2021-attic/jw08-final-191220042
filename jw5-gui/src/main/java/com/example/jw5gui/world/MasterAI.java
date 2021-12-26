package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MasterAI extends PlayerAI{
    public MasterAI(Creature creature, GridPane pane, CreatureFactory factory) {
        super(creature, pane, factory);
    }

    @Override
    public void skill(String key){
        DragonAttackThing bullet = null;
        if(key.equals("D")) {
            bullet= factory.getThingFactory().newDragonBullet(Direction.Right, creature.getX() + 1, creature.getY(),this);
        }else if(key.equals("A")){
            bullet = factory.getThingFactory().newDragonBullet(Direction.Left, creature.getX() -1, creature.getY(),this);
        }else if(key.equals("W")){
            bullet = factory.getThingFactory().newDragonBullet(Direction.Up, creature.getX() , creature.getY()-1,this);
        }else if(key.equals("S")){
            bullet = factory.getThingFactory().newDragonBullet(Direction.Down, creature.getX() , creature.getY()+1,this);
        }else if(key.equals("Q")){
            factory.getThingFactory().newMagic(creature.getX() + 2, creature.getY(),this);
        }else if(key.equals("E")){
            for (int i = 0; i < 4; i++) {
                Creature creature = factory.newMonster();
                Image mimage = new Image(creature.getUrl());
                creature.setImageView(new ImageView(mimage));
                Platform.runLater(()->{
                    pane.add(creature.getImageView(),creature.getX(),creature.getY());
                });
                Thread t = new Thread(creature.getAI());
                t.start();
            }
        }

        if(bullet != null){
            new Thread(bullet).start();
        }
    }
}
