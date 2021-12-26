package com.example.jw5gui.world;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World {

    public Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Thing> things;

    public final static int TILE_TYPES=5;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.things = new ArrayList<>();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public void change(int x, int y) {
        if(this.tile(x,y)==Tile.WALL) {
            this.tiles[x][y] = Tile.FLOOR;
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public void addAtEmptyLocation(Thing thing) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);
        thing.setX(x);
        thing.setY(y);

        this.things.add(thing);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.getX() == x && c.getY() == y) {
                return c;
            }
        }
        return null;
    }

    public Thing thing(int x, int y) {
        for (Thing t : this.things) {
            if( t.getX()== x && t.getY() == y)
                return t;
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public List<Thing> getThings() {
        return this.things;
    }

    public void remove(Creature target) {
        this.creatures.remove(target);
    }

    public void remove(Thing target) { this.things.remove(target); }

    public void update() {
        ArrayList<Creature> toUpdateCreature = new ArrayList<>(this.creatures);
        ArrayList<Thing> toUpdateThing = new ArrayList<>((this.things));

        for (Creature creature : toUpdateCreature) {
            creature.update();
        }

        for (Thing thing : toUpdateThing) {
            thing.update();
        }
    }
}
