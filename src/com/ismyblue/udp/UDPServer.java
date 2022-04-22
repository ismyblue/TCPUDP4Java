package com.atguigu.ismyblue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.time.LocalTime;

public class UDPServer extends Thread {
    public static void main(String[] args) {
        int port = 1111;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        UDPServer udpServer = new UDPServer(port);
        udpServer.start();
    }

    private DatagramSocket udpServer = null;

    UDPServer(int port) {
        try {
            this.udpServer = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] receiveBytes = new byte[1024 * 64];
            byte[] sendBytes;
            DatagramPacket p = new DatagramPacket(receiveBytes, receiveBytes.length);

            while (true) {
                // 接收
                udpServer.receive(p);
                String receiveStr = new String(receiveBytes, 0, p.getLength());
                System.out.printf("[%s] %s:%d : %s\n", LocalTime.now(), p.getAddress().getHostAddress(), p.getPort(), receiveStr);
//                System.out.println(LocalTime.now() + p.getAddress().getHostAddress() + ":" + p.getPort() + receiveStr);
                // 回复
                sendBytes = (" Got it [" + receiveStr + "]").getBytes();
                udpServer.send(new DatagramPacket(sendBytes, 0, sendBytes.length, p.getAddress(), p.getPort()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            udpServer.close();
        }

    }
}
