/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.example.jw5gui.world;


import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.Serializable;


public class Creature implements Serializable {

    transient private World world;

    transient private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public World getWorld() {
        return world;
    }

    public int lastDir = 3;

    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    String url;

    public String getUrl() {
        return url;
    }

    private CreatureAI ai;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    public CreatureAI getAI() {
        return ai;
    }

    public Creature(String url,World world){
        this.url = url;
        this.world = world;
        this.maxHP=100;
        this.hp=maxHP;
        this.attackValue=50;
        this.defenseValue=0;
    }

    private int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    private int hp;

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        this.hp += amount;

        if (this.hp < 1) {
            world.remove(this);
            Platform.runLater(()->{
                getAI().pane.getChildren().remove(this.getImageView());
            });
        }
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }
    public  void setAttackValue(int attackValue){
        this.attackValue = attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public Boolean moveBy(int mx, int my) {
        synchronized(this.world) {
            if (!world.tile(x + mx, y + my).isGround())
                return false;
            else {
                Creature other = world.creature(x + mx, y + my);
                Thing t = world.thing(x+mx,y+my);

                if(t != null){
                    ai.handleThing(t);
                }
                if (other == null) {
                    ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
                    return true;
                } else {
                    attack(other);
                    return false;
                }
            }
        }
    }

    public void attack(Creature other) {
        synchronized(other){
            int damage = Math.max(0, this.attackValue() - other.defenseValue());
            damage = (int) (Math.random() * damage) + 1;

            other.modifyHP(-damage);

//            this.notify("You attack the '%s' for %d damage.", other.glyph, damage);
//            other.notify("The '%s' attacks you for %d damage.", glyph, damage);
        }
    }

    public void update() {
        this.ai.onUpdate();
    }

//    public void notify(String message, Object... params) {
//        ai.onNotify(String.format(message, params));
//    }


}
