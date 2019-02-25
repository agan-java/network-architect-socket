package com.agan.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {


    public static void main(String[] args) {
        byte[] data = "hello world".getBytes();
        DatagramSocket datagramSocket = null;
        try {
            // 第一步，创建 DatagramSocket 实例，由于 UDP 是无连接的，不需要指定服务器的 IP 地址和端口号。
            datagramSocket = new DatagramSocket();

            // 第二步，创建用于发送和接收的数据包。可以注意到，用于发送和接收的 DatagramPacket 实例的创建方式是不同的。
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8889);

            // 第三步，使用 DatagramSocket 实例的 send(DatagramPacket p) 发送数据包。
            datagramSocket.send(sendPacket);

            // 第四步，使用 DatagramSocket 实例 receive(DatagramPacket p) 来接收服务器返回的数据包。
            DatagramPacket receivePacket = new DatagramPacket(new byte[data.length], data.length);
            datagramSocket.receive(receivePacket);
            System.out.println("Server said: " + new String(receivePacket.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. close datagram socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}



