package com.itheima;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    //定义一个集合容器存储所有客户端的管道
    //定义一个map集合，键是管道，值是名称
   public static final Map<Socket, String> onLineSockets = new HashMap<>();


    public static void main(String[] args)
    {
        System.out.println("服务器启动...");
        //1.注册端口
        try {
            ServerSocket serverSocket = new ServerSocket(Constant.PORT);

            //2.主线程负责接收客户端的连接请求
            while (true) {
                System.out.println("等待客户端的连接...");
                Socket  socket = serverSocket.accept();
                //3.每一个管道创建一个线程，负责读取客户端发送过来的数据
                new ServerReaderThread(socket).start();
                System.out.println("一个客户端连接了...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
