package com.atguigu.ismyblue;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPServerClientConnect extends Thread {
    public static int connectCount = 0;
    private Socket tcpClient;
    private InputStream is;
    private OutputStream os;
    private PrintStream ps;
    private BufferedReader br;

    TCPServerClientConnect(Socket tcpClient) throws IOException {
        InetSocketAddress s = (InetSocketAddress) tcpClient.getRemoteSocketAddress();
        setName(s.getAddress().getHostAddress() + ":" + s.getPort());

        this.tcpClient = tcpClient;
        is = tcpClient.getInputStream();
        os = tcpClient.getOutputStream();
        ps = new PrintStream(os);
        br = new BufferedReader(new InputStreamReader(is));
        //连接数加一
        TCPServerClientConnect.connectCount++;
        //第一次连接发送helloworld
        System.out.println("<Server>: " + "Hello World! " + getName() + " Connect Count:" + TCPServerClientConnect.connectCount);
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
                    System.out.println("<Server>: GoodBye!" + getName());
                    ps.println("GoodBye! " + getName());
                    tcpClient.shutdownOutput();
                    tcpClient.shutdownInput();
                    tcpClient.close();
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            try {
                is.close();
                os.close();
                ps.close();
                br.close();
                tcpClient.close();
                TCPServerClientConnect.connectCount--;
                System.out.println("Connect Count:" + TCPServerClientConnect.connectCount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }//run()
}
