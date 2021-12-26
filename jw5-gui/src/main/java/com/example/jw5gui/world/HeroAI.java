package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class HeroAI extends PlayerAI{
    public HeroAI(Creature creature, GridPane pane, CreatureFactory factory) {
        super(creature, pane, factory);
    }

    @Override
    public void skill(String key){
        FireAttackThing fire = null;
        if(key.equals("D")) {
            fire= factory.getThingFactory().newFireBullet(Direction.Right, creature.getX() + 1, creature.getY(),this);
        }else if(key.equals("A")){
            fire = factory.getThingFactory().newFireBullet(Direction.Left, creature.getX() -1, creature.getY(),this);
        }else if(key.equals("W")){
            fire = factory.getThingFactory().newFireBullet(Direction.Up, creature.getX() , creature.getY()-1,this);
        }else if(key.equals("S")){
            fire= factory.getThingFactory().newFireBullet(Direction.Down, creature.getX() , creature.getY()+1,this);
        }else if(key.equals("Q")) {
            creature.setHp(creature.getHp()/2);
            creature.setAttackValue(creature.attackValue()+100);
        }else if(key .equals("E")){
            switch(creature.lastDir){
                case 1:{
                    while(creature.moveBy(0,-1)){
                        factory.getThingFactory().newFire(creature.getX() , creature.getY(),this);
                    }break;
                }
                case 2:{
                    while(creature.moveBy(0,1)){
                        factory.getThingFactory().newFire(creature.getX() , creature.getY(),this);
                    }break;
                }
                case 3:{
                    while(creature.moveBy(-1,0)){
                        factory.getThingFactory().newFire(creature.getX() , creature.getY(),this);
                    }break;
                }
                case 4:{
                    while(creature.moveBy(1,0)){
                        factory.getThingFactory().newFire(creature.getX() , creature.getY(),this);
                    }break;
                }
            }

        }


        if(fire != null){
            new Thread(fire).start();
        }

    }
}
