package com.agan.socket.chat;

import java.net.Socket;
import java.util.List;
import java.util.Map;

class MyThread extends Thread{
    private Socket socket;
    private List<Socket> sockets;

    public MyThread(Socket socket, List<Socket> sockets) {
        this.socket = socket;
        this.sockets = sockets;
    }

    // 循环处理转发数据
    @Override
    public void run() {
        String descClient = "[" + socket.getRemoteSocketAddress() + "]";
        try {
            System.out.println(descClient + "上线了");
            while(true) {
                System.out.println("上线主机列表：" + sockets);

                Map<Byte, byte[]> map = Utils.read(socket);
                Byte type = Utils.getMapKey(map);
                byte[] data = Utils.getMapValue(map);

                System.out.println(socket.getInetAddress() + ": 数据类型--" + type + "， 数据--" + new String(data));

                // 群发消息，排除发出消息的客户端的群发
                for(Socket s : sockets) {
                    if(s == socket) {
                        continue;
                    }
                    if(type == Utils.STRINGTYPE) {
                        Utils.write(descClient + ":" + new String(data), s, type);
                    }else if(type == Utils.FILETYPE){
                        Utils.write(descClient + "发送文件", s, Utils.STRINGTYPE);
                        Utils.write(data, s, type);
                    }
                }
                if("exit".getBytes().equals(data) || data == null) {
                    break;
                }
            }
            // 客户端正常下线通知
            sockets.remove(socket);
            socket.close();
            Server.offlineNotice(socket, "下线了");
            System.out.println(descClient + ":下线了");
        } catch (Exception e) {
            // 客户端异常正常下线通知
            e.printStackTrace();
            sockets.remove(socket);
            Server.offlineNotice(socket, "异常下线了");
            System.out.println(descClient + "异常下线了");
        }
    }
}
