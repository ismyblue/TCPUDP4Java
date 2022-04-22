package com.atguigu.ismyblue;

import java.io.IOException;
import java.net.*;
import java.time.LocalTime;

public class UDPClient extends Thread {

    public static void main(String[] args) {
        String ip = "localhost";
        int port = 1111;
        if (args.length >= 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        UDPClient udpClient = new UDPClient(ip, port);
        udpClient.start();
    }

    private String ip = null;
    private int port;
    private DatagramSocket udpClient;

    public UDPClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            udpClient = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // 接收消息
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            byte[] message = new byte[1024 * 64];
                            DatagramPacket p = new DatagramPacket(message, 0, message.length);
                            udpClient.receive(p);
                            System.out.printf("[%s] Receive: %s\n", LocalTime.now(), new String(message, 0, p.getLength()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

            while (true) {
                byte[] bytes = "Hello Server!".getBytes();
                DatagramPacket p = new DatagramPacket(bytes, 0, bytes.length, new InetSocketAddress(ip, port));
                udpClient.send(p);
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            udpClient.close();
        }

    }
}
