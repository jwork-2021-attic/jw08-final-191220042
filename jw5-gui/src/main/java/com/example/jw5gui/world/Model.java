package com.example.jw5gui.world;

import java.io.*;
import java.nio.ByteBuffer;

public class Model implements Serializable {
    public int x;
    public int y;
    public String url;

    public Model(int x, int y,String url){
        this.x = x;
        this.y = y;
        this.url = url;
    }
}
