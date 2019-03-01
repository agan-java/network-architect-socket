package com.agan.socket.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
    // Socket列表，用于存放所有上线的用户Socket
    private static List<Socket> sockets;

    // 循环监听8888端口，一有连接就新建线程去处理，再继续监听端口
    public static void main(String[] args) {
        ServerSocket serverSocket;
        sockets = new ArrayList<Socket>();
        try {
            serverSocket = new ServerSocket(8888);
            System.out.println("服务器已启动，等待客户端连接");
            while (true) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                //通知所有客户端，有新用户上线
                offlineNotice(socket, "上线了");
                new MyThread(socket, sockets).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 通知所有客户端，有新用户上线
     */
    public static void offlineNotice(Socket socket, String message) {
        String descClient = "[" + socket.getRemoteSocketAddress() + "]";
        byte[] userByte = getUsersByte();
        for (Socket s : sockets) {
            Utils.write(descClient + message, s, Utils.STRINGTYPE);
            Utils.write(userByte, s, Utils.USERSTYPE);
        }
    }

    // 将Sockets取出其中的IP地址和端口，转成字符串添加到List中，再序列化成byte[]返回
    public static byte[] getUsersByte() {
        byte[] userByte = null;
        try {
            List<String> users = new ArrayList<String>();
            for (Socket s : sockets) {
                users.add("[" + s.getRemoteSocketAddress() + "]");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(users);

            oos.close();
            baos.close();
            userByte = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userByte;
    }
}


