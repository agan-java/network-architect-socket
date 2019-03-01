package com.agan.socket.chat;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 7517634945840465934L;
    public JTextArea chatArea;
    private JButton sendNewsButton;
    private JButton sendFileButton;
    public JTextArea friendsArea;
    private JTextField newsField;
    private Client client = null;

    public static void main(String[] args) {
        ChatUI chatui = new ChatUI();
    }

    public ChatUI() {
        init();
    }

    // 初始化聊天窗口
    public void init() {
        this.setTitle("聊天窗口");
        this.setBounds(270, 80, 800, 600);
        this.setLayout(null);
        this.setVisible(true);

        Font font = new Font("宋体", Font.BOLD, 22);

        // 聊天对话界面
        chatArea = new JTextArea();
        JScrollPane chatPane = new JScrollPane(chatArea);
        chatPane.setBounds(30, 20, 450, 400);
        chatArea.setBounds(30, 20, 430, 380);
        chatArea.setFont(font);
        this.add(chatPane);

        // 填写发送消息的输入框
        newsField = new JTextField();
        newsField.setBounds(30, 460, 380, 40);
        newsField.setFont(font);
        this.add(newsField);

        // 发送按钮
        sendNewsButton = new JButton("发送");
        sendNewsButton.setBounds(450, 460, 60, 40);
        sendNewsButton.addActionListener(this);
        this.add(sendNewsButton);

        // 发送文件按钮
        sendFileButton = new JButton("发送文件");
        sendFileButton.setBounds(540, 460, 100, 40);
        sendFileButton.addActionListener(this);
        this.add(sendFileButton);

        // 所有上线客户端列表
        friendsArea = new JTextArea();
        friendsArea.setFont(new Font("宋体", Font.BOLD, 18));
        JScrollPane friendsPane = new JScrollPane(friendsArea);
        friendsPane.setBounds(550, 20, 200, 400);
        this.add(friendsPane);

        // 添加窗口事件处理程序，使用适配器，监听JFame退出事件
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("关闭窗口");
                System.exit(-1);
            }
        });


        // 实例化一个负责通信的 Client
        client = new Client(this);
    }

    // 对两个按钮事件进行处理
    @Override
    public void actionPerformed(ActionEvent e) {
        // 发送按钮
        if (e.getSource() == sendNewsButton) {
            String msg = newsField.getText().trim();
            if (msg.length() != 0) {
                boolean status = Utils.write(msg, client.getSocket(), Utils.STRINGTYPE);
                if (!status) {
                    msg = "连接出错";
                    // 关闭socket
                    client.close();
                }
                // 设置聊天窗口的数据
                String old;
                if ("\r\n".equals(chatArea.getText())) {
                    old = "";
                } else {
                    old = chatArea.getText();
                }
                chatArea.setText(old + "我：" + msg + "\r\n");
                newsField.setText("");

            }
        } else if (e.getSource() == sendFileButton) { // 发送文件按钮
            // 对于发送文件的处理，需要重构发送的数据格式，定义文件头
            try {
                FileDialog dialog = new FileDialog(this, "选择文件", FileDialog.LOAD);
                dialog.setVisible(true);
                String path = dialog.getDirectory() + dialog.getFile();
                System.out.println(path);
                Utils.write(path, new FileInputStream(path), client.getSocket());
                String old;
                if ("\r\n".equals(chatArea.getText())) {
                    old = "";
                } else {
                    old = chatArea.getText();
                }
                chatArea.setText(old + "我发送文件：" + path + "\r\n");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }
}
