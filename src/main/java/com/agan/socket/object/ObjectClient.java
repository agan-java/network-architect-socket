package com.agan.socket.object;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectClient {

    public static void main(String args[]) {
        String host = "127.0.0.1";  //要连接的服务端IP地址
        int port = 8080;   //要连接的服务端对应的监听端口
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            //第一步：创建Socket通信，设置通信服务端的IP和Port
            socket = new Socket(host, port);
            //第二步：建立IO输出流向服务器发送数据消息
            oos = new ObjectOutputStream(socket.getOutputStream());
            //向服务器端发送一条消息
            Student stu = new Student(1, "阿甘", 18);
            oos.writeObject(stu);
            oos.flush();

            //第三步：建立IO输入流，读取服务器发送来的数据消息
            ois = new ObjectInputStream(socket.getInputStream());

            stu = (Student) ois.readObject();
            System.out.println("接收到服务端发来的数据: " + stu);
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }
}
