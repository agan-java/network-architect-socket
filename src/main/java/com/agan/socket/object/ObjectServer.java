package com.agan.socket.object;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ObjectServer {

    public static void main(String args[]) {
        int port = 8080;
        ServerSocket server = null;
        Socket socket = null;
        ObjectOutputStream oos=null;
        try {
            //第一步：服务器建立通信ServerSocket，并绑定端口号监听8080端口号
            server = new ServerSocket(port);
            while (true) {
                //第二步：服务器建立Socket接收客户端连接，accept代表尝试接收客户端Socket的连接请求.(注意:该accept方法是阻塞式的)
                socket = server.accept();

                //第三步：建立IO输入流，读取客户端发送的数据，只要跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Student stu = (Student)ois.readObject();
                System.out.println("接收到客户端发来的数据: " + stu);

                //第四步：建立IO输出流，向客户端发送数据消息
                oos = new ObjectOutputStream(socket.getOutputStream());
                //把student的age加上10岁返回给客户端
                stu.setAge(stu.getAge()+10);
                oos.writeObject(stu);
                oos.flush();
            }
        } catch (Exception e) {
            System.out.println("服务端异常:" + e.getMessage());
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (socket != null)
                    socket.close();
                if (server != null)
                    server.close();
            } catch (Exception e) {
                System.out.println("finally 异常:" + e.getMessage());
            }
        }
    }
}
