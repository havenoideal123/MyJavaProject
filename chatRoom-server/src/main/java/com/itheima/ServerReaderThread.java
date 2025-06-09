package com.itheima;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

public class ServerReaderThread extends Thread{
    private Socket socket;
    public ServerReaderThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        //接收消息有：1.登录2.群聊
        //所以客户端必须声明协议发送消息，比如1代表登录2代表群聊

        //先从socket接收客户端发来的消息类型
        try {
            DataInputStream  dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int type = dis.readInt();
                switch (type){
                    case 1:
                        //客户端发来登录消息，接下来接收昵称数据，更新全部在线用户表
                        String nickName = dis.readUTF();
                        //存入集合
                        Server.onLineSockets.put(socket,nickName);
                        //更新全部在线用户表
                        updateSocketUserList();
                        break;
                    case 2:
                        //客户端发来群聊消息，接下来接收消息内容，发送给所有在线用户
                        String msg = dis.readUTF();
                        sendMsgToAll(msg);
                        break;
                    case 3:
                        //客户端发来私聊消息，接下来接收消息内容，发送给指定用户
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("客户端已退出..." + socket.getInetAddress().getHostAddress());
            // 剔除下线的socket
            Server.onLineSockets.remove(socket);
            //下限后更新在线用户列表
            updateSocketUserList();
        }

    }

    private void sendMsgToAll(String msg) {
        // 一定要拼好这个消息，再发送给所有人socket
        //拼接消息
        StringBuilder sb = new StringBuilder();
        String name = Server.onLineSockets.get(socket);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter  dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowString = dtf.format(now);
        String msgResult = sb.append(name).append(" ").append(nowString).append("\r\n")
                .append(msg).append("\r\n").toString();

        //发送消息给所有人
        for (Socket socket : Server.onLineSockets.keySet()){
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(2); // 2代表是群发消息
                dos.writeUTF(msgResult);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateSocketUserList() {
        //更新全部在线人数列表
        //拿到全部在线客户端的用户名称，并把名称转发给全部的socket
       // 1.拿到全部在线客户端的用户名称
        Collection<String> onLineUsers = Server.onLineSockets.values();
        //2.把这个集合中所有用户都推送给所有的socket
        for (Socket socket : Server.onLineSockets.keySet()) {
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                //1.消息类型
                dos.writeInt(1); //1.代表发送的是在线人数消息
                dos.writeInt(onLineUsers.size());
                for (String onLineUser : onLineUsers) {
                    dos.writeUTF(onLineUser);
                }
                //刷新缓冲区
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
