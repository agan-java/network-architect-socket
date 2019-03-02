package com.agan.socket.io;

import java.io.*;

public class IOdemo {
    static void main(String args[]) {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

            /* 读入TXT文件 */
            String pathname = "E:\\file\\漫谈spring cloud微服务架构.txt";
            // 节点流：建立一个字节输入流，读取以上路径的文件
            FileInputStream fileInputStreamnew = new FileInputStream(new File(pathname));
            // 处理流：包装成一个字符输入流对象，用于缓冲读取
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStreamnew));
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                System.out.println(line);
            }


            /* 写入Txt文件 */
            File writename = new File("E:\\file\\temp.txt");
            // 创建新文件
            writename.createNewFile();
            // 节点流：建立一个字符输出流
            FileWriter fileWriter = new FileWriter(writename);
            // 处理流：包装成字符输出流，然后缓冲写文件
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
