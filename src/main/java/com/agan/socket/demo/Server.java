package com.agan.socket.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) {
        int port = 8080;
        ServerSocket serverSocket = null;
        BufferedReader reader = null;
        Writer writer = null;
        Socket server = null;
        try {
            //第一步：服务器建立通信ServerSocket，并绑定端口号监听8080端口号
            serverSocket = new ServerSocket(port);
            while (true) {
                //第二步：服务器建立Socket接收客户端连接，accept代表尝试接收客户端Socket的连接请求.(注意:该accept方法是阻塞式的)
                server = serverSocket.accept();

                //第三步：建立IO输入流，读取客户端发送的数据，只要跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
                reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String mess = reader.readLine();
                System.out.println("接收到客户端发来的数据: " + mess);

                //第四步：建立IO输出流，向客户端发送数据消息
                writer = new OutputStreamWriter(server.getOutputStream());
                writer.write("您好，我是服务端.\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("服务端异常:" + e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (server != null)
                    server.close();
                if (writer != null)
                    writer.close();
                if (serverSocket != null)
                    serverSocket.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }
}
