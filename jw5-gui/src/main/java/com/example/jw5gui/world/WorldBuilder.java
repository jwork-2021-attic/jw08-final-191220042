package com.example.jw5gui.world;


import com.example.jw5gui.maze.MazeGenerator;
import java.util.Random;

/*
 * Copyright (C) 2015 s-zhouj
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

/**
 *
 * @author s-zhouj
 */
public class WorldBuilder {

    private int width;
    private int height;
    private Tile[][] tiles;
    private int[][] map;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;

        MazeGenerator myMaze = new MazeGenerator(this.width);
        myMaze.generateMaze();
        map=myMaze.getMaze();

        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }

    private WorldBuilder randomizeTiles() {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                if(map[width][height]==1)
                    tiles[width][height] = Tile.FLOOR;
                else
                    tiles[width][height] = Tile.WALL;
            }
        }
        return this;
    }

    private WorldBuilder smooth(int factor) {
        Tile[][] newtemp = new Tile[width][height];
        if (factor > 1) {
            smooth(factor - 1);
        }
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                // Surrounding walls and floor
                int surrwalls = 0;
                int surrfloor = 0;

                // Check the tiles in a 3x3 area around center tile
                for (int dwidth = -1; dwidth < 2; dwidth++) {
                    for (int dheight = -1; dheight < 2; dheight++) {
                        if (width + dwidth < 0 || width + dwidth >= this.width || height + dheight < 0
                                || height + dheight >= this.height) {
                            continue;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.FLOOR) {
                            surrfloor++;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.WALL) {
                            surrwalls++;
                        }
                    }
                }
                Tile replacement;
                if (surrwalls > surrfloor) {
                    replacement = Tile.WALL;
                } else {
                    replacement = Tile.FLOOR;
                }
                newtemp[width][height] = replacement;
            }
        }
        tiles = newtemp;

//        for (int i=0;i<height;i++)
//            tiles[width-1][i] = Tile.BOUNDS;
//        for (int i=0;i<width;i++)
//            tiles[i][height-1] = Tile.BOUNDS;


//        Random rand = new Random();
//        for(int i=0;i<10;i++) {
//            int x=rand.nextInt(width);
//            int y=rand.nextInt(height);
//            tiles[x][y]=Tile.VISION;
//        }
//
//        for(int i=0;i<10;i++) {
//            int x=rand.nextInt(width);
//            int y=rand.nextInt(height);
//            tiles[x][y]=Tile.BLOOD;
//        }

        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(8);
    }
}
