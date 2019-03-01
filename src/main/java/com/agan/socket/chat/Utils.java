package com.agan.socket.chat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Utils {

    // 三种数据类型，用于区分发送的数据
    // 字符串标识
    public final static byte STRINGTYPE = 0;
    // 所有上线用户信息标识
    public final static byte USERSTYPE = 1;
    // 发送文件标识
    public final static byte FILETYPE = 2;

    //int转成四字节的byte[]，便于写入到socket流中，也便于解码
    public static byte[] int2Byte(int number) {
        byte[] b = new byte[4];
        b[0] = (byte) (number >> 24);
        b[1] = (byte) (number >> 16);
        b[2] = (byte) (number >> 8);
        b[3] = (byte) number;
        return b;
    }

    //byte转int，和上面的int2byte对应
    public static int byte2Int(byte[] b) {
        int i3 = (b[0] & 0xFF) << 24;
        int i2 = (b[1] & 0xFF) << 16;
        int i1 = (b[2] & 0xFF) << 8;
        int i0 = b[3] & 0xFF;
        return i3 | i2 | i1 | i0;
    }

    //重写read方法
    public static Map<Byte, byte[]> read(Socket socket) throws Exception {
        Map<Byte, byte[]> map = read(socket, null);
        return map;
    }

    //获取字典数据(只有一个Entry)中的key，也就是Byte
    public static Byte getMapKey(Map<Byte, byte[]> map) {
        Set<Entry<Byte, byte[]>> entrySet = map.entrySet();
        Iterator<Entry<Byte, byte[]>> it = entrySet.iterator();
        Entry<Byte, byte[]> entry = it.next();
        return entry.getKey();
    }


    /**
     * 获取字典数据(只有一个Entry)中的value，
     * 也就是byte[] 数组
     **/
    public static byte[] getMapValue(Map<Byte, byte[]> map) {
        Set<Entry<Byte, byte[]>> entrySet = map.entrySet();
        Iterator<Entry<Byte, byte[]>> it = entrySet.iterator();
        Entry<Byte, byte[]> entry = it.next();
        return entry.getValue();
    }

    /**
     * read方法对封装的数据进行解编码，取出封装的数据，但对数据类型不予区分，只返回类型和数据的map
     * 具体的区分数据类型在client中处理，对于文件类型需要特殊处理
     **/
    public static Map<Byte, byte[]> read(Socket socket, ChatUI chatui) throws Exception {
        Map<Byte, byte[]> map = new HashMap<Byte, byte[]>();
        InputStream in = socket.getInputStream();
        byte[] type = new byte[1];
        in.read(type);
        byte[] lengthByte = new byte[4];
        in.read(lengthByte);
        int length = Utils.byte2Int(lengthByte);

        byte[] buffer = new byte[length];
        in.read(buffer);

        map.put(type[0], buffer);
        return map;
    }

    //重写write方法，使得可以写入文件，但注意不能写入过大的文件，对数据内容进一步重构
    public static void write(String path, FileInputStream fis, Socket socket) {
        try {
            //把文件名称也写到数组中，首先是四个字节的文件名称长度，加上文件名称，再加内容
            String fileName = path.substring(path.lastIndexOf("\\") + 1, path.length());
            //文件名称
            byte[] nameByte = fileName.getBytes();
            //文件名称长度
            byte[] lenByte = Utils.int2Byte(nameByte.length);
            //文件数据
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            //新建足够长的数组用于存放合并的数据
            byte[] newByte = new byte[4 + nameByte.length + buffer.length];
            //合并数组
            System.arraycopy(lenByte, 0, newByte, 0, lenByte.length);
            System.arraycopy(nameByte, 0, newByte, lenByte.length, nameByte.length);
            System.arraycopy(buffer, 0, newByte, lenByte.length + nameByte.length, buffer.length);

            write(newByte, socket, Utils.FILETYPE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定客户端socket 发送消息
     */
    public static boolean write(String data, Socket socket, byte type) {
        return write(data.getBytes(), socket, type);
    }

    /**
     * 指定客户端socket 发送消息
     * @param data 发送的数据类型
     * @param socket 对应的socket
     * @param type 数据类型，用于区分发送的数据
     */
    public static boolean write(byte[] data, Socket socket, byte type) {
        try {
            OutputStream out = socket.getOutputStream();
            //获得数据类型，数据长度，转成byte
            int length = data.length;
            byte[] typeByte = {(byte) type};
            byte[] lengthByte = Utils.int2Byte(length);
            //新建足够长的数组用于存放合成的数据
            byte[] end = new byte[1 + 4 + data.length];
            //合并数组
            System.arraycopy(typeByte, 0, end, 0, typeByte.length);
            System.arraycopy(lengthByte, 0, end, 1, lengthByte.length);
            System.arraycopy(data, 0, end, 5, data.length);

            out.write(end);
            System.out.println("发送完毕:" + new String(data));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }
}