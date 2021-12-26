package com.example.jw5gui.world;

import javafx.scene.layout.GridPane;

public class FireAttackThing extends AttackThing {

    int attackValue=10;
    Direction dir;

    public FireAttackThing(String url, World world, GridPane pane, int x, int y, Direction dir, PlayerAI owner) {
        super(url, world, pane, x, y,owner);
        this.dir=dir;
        attackValue += owner.creature.attackValue()/2;
    }

    public void attack(Creature other){
        synchronized(other){
            int damage = Math.max(0, this.attackValue - other.defenseValue());
            damage = (int) (Math.random() * damage) + 1;

            other.modifyHP(-damage);
            setAlive(false);
//            this.notify("You attack the '%s' for %d damage.", other.glyph, damage);
//            other.notify("The '%s' attacks you for %d damage.", glyph, damage);
        }

    }

    @Override
    public void run() {
        while (alive){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (dir) {
                case Up -> {moveBy(0,-1);break;}
                case Down ->{ moveBy(0,1);break;}
                case Left -> {moveBy(-1,0);break;}
                case Right -> {moveBy(1,0);break;}
            }
        }
    }
}

