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


import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureAI implements Runnable{

    protected Creature creature;
    public GridPane pane;

    public CreatureAI(Creature creature,GridPane pane) {
        this.pane=pane;
        this.creature = creature;
        this.creature.setAI(this);
    }

    public void onEnter(int x, int y, Tile tile) {
    }

    public void handleThing(Thing t){
        t.setAlive(false);
    }

    public void onUpdate() {
    }

    public void onNotify(String message) {
    }


    @Override
    public void run(){}
}
