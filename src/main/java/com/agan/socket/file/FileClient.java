package com.agan.socket.file;


import java.io.*;
import java.net.Socket;

public class FileClient {

    public static void main(String args[]) {
        File file = new File("E:\\file\\漫谈spring cloud微服务架构.txt");
        String host = "127.0.0.1";  //要连接的服务端IP地址
        int port = 8080;   //要连接的服务端对应的监听端口
        Socket client = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            //第一步：创建Socket通信，设置通信服务端的IP和Port
            client = new Socket(host, port);

            //第二步：建立IO输出流向服务器发送数据消息
            dos = new DataOutputStream(client.getOutputStream());
            // 文件名和长度
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();

            // 开始传输文件
            System.out.println("======== 开始传输文件 ========");
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;

            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            System.out.println("======== 文件传输成功 ========");
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (dos != null)
                    dos.close();
                if (client != null)
                    client.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }
}
