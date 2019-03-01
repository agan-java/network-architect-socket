package com.agan.socket.chat;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

 // 负责ChatUI后台与服务器Server进行通信
public class Client {
    private Socket socket = null;

    public Client(final ChatUI chatui) {
        getSocket();
        System.out.println("等待读取数据");

         // 启动一个守护线程，用于循环读取数据
        Thread t = new Thread() {
            public void run() {
                try {
                    while(true) {
                        Map<Byte, byte[]> map = Utils.read(socket, chatui);
                        Byte type = Utils.getMapKey(map);
                        byte[] buffer = Utils.getMapValue(map);
                     // 检测获得的数据是否是字符串类型
                        if(type == Utils.STRINGTYPE) {
                         // 检测是否满足退出条件
                            if("exit".equals(new String(buffer)) || buffer == null) {
                                System.out.println("连接关闭");
                                socket.close();
                                chatui.chatArea.setText(chatui.chatArea.getText() + "连接关闭" + "\r\n");
                                break;
                            }
                         // 将字符串直接写入到聊天窗口中
                            System.out.println("STRINGTYPE = " + new String(buffer));
                            chatui.chatArea.setText(chatui.chatArea.getText() + new String(buffer) + "\r\n");
                        }else if(type == Utils.USERSTYPE){  // 检测是否是用户列表类型
                                // 用户列表数据是经过序列化成byte的数据，所以要先转换为对象
                            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                            ObjectInputStream ois = new ObjectInputStream(bais);
                            List<String> usersList =  (List<String>) ois.readObject();
                            String d = "";
                            for(String str : usersList) {
                                d = d + str + "\r\n";
                            }
                         // 读取所有的用户数据，构造好字符串，填入好友列表框中
                            chatui.friendsArea.setText(d);
                            ois.close();
                            bais.close();

                        }else if(type == Utils.FILETYPE) {  // 检查是否是文件类型
                                // 是文件类型，需要反重构，将文件名称和文件数据分别取出来
                                // 前四个字节是文件名称的长度，接下来是文件名称，最后就是文件数据了
                            System.out.println("接收到文件了：" + new String(buffer));
                         // 文件名称长度
                            byte[] lenByte = new byte[4];
                            System.arraycopy(buffer, 0, lenByte, 0, 4);
                         // 将byte[]转成int类型
                            int lenName = Utils.byte2Int(lenByte);
                            System.out.println("lenName = " + lenName);

                         // 构造一个特定长度的byte[]，取出文件名称
                            byte[] nameByte = new byte[lenName];
                            System.arraycopy(buffer, 4, nameByte, 0, lenName);
                         // 构造文件数据长度的byte[]，取出文件数据
                            byte[] bufferData = new byte[buffer.length-4-lenName];
                            System.arraycopy(buffer, 4+lenName, bufferData, 0, bufferData.length);
                         // 将文件数据写入文件中
                            FileOutputStream fos = new FileOutputStream("E://TestCase//day20//" + new String(nameByte));
                            fos.write(bufferData);
                            fos.close();
                         // 在聊天窗口中显示文件保存位置
                            chatui.chatArea.setText(chatui.chatArea.getText() + "文件保存在E://TestCase//day20//" + new String(nameByte) + "\r\n");
                            System.out.println("文件保存位置 E://TestCase//day20//" + new String(nameByte));
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        chatui.chatArea.setText(chatui.chatArea.getText() + "异常，连接已关闭" + "\r\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
         // 设置为守护线程，并启动
        t.setDaemon(true);
        t.start();
    }

     // 检测socket是否为null，是则创建，可以避免创建多个socket
    public Socket getSocket() {
        if(socket == null) {
            try {
                socket = new Socket("localhost", 8888);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("服务器没有开启");
                System.exit(-1);
            }
        }
        return socket;
    }

     // 关闭socket方法
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
