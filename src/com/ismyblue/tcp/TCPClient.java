package com.atguigu.ismyblue;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {
    public static void main(String[] args) {
        Thread tcpClient;
        if (args.length < 2) {
            tcpClient = new TCPClient("localhost", 1111);
        } else {
            tcpClient = new TCPClient(args[0], Integer.parseInt(args[1]));
        }
        tcpClient.start();
    }

    private Socket socket;

    public TCPClient(String hostAddress, int port){
        try {
            this.socket = new Socket(hostAddress, port);
        } catch (IOException e) {
            System.out.println("创建客户端套接字错误！");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            //字节输入输出流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps = new PrintStream(socket.getOutputStream());
            // 客户端服务端消息
            String clientMessage = null;


            // 从服务器读
            new Thread(){
                private String serverMessage = null;
                @Override
                public void run() {
                    try {
                        //读取服务器消息
                        while ((serverMessage = br.readLine()) != null) {
                            System.out.println("<Server>: " + serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }.start();

            // 给服务器发消息
            Scanner scanner = new Scanner(System.in);
            while ((clientMessage = scanner.nextLine()) != null) {
                ps.println(clientMessage);
                if (clientMessage.equals("exit")) {
                    br.close();
                    ps.close();
                    socket.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
