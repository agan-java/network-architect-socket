package com.agan.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    private static final int ECHOMAX = 255;

    public static void main(String[] args) {

        DatagramSocket datagramSocket = null;
        try {
            // 第一步，创建 DatagramSocket 实例，需要指定本地的端口，表明要获取本地哪个端口的数据包。
            datagramSocket = new DatagramSocket(8889);

            // 第二步，接收指定端口的数据包。
            DatagramPacket receivePacket = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
            datagramSocket.receive(receivePacket);
            System.out.println("Client said: " + new String(receivePacket.getData()));

            //第三步，首先根据接收的数据包的源端 IP 地址的端口号来创建要发送的数据包，然后再发送出来。
            byte[] sendData = "Welcome!".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    receivePacket.getSocketAddress());
            datagramSocket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();


        } finally {
            // 4. close datagram socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}


