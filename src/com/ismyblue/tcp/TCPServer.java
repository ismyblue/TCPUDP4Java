package com.ismyblue.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class TCPServer extends Thread {

    public static void main(String[] args) {
        Thread tcpServer;
        if (args.length < 1) {
            tcpServer = new TCPServer(1111);
        } else {
            tcpServer = new TCPServer(Integer.parseInt(args[0]));
        }
        tcpServer.start();
    }

    private ServerSocket serverSocket;
    private ArrayList<TCPClientConnect> clientConnects = new ArrayList<>();

    public TCPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public TCPServer(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // 侦听
        try {
            // 群发消息
            new Thread(){
                @Override
                public void run() {
                    // 服务器群发消息
                    Scanner scanner = new Scanner(System.in);
                    String serverMessage;
                    // 给每个客户端连接发送消息
                    while ((serverMessage = scanner.nextLine()) != null) {
                        Iterator<TCPClientConnect> iter = clientConnects.iterator();
                        while (iter.hasNext()) {
                            TCPClientConnect conn = iter.next();
                            if (!conn.isAlive()) {
                                iter.remove();
                                continue;
                            }
                            conn.sendToClient(serverMessage);
                        }
                    }
                }
            }.start();

            // 侦听连接
            while (true) {
                //获取tcp连接，开启一个处理线程
                Socket tcpclient = serverSocket.accept();

                // 创建接受客户端消息的线程
                TCPClientConnect tcpClientConnect = new TCPClientConnect(tcpclient);
                tcpClientConnect.start();
                clientConnects.add(tcpClientConnect);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
