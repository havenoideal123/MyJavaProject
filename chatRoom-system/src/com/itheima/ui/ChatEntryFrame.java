package com.itheima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatEntryFrame extends JFrame {
    private JTextField nicknameField;
    private JButton enterButton;
    private JButton cancelButton;
    private  Socket socket;//定一个全局管道用于传递当前用户的通信管道

    public ChatEntryFrame() {
        setTitle("Chat Entry");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("Enter your nickname:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(60, 60, 60));
        panel.add(label, BorderLayout.NORTH);

        nicknameField = new JTextField();
        nicknameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nicknameField.setPreferredSize(new Dimension(250, 30)); // Reduced size
        nicknameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(nicknameField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 16));
        enterButton.setBackground(new Color(76, 175, 80)); // Green color
        enterButton.setForeground(Color.BLUE);
        enterButton.setFocusPainted(false);
        enterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        enterButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户名称
                String nickname = nicknameField.getText().trim();
                if (!nickname.isEmpty()) {
                    //进入聊天，立即发送登录消息给服务端
                    //1.请求登录
                    try {
                        login(nickname);
                        new  ChatInterFrame(nickname,  socket);
                        //进入后关闭窗口
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(ChatEntryFrame.this, "Welcome, " + nickname + "!");
                    // Here you can add code to proceed to the chat interface
                } else {
                    JOptionPane.showMessageDialog(ChatEntryFrame.this, "Please enter a valid nickname.");
                }
            }
        });
        buttonPanel.add(enterButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setBackground(new Color(244, 67, 54)); // Red color
        cancelButton.setForeground(Color.BLUE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
        this.setVisible(true);
    }

    private void login(String nickName) throws Exception {
        //建立连接
        socket = new Socket(Constant.SERVER_IP,  Constant.PORT);
        //发送消息类型和昵称给服务器
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(1); //1表示登录
        dos.writeUTF(nickName);
        dos.flush();



    }

}



