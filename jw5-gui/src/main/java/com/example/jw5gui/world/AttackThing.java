package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class AttackThing extends Thing implements Runnable{


    PlayerAI owner;
    public AttackThing(String url, World world, GridPane pane, int x, int y,PlayerAI owner) {
        super(url,world,pane,x,y);
        this.owner = owner;
    }

    public void moveBy(int mx, int my) {
        synchronized(this.world) {
            if (!world.tile(x + mx, y + my).isGround())
               alive=false;
            else {
                Creature other = world.creature(x + mx, y + my);

                if (other == null) {
                    this.x+=mx;
                    this.y+=my;
                } else {
                    if(other.getAI() != owner){
                        attack(other);
                    }
                }
            }
        }
    }

    public void attack(Creature other){

    }


    @Override
    public void run() {

    }

}
