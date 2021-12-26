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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;
    private GridPane pane;
    private ThingFactory thingFactory;

    public ThingFactory getThingFactory() {
        return thingFactory;
    }

    public CreatureFactory(World world,GridPane pane,ThingFactory thingFactory) {
        this.world = world;
        this.pane = pane;
        this.thingFactory=thingFactory;
    }

    public Creature newPlayer() {
        Creature player = new Creature("hero.png",world);
        world.getCreatures().add(player);
        new MasterAI(player,pane,this);
        return player;
    }

    public Creature newOtherPlayer() {
        Creature player = new Creature("master.png",world);
        world.getCreatures().add(player);
        new HeroAI(player,pane,this);
        return player;
    }

    public Creature newMonster() {
        Creature monster = new Creature("monster.png",this.world);
        world.addAtEmptyLocation(monster);
        new MonsterAI(monster,pane,this);
        return monster;
    }

    public Creature newMonster(int x,int y) {
        Creature monster = new Creature("monster.png",this.world);
        monster.setX(x);
        monster.setY(y);

        world.getCreatures().add(monster);
        new MonsterAI(monster,pane,this);
        return monster;
    }


}
