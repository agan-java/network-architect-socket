package com.agan.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

    public static void main(String[] args) {

        DatagramSocket datagramSocket = null;
        try {
            // 第一步，创建 DatagramSocket 实例，需要指定本地的端口，表明要获取本地哪个端口的数据包。
            datagramSocket = new DatagramSocket(9090);

            // 第二步，接收客户端发过来的数据包
            DatagramPacket receivePacket = new DatagramPacket(new byte[256], 256);
            datagramSocket.receive(receivePacket);
            System.out.println("Client said: " + new String(receivePacket.getData()));

            //第三步，响应客户端的请求，发响应数据给客户端；首先根据接收的数据包的源端 IP 地址的端口号来创建要发送的数据包，然后再发送出来。
            byte[] sendData = "Welcome!".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getSocketAddress());
            datagramSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}


