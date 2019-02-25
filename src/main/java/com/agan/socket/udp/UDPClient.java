package com.agan.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        try {
            // 第一步，创建 DatagramSocket 实例，由于 UDP 是无连接的，不需要指定服务器的 IP 地址和端口号。
            datagramSocket = new DatagramSocket();

            // 第二步，创建DatagramPacket，用于发送的数据包。注意，数据包需要指定服务端的IP和端口。
            byte[] request = "hello".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(request, request.length, InetAddress.getLocalHost(), 9090);

            // 第三步，使用DatagramSocket 向服务器，发送数据包。
            datagramSocket.send(sendPacket);

            // 第四步，使用 DatagramSocket 接收服务器返回的数据包。
            DatagramPacket receivePacket = new DatagramPacket(new byte[256], 256);
            datagramSocket.receive(receivePacket);
            System.out.println("Server said: " + new String(receivePacket.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}



