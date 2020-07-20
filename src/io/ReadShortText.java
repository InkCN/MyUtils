package io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadShortText {
    public static void main(String[] args) {
        String path = "C:\\Users\\Administrator\\Desktop\\新建文本文档.txt";
        Path filePath = new File(path).toPath();
        try {
            List<String> list = Files.readAllLines(filePath, Charset.forName("gbk"));
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.print(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
