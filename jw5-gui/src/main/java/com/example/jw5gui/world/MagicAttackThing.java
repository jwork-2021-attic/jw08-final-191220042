package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MagicAttackThing extends AttackThing{

    int time = 10;
    int attackValue = 20;
    public MagicAttackThing(String url, World world, GridPane pane, int x, int y,PlayerAI owner) {
        super(url, world, pane, x, y,owner);
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

        for(int i=x;i<=x+2;i++)
            for(int j=y-2;j<=y+2;j++){
                if (world.tile(i,j) != Tile.BOUNDS){
                    Creature other = world.creature(i,j);
                    if(other != null&& other != this.owner.creature)
                        attack(other);
                }
            }

        Platform.runLater(()->{
            pane.getChildren().remove(imageView);
            if(alive == true) {
                pane.add(imageView, x, y);
            }else{
                world.remove(this);
                for(int i=x;i<=x+2;i++)
                    for(int j=y-2;j<=y+2;j++){
                        if (world.tile(i,j) == Tile.WALL) {
                            world.change(i,j);
                            changeTile(i,j);
                        }
                    }
            }
        });
    }

    public void changeTile(int x,int y){
        Platform.runLater(()->{
            Image image = new Image(world.tile(x,y).getUrl());
            ImageView imageView = new ImageView(image);
            pane.add(imageView,x,y);
        });
    }
}
