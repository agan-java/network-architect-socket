package com.agan.socket.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public static void main(String args[]) {
        FileServer server = new FileServer();
        server.init();
    }

    public void init() {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            //第一步：服务器建立通信ServerSocket，并绑定端口号监听8080端口号
            serverSocket = new ServerSocket(port);
            while (true) {
                //第二步：服务器建立Socket接收客户端连接，accept代表尝试接收客户端Socket的连接请求.(注意:该accept方法是阻塞式的)
                Socket server = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(server);
            }
        } catch (Exception e) {
            System.out.println("服务端异常:" + e.getMessage());
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }


    private class HandlerThread implements Runnable {
        private Socket server;
        private DataInputStream dis;
        private FileOutputStream fos;

        public HandlerThread(Socket socket) {
            server = socket;
            new Thread(this).start();
        }

        public void run() {
            try {
                //第三步：建立IO输入流，读取客户端发送的数据，只要跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
                dis = new DataInputStream(server.getInputStream());

                // 发过来的文件名
                String fileName = dis.readUTF();
                // 发过来的文件大小
                long fileLength = dis.readLong();
                //接受过来存储的目录
                File directory = new File("e:\\files");
                //判断是否存在目录，没有的话就创建
                if (!directory.exists()) {
                    directory.mkdir();
                }

                //路径分隔符 File.separatorChar ，Windows下的路径分隔符"\\" 和 Linux下的路径分隔符"/"
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);

                // 开始接收文件
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }
                System.out.println("======== 文件接收成功 [File Name：" + fileName + "] ========");
            } catch (Exception e) {
                System.out.println("服务端异常:" + e.getMessage());
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (fos != null)
                        fos.close();
                    if (server != null)
                        server.close();
                } catch (Exception e) {
                    System.out.println("finally 异常:" + e.getMessage());
                }
            }
        }
    }
}
