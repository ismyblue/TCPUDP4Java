package com.ismyblue.tcp;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClientConnect extends Thread{
    private Socket tcpClient;
    private InputStream is;
    private OutputStream os;
    private PrintStream ps;
    private BufferedReader br;

    TCPClientConnect(Socket tcpClient) throws IOException {
        InetSocketAddress s = (InetSocketAddress) tcpClient.getRemoteSocketAddress();
        setName(s.getAddress().getHostAddress() + ":" + s.getPort());

        this.tcpClient = tcpClient;
        is = tcpClient.getInputStream();
        os = tcpClient.getOutputStream();
        ps = new PrintStream(os);
        br = new BufferedReader(new InputStreamReader(is));
        //第一次连接发送helloworld
        System.out.println("<Server>: " + "Hello World! " + getName());
        ps.println("Hello World! " + getName());

    }

    // 发送消息到客户端
    public void sendToClient(String serverMessage) {
        ps.println(serverMessage);
    }

    @Override
    public void run() {
        try {
            // 客户端消息
            String clientMessage = null;

            // 开始接受客户端消息
            while (true) {
                // 接受客户端消息
                clientMessage = br.readLine();
                System.out.println("<Client " + Thread.currentThread().getName() + ">: " + clientMessage);
                if (clientMessage.equals("exit")) {// 客户端说拜拜，服务的耶拜拜
                    System.out.println("<Server>: GoodBye!");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
                ps.close();
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }//run()
}
