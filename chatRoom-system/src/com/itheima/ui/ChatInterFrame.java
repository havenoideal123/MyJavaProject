package com.itheima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatInterFrame extends JFrame {
    private DefaultListModel<String> userListModel;
    private JList<String> onlineUsersList;
    private JTextArea chatHistory;
    private JTextField inputField;
    private JButton sendButton;
    private String nickName;
    private Socket socket;
    List<String> onlineUsers;

    public ChatInterFrame() {
        setTitle("Chat Room");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Split pane for history and online users
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600); // Set initial divider location

        // Chat history area
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        chatHistory.setBackground(new Color(245, 245, 245));
        chatHistory.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollPaneHistory = new JScrollPane(chatHistory);
        splitPane.setLeftComponent(scrollPaneHistory);

        // Online users area

        userListModel = new DefaultListModel<>();

        onlineUsersList = new JList<>(userListModel);
        onlineUsersList.setFont(new Font("Arial", Font.PLAIN, 14));
        onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineUsersList.setBackground(new Color(245, 245, 245));
        onlineUsersList.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollPaneUsers = new JScrollPane(onlineUsersList);
        splitPane.setRightComponent(scrollPaneUsers);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(600, 40));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(76, 175, 80)); // Green color
        sendButton.setForeground(Color.BLACK);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText().trim();
                if (!message.isEmpty()) {
                    //清空输入框
                    inputField.setText("");
                    //发送
                    sendMsg(message);

                }
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        this.setVisible(true);
    }

    private void sendMsg(String message)  {
        //从socket管道获取一个特殊数据输出流
        try {
            DataOutputStream  dos = new DataOutputStream(socket.getOutputStream());
            //发送消息类型和消息给服务器
            dos.writeInt(2);
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ChatInterFrame(String nickname, Socket socket) {
        this();//初始化无参构造
        //初始化数据，立即展示到窗口
        this.setTitle(nickname + "的聊天窗口");
        this.nickName = nickname;
        this.socket = socket;

        //立即将客户端的socket管道交个一个独立线程，负责读取客户端从服务端收到的在线人数更新和群聊
        new ClientReaderThread(socket,this).start();
    }

    public void updateOnlineUsers(List<String> nicklist) {
        userListModel.clear();
        for (String user : nicklist) {
            userListModel.addElement(user);
        }
    }

    //更新群聊消息到界面展示
    public void setMsgToWin(String msg) {
        chatHistory.append(msg);
    }
}



