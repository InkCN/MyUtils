package io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * download by url
 *
 * 防止屏蔽程序抓取而返回403错误可以使用以下语句：
 * conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
 */
public class DownLoadByURL {

    public static boolean downloadByURL(String fileUrl, String path) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //set connect and read time can't over 5 seconds
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK)
            return false;

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));

        byte[] b = new byte[1024 * 4];
        int len;
        while ((len = bis.read(b)) > 0)
            bos.write(b, 0, len);
        bos.close();
        bis.close();
        return true;
    }
}
