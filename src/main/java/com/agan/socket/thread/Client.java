package com.agan.socket.thread;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    public static void main(String args[]) {
        String host = "127.0.0.1";  //要连接的服务端IP地址
        int port = 8080;   //要连接的服务端对应的监听端口
        Socket client = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            //第一步：创建Socket通信，设置通信服务端的IP和Port
            client = new Socket(host, port);

            //第二步：建立IO输出流向服务器发送数据消息
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            //接收控制台的System.in数据
            String line = "";
            while (line.indexOf("EOF") == -1) {
                line = new BufferedReader(new InputStreamReader(System.in)).readLine();
                writer.write(line);
                writer.flush();
            }

            //第三步：建立IO输入流，读取服务器发送来的数据消息
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String mess = reader.readLine();
            System.out.println("接收到服务端发来的数据:：" + mess);
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
                if (client != null)
                    client.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }
}
