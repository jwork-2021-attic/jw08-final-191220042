package com.example.jw5gui.parallel;

import com.example.jw5gui.ParallelGame;
import com.example.jw5gui.world.*;
import javafx.application.Application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class EchoNIOServer {
    int width = 12;
    int height = 12;
    int i = 1;

    private Selector selector;

    private InetSocketAddress listenAddress;
    private final static int PORT = 9093;

    private List<SocketChannel> myChannels = new ArrayList<SocketChannel>();

    public ManyModels manyModels =new ManyModels();
    public static void main(String[] args) throws Exception {



        new Thread(()->{Application.launch(ParallelGame.class);}).start();

        try {
            new EchoNIOServer("localhost", 9093).startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public EchoNIOServer(String address, int port) throws IOException {
            listenAddress = new InetSocketAddress(address, PORT);
        }


    private void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // bind server socket channel to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("EchoNIOServer started on port >> " + PORT);

        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true){
                try {
                    Thread.sleep(125);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i > 1){//?
                    for (SocketChannel channels : myChannels) {
                        try {
                            this.write(channels);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();

        while (true) {
            // wait for events
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }


            // process selected keys...
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();

                // Remove key from set so we don't process it twice
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) { // Accept client connections
                    this.accept(key);
                } else if (key.isReadable()) { // Read from client
                    this.read(key);
                } else if (key.isWritable()) {
                }

            }
        }
    }

    // accept client connection
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);


        i++;
        myChannels.add(channel);

        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        /*
         * Register channel with selector for further IO (record it for read/write
         * operations, here we have used read operation)
         */
        channel.register(this.selector, SelectionKey.OP_READ);
        channel.keyFor(selector).attach(i);

    }

    private  void write(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(40960);

        Tile myTile[][] = ParallelGame.multiPlayer.myWorld.tiles;
        int creatureCount = ParallelGame.multiPlayer.myWorld.getCreatures().size();
        int thingCount = ParallelGame.multiPlayer.myWorld.getThings().size();

        List<Creature> creatures = ParallelGame.multiPlayer.myWorld.getCreatures();
        List<Thing> things = ParallelGame.multiPlayer.myWorld.getThings();
        manyModels.myModels = new Model[width*height+creatureCount+thingCount+1];
        for (int i=0;i<width;i++)
            for(int j=0;j<height;j++){
                Model model = new Model(i,j,myTile[i][j].getUrl());
                manyModels.myModels[i*height+j] = model;
            }

        for (int i = 0; i < creatureCount; i++){
            Creature creature = creatures.get(i);
            Model model = new Model(creature.getX(),creature.getY(),creature.getUrl());
            manyModels.myModels[width*height+i] = model;
        }

        for (int i = 0; i < thingCount; i++){
            Thing thing = things.get(i);
            Model model = new Model(thing.getX(),thing.getY(),thing.getUrl());
            manyModels.myModels[width*height+creatureCount+i] = model;
        }

        Model model = new Model(ParallelGame.multiPlayer.player.getHp(),ParallelGame.multiPlayer.player2.getHp(),"blood");
        manyModels.myModels[width*height+creatureCount+thingCount] = model;
        buffer.put(Utils.getBytes(manyModels));
        buffer.flip();

        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.clear();
    }

    // read from the socket channel
    private void read(SelectionKey key) throws IOException {
        Object x = key.attachment();
        //System.out.println((int)x);


        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        //System.out.println("Got: " + new String(data));

        if((int)x == 2) {
            ParallelGame.responseToUser(new String(data));
        }else{
            ParallelGame.responseToOtherUser(new String(data));
        }
    }


}
