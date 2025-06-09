package com.itheima.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClientReaderThread extends Thread{
    private Socket socket;
    private DataInputStream  dis;
    private ChatInterFrame win;
    public ClientReaderThread(Socket socket,ChatInterFrame win)
    {
        this.win = win;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        //接收消息有：1.登录2.群聊
        //所以客户端必须声明协议发送消息，比如1代表登录2代表群聊

        //先从socket接收服务端发来的消息类型
        try {
            dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int type = dis.readInt();
                switch (type){
                    case 1:
                        //服务端发来在线人数消息，接下来接收昵称数据，更新全部在线用户表

                        //更新全部在线用户表
                        updateClientOnLineUserList();
                        break;
                    case 2:
                        //服务端发来群聊，接下来接收消息内容，发送给所有在线用户
                        getMsg();
                        break;
                    case 3:
                        //客户端发来私聊消息，接下来接收消息内容，发送给指定用户
                        break;
                }
            }

        } catch (Exception e) {
          e.printStackTrace();
        }

    }

    private void getMsg() throws IOException {
        String msg = dis.readUTF();
        //获取消息后返回给聊天界面
        win.setMsgToWin(msg);
    }

    private void updateClientOnLineUserList() throws IOException {
        //1.读取有多少个用户
        int count = dis.readInt();
        //2。循环控制读取多少个用户信息
        List<String> nicklist = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String nickname = dis.readUTF();
            nicklist.add(nickname);
        }
        //3.将集合信息展示到窗口
        win.updateOnlineUsers(nicklist);

    }
}


