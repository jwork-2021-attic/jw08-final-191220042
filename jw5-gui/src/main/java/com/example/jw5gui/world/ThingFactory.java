package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ThingFactory {
    World world;
    GridPane pane;
    DragonAttackThing bullet=null;
    FireAttackThing fire=null;

    public ThingFactory(World world,GridPane pane){
        this.world = world;
        this.pane = pane;
    }

    public DragonAttackThing newDragonBullet(Direction dir, int x, int y,PlayerAI owner){

        if(world.tile(x,y) == Tile.BOUNDS)
            return null;

        if (!world.tile(x , y).isGround())
            return null;


        switch (dir) {
            case Left -> {bullet = new DragonAttackThing("dragonbullet-left.png", world, pane, x, y, dir,owner);break;}
            case Right -> {bullet = new DragonAttackThing("dragonbullet-right.png", world, pane, x, y, dir,owner);break;}
            case Up -> {bullet = new DragonAttackThing("dragonbullet-up.png", world, pane, x, y, dir,owner);break;}
            case Down -> {bullet = new DragonAttackThing("dragonbullet-down.png", world, pane, x, y, dir,owner);break;}
        }

        world.getThings().add(bullet);

        Image image = new Image(bullet.url);
        bullet.imageView=new ImageView(image);

//       Platform.runLater(()->{
//            pane.add(bullet.imageView, x,y);
//       });

        Creature other = world.creature(x , y );
        if(other != null)
            bullet.attack(other);

        return bullet;
    }

    public void newMagic(int x, int y,PlayerAI owner){
        MagicAttackThing magicAttackThing = new MagicAttackThing("master.gif", world, pane, x, y,owner);
        world.getThings().add(magicAttackThing);

        Image image = new Image(magicAttackThing.url);
        magicAttackThing.imageView=new ImageView(image);

//        Platform.runLater(()->{
//            pane.add(magicAttackThing.imageView, x,y);
//        });
    }
    public void newFire(int x, int y,PlayerAI owner){
        Fire fire= new Fire("hskill3.gif", world, pane, x, y,owner);
        world.getThings().add(fire);

        Image image = new Image(fire.url);
        fire.imageView=new ImageView(image);

//        Platform.runLater(()->{
//            pane.add(magicAttackThing.imageView, x,y);
//        });
    }

    public FireAttackThing newFireBullet(Direction dir, int x, int y,PlayerAI owner){

        if(world.tile(x,y) == Tile.BOUNDS)
            return null;

        if (!world.tile(x , y).isGround())
            return null;


        switch (dir) {
            case Left -> {fire= new FireAttackThing("fire-left.png", world, pane, x, y, dir,owner);break;}
            case Right -> {fire = new FireAttackThing("fire-right.png", world, pane, x, y, dir,owner);break;}
            case Up -> {fire = new FireAttackThing("fire-up.png", world, pane, x, y, dir,owner);break;}
            case Down -> {fire = new FireAttackThing("fire-down.png", world, pane, x, y, dir,owner);break;}
        }

        world.getThings().add(fire);

        Image image = new Image(fire.url);
        fire.imageView=new ImageView(image);

//       Platform.runLater(()->{
//            pane.add(bullet.imageView, x,y);
//       });

        Creature other = world.creature(x , y );
        if(other != null)
            fire.attack(other);

        return fire;
    }

    public void newBlood(){
       Blood blood= new Blood("blood.png", world, pane);
       world.addAtEmptyLocation(blood);

       Image image = new Image(blood.url);
       blood.imageView=new ImageView(image);
//        Platform.runLater(()->{
//            pane.add(magicAttackThing.imageView, x,y);
//        })
    }

    public void newBlood(int x,int y){
        Blood blood= new Blood("blood.png", world, pane);

        blood.setX(x);
        blood.setY(y);

        world.getThings().add(blood);

        Image image = new Image(blood.url);
        blood.imageView=new ImageView(image);
//        Platform.runLater(()->{
//            pane.add(magicAttackThing.imageView, x,y);
//        })
    }

}
