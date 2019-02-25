package com.agan.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private static final int TIMEOUT = 3000;
    private static final int MAXTRIES = 5;

    public static void main(String[] args) {
        byte[] data = "hello world".getBytes();
        DatagramSocket datagramSocket = null;
        try {
            // 1. create a datagram socket
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(TIMEOUT); // receive timeout
            // 2. create datagram packet for sending and receiving
            DatagramPacket sendPacket = new DatagramPacket(data, data.length,
                    InetAddress.getLocalHost(), 8889);
            DatagramPacket receivePacket = new DatagramPacket(new byte[data.length], data.length);

            int tries = 0;// packets may be lost, so we have to keep trying
            boolean receivedResponse = false;
            do {
                // 3. send datagram packet
                datagramSocket.send(sendPacket);
                try {
                    // 4. receive datagram packet, blocking until receive reponse
                    datagramSocket.receive(receivePacket);
                    if (!receivePacket.getAddress().equals(sendPacket.getAddress())) {
                        throw new IOException("Received packet from an unknown source");
                    }
                    receivedResponse = true;
                } catch (IOException e) {
                    //5. if error, try again
                    tries += 1;
                    System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
                }
            } while (!receivedResponse && (tries < MAXTRIES));

            if (receivedResponse) {
                System.out.println("Server said: " + new String(receivePacket.getData()));
            } else {
                System.out.println("No response -- giving up");
            }
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



