package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Fire extends AttackThing{

    int time = 10;
    int attackValue = 20;

    public Fire(String url, World world, GridPane pane, int x, int y, PlayerAI owner) {
        super(url, world, pane, x, y, owner);
    }


    @Override
    public void attack(Creature other){
        synchronized(other){
            int damage = Math.max(0, this.attackValue - other.defenseValue());
            damage = (int) (Math.random() * damage) + 1;

            other.modifyHP(-damage);
//            this.notify("You attack the '%s' for %d damage.", other.glyph, damage);
//            other.notify("The '%s' attacks you for %d damage.", glyph, damage);
        }
    }

    @Override
    public void update(){
        this.time--;
        if(time <= 0)
            this.alive = false;


        Creature other = world.creature(x,y);
        if(other != null&& other != this.owner.creature){
            attack(other);
        }


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
