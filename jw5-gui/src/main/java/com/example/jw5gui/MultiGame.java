package com.example.jw5gui;

import com.example.jw5gui.world.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class MultiGame {

    int width = 12;
    int height = 12;

    private GridPane gridPane;
    private byte[] data;
    private Pane myPane;
    public ByteBuffer writeBuffer ;

    public SocketChannel clientChannel = null;
    private Selector selector;

    private Rectangle rect1;
    private Rectangle rect2;

    public Boolean alive = true;

    int mycount;

    public ManyModels manyModels = new ManyModels();
    public MultiGame(Pane pane,int index){


        this.myPane = pane;
        mycount = index;
        myPane.getChildren().clear();


        gridPane = new GridPane();
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(20);

        for(int i=0; i<width+1; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(22));
            gridPane.getRowConstraints().add(new RowConstraints(22));
        }

        pane.getChildren().add(gridPane);

        writeBuffer = ByteBuffer.allocate(72);

        try {
            clientChannel = SocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SocketAddress address = new InetSocketAddress("localhost",9093);

        try {
            clientChannel.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }


        new Thread(()->{while (true){
            int readyChannels = 0;
            try {
                readyChannels = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();

                if(selectionKey.isReadable()){
                    try {
                        readHandler(selectionKey);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(selectionKey.isConnectable()){
                    connectHandler(selectionKey);
                }
            }
        }
        }).start();


        setHpUI(550,"Player1");
        setHpUI(650,"Player2");

        skillShow();


    }

    private void skillShow() {
        if(this.mycount == 1){
            Image image1 = new Image("mskill1.png");
            ImageView imageView1 = new ImageView(image1);
            imageView1.setLayoutX(500);
            imageView1.setLayoutY(80);
            myPane.getChildren().add(imageView1);

            Image image2 = new Image("mskill2.png");
            ImageView imageView2 = new ImageView(image2);
            imageView2.setLayoutX(500);
            imageView2.setLayoutY(250);
            myPane.getChildren().add(imageView2);

            Image image3 = new Image("mskill3.png");
            ImageView imageView3 = new ImageView(image3);
            imageView3.setLayoutX(500);
            imageView3.setLayoutY(450);
            myPane.getChildren().add(imageView3);


            Label skill1 = new Label("向指定方向发射冲击波，通过WSAD控制上下左右，冲击波会被地形阻挡 （50）");
            skill1.setPrefSize(400, 100); // 设置标签的推荐宽高
            skill1.setAlignment(Pos.CENTER); // 设置标签的对齐方式
            skill1.setWrapText(true);
            skill1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            skill1.setLayoutX(600);
            skill1.setLayoutY(50);
            myPane.getChildren().add(skill1);

            Label skill2 = new Label("按Q在前方释放持续一秒的亡灵法阵，对停留其中的生物造成大量伤害,可对地形造成破坏 （每0.1秒10）");
            skill2.setPrefSize(400, 100); // 设置标签的推荐宽高
            skill2.setAlignment(Pos.CENTER); // 设置标签的对齐方式
            skill2.setWrapText(true);
            skill2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            skill2.setLayoutX(600);
            skill2.setLayoutY(250);
            myPane.getChildren().add(skill2);

            Label skill3 = new Label("按E可在地图随机位置召唤4只亡灵");
            skill3.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            skill3.setLayoutX(600);
            skill3.setLayoutY(480);
            myPane.getChildren().add(skill3);
        }else {
                Image image1 = new Image("hskill1.png");
                ImageView imageView1 = new ImageView(image1);
                imageView1.setLayoutX(500);
                imageView1.setLayoutY(50);
                myPane.getChildren().add(imageView1);

                Image image2 = new Image("hskill2.png");
                ImageView imageView2 = new ImageView(image2);
                imageView2.setLayoutX(500);
                imageView2.setLayoutY(250);
                myPane.getChildren().add(imageView2);

                Image image3 = new Image("hskill3.png");
                ImageView imageView3 = new ImageView(image3);
                imageView3.setLayoutX(500);
                imageView3.setLayoutY(450);
                myPane.getChildren().add(imageView3);


                Label skill1 = new Label("按Q损失1/2的血量以提升自身高额攻击力");
                skill1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                skill1.setLayoutX(600);
                skill1.setLayoutY(50);
                myPane.getChildren().add(skill1);

                Label skill2 = new Label("向指定方向发射火焰，通过WSAD控制上下左右，火焰会被地形阻挡 （10+基础攻击力*50%）");
                skill2.setPrefSize(400, 100); // 设置标签的推荐宽高
                skill2.setAlignment(Pos.CENTER); // 设置标签的对齐方式
                skill2.setWrapText(true);
                skill2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                skill2.setLayoutX(600);
                skill2.setLayoutY(250);
                myPane.getChildren().add(skill2);

                Label skill3 = new Label("按E根据上一次的移动方向向前冲刺，在沿途路径上留下燃烧的火花，遇到第一个敌人会停止冲锋并对其造成高额伤害 （50）");
                skill3.setPrefSize(400, 100); // 设置标签的推荐宽高
                skill3.setAlignment(Pos.CENTER); // 设置标签的对齐方式
                skill3.setWrapText(true);
                skill3.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                skill3.setLayoutX(600);
                skill3.setLayoutY(450);
                myPane.getChildren().add(skill3);
        }
    }

    private void connectHandler(SelectionKey selectionKey) {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        if(socketChannel.isConnectionPending()) {
            try {
                socketChannel.finishConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    private void readHandler(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(40960);
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

        //System.out.println("Got: " + numRead);

        if(numRead!=-1) {
            try {
                manyModels = (ManyModels) Utils.getObject(data);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void  respondToKeyEvent(KeyEvent key){
        writeBuffer.put(key.getCode().getName().getBytes());
        writeBuffer.flip();
        try {
            clientChannel.keyFor(selector).attach(mycount);
            clientChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeBuffer.clear();
    }

    public Pane returnPane(){
        return myPane;
    }

    public Pane update() {
        Platform.runLater(()->{
            gridPane.getChildren().clear();
            if(manyModels.myModels != null) {
                for (int i = 0; i < manyModels.myModels.length-1; i++) {
                    Image image = new Image(manyModels.myModels[i].url);
                    ImageView imageView = new ImageView(image);
                    gridPane.add(imageView, manyModels.myModels[i].x, manyModels.myModels[i].y);
                }


                if(manyModels.myModels[manyModels.myModels.length-1].x <= 0){
                    resultShow("Player2");
                    alive = false;
                }else if(manyModels.myModels[manyModels.myModels.length-1].y <= 0) {
                    resultShow("Player1");
                    alive = false;
                }

                HpUpdate(manyModels.myModels[manyModels.myModels.length-1].x,manyModels.myModels[manyModels.myModels.length-1].y);

            }
        });


       return myPane;
    }

    private void resultShow(String player) {
        myPane.getChildren().clear();
        Label win = new Label(player+"  Win");
        win.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        win.setLayoutX(420);
        win.setLayoutY(450);
        myPane.getChildren().add(win);
    }

    void setHpUI(int i,String name) {
        Label hp = new Label(name + "HP:");
        hp.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        hp.setLayoutX(20);
        hp.setLayoutY(i);
        myPane.getChildren().add(hp);

        Rectangle r = new Rectangle(150, i, 100* 2, 20);
        r.setArcHeight(15);
        r.setArcWidth(15);
        myPane.getChildren().add(r);//血条底框
    }

    public void HpUpdate(int hp1,int hp2){
        Platform.runLater(()->{
            if(this.alive) {
                myPane.getChildren().remove(rect1);
                rect1 = new Rectangle(150, 550, hp1 * 2, 20);
                rect1.setArcHeight(15);
                rect1.setArcWidth(15);
                rect1.setFill(Color.RED);
                myPane.getChildren().add(rect1);


                myPane.getChildren().remove(rect2);
                rect2 = new Rectangle(150, 650, hp2 * 2, 20);
                rect2.setArcHeight(15);
                rect2.setArcWidth(15);
                rect2.setFill(Color.RED);
                myPane.getChildren().add(rect2);
            }
        });
    }
}
