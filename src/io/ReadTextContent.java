package io;

import java.io.*;

/**
 * 自动判断文件编码，读取文本内容为一个字符串
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ReadTextContent {
    public static void main(String[] args) throws IOException {
        //请修改下面的字符串内容：改为要读取的文件路径
        String path = "请在这里填入要读取文件的路径";

        String text = ReadText(path);
        System.out.print(text);


//        测试方法isUtf8
//        File file = new File(path);
//        Boolean utf8 = isUtf8(file);
//        System.out.println(utf8);
    }

    public static String ReadText(String path) throws IOException {
        File file = new File(path);

        //判断文件是否存在，不存在抛出异常，结束方法
        if (!file.exists())
            throw new RuntimeException("The file is not exists!!");

        //判断文件编码格式
        String code = getTxtCode(path);

        //如果是"UTF-8 BOM"，要在后面进行读取第一个，故先打上一个标记 isUtfBOM
        boolean isUtfBOM = false;
        if (code.equals("UTF-8 BOM")) {
            code = "UTF-8";
            isUtfBOM = true;
        }


        //创建对应的流
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, code);
        BufferedReader br = new BufferedReader(isr);

        //当是UTF—8含有BOM时，BOM头会造成一些问题，将它读取掉。
        if (isUtfBOM)
            br.read();


        String str;
        StringBuilder builder = new StringBuilder();

        while ((str = br.readLine()) != null)
            builder.append(str).append("\n");
        builder.deleteCharAt(builder.length() - 1);

        br.close();

        return builder.toString();
    }

    //得到文件编码格式
    public static String getTxtCode(String path) throws IOException {
        File file = new File(path);
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[3];
        //读取流中前三个字节并放入到byte[]中
        stream.read(bytes);

        /*
          ANSI： 无格式定义
          Unicode：前两个字节为0xFFFE Unicode文档以0xFFFE开头
          Unicode big endian： 前两字节为 0xFEFF
          UTF-8(带BOM):  以0xEFBBBF开头
          比如：0xFFFE：第一个字节：
          0xFF:换为二进制：11111111（八个一），转成补码：10000001(第一位是符号，故为-1)
          第二个字节：
          0xFE:换为二进制：11111110（八个一），转成补码：10000010(第一位是符号，故为-2)
         */
        if (bytes[0] == -1 && bytes[1] == -2)//0xFFFE
            return "UTF-16";
        else if (bytes[0] == -2 && bytes[1] == -1)//0xFEFF
            return "Unicode";
        else if (bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65)//
            return "UTF-8 BOM";
        else if (isUtf8(file))
            return "UTF-8";
        else
            return "GBK";
    }


    /**
     * 从文件中直接读取字节
     *
     * @param file 文件
     * @return byte[]
     */
    public static byte[] readByteArrayData(File file) {
        byte[] rebyte = null;
        BufferedInputStream bis;
        ByteArrayOutputStream output;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            output = new ByteArrayOutputStream();
            byte[] byt = new byte[1024 * 4];
            int len;
            try {
                while ((len = bis.read(byt)) != -1) {
                    if (len < 1024 * 4) {
                        output.write(byt, 0, len);
                    } else
                        output.write(byt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            rebyte = output.toByteArray();
            bis.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rebyte;
    }


    public static Boolean isUtf8(File file) {
        boolean isUtf8 = true;
        byte[] buffer = readByteArrayData(file);
        int end = buffer.length;
        for (int i = 0; i < end; i++) {
            byte temp = buffer[i];
            if ((temp & 0x80) == 0) {// 0xxxxxxx
                continue;
            } else if ((temp & 0xC0) == 0xC0 && (temp & 0x20) == 0) {// 110xxxxx 10xxxxxx
                if (i + 1 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0) {
                    i = i + 1;
                    continue;
                }
            } else if ((temp & 0xE0) == 0xE0 && (temp & 0x10) == 0) {// 1110xxxx 10xxxxxx 10xxxxxx
                if (i + 2 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0) {
                    i = i + 2;
                    continue;
                }
            } else if ((temp & 0xF0) == 0xF0 && (temp & 0x08) == 0) {// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                if (i + 3 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0
                        && (buffer[i + 3] & 0x80) == 0x80 && (buffer[i + 3] & 0x40) == 0) {
                    i = i + 3;
                    continue;
                }
            }
            isUtf8 = false;
            break;
        }
        return isUtf8;
    }

}
