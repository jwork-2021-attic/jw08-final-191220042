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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


public class PlayerAI extends CreatureAI {

    protected CreatureFactory factory;

    public PlayerAI(Creature creature, GridPane pane, CreatureFactory factory) {
        super(creature,pane);
        this.factory=factory;
    }

    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }


    @Override
    public void handleThing(Thing t){
        if(t instanceof Blood){
            creature.setHp(100< creature.getHp()+30?100: creature.getHp()+30);
            t.setAlive(false);
        }
    }

    public void skill(String key){

    }


    @Override
    public void onUpdate() {
        Platform.runLater(
                ()-> {
            pane.getChildren().remove(creature.getImageView());
            pane.add(creature.getImageView(), creature.getX(), creature.getY());
            }
        );
    }


}
