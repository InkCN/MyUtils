package io;

import java.io.*;

/**
 * 文件复制
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class FileCopy {
    public static void main(String[] args) {
        //被复制文件或文件夹的路径
        String oldPath = "Please fill in the path of the copied file or folder!!";

        //新文件或新文件夹的存放路径 Please fill in the new file or new folder to store the path!!
        String newPath = "Please fill in the path of the copied file or folder!!";

        boolean b = fileCopy(oldPath, newPath);

        System.out.println("是否复制成功：" + b);
    }


    public static boolean fileCopy(String oldPath, String newPath) {
        File start = new File(oldPath);
        if (!start.exists()) {
            System.out.println("The path is not right.");
            return false;
        }
        FileCopy fileUtil = new FileCopy();

        if (start.isDirectory()) {
            fileUtil.directoryCopy(oldPath, newPath);
        } else {
            fileUtil.singleFileCopy(oldPath, newPath);
        }
        return true;
    }

    private void singleFileCopy(String oldPath, String newPath) {
        FileInputStream fis;
        FileOutputStream fos;

        try {
            fis = new FileInputStream(oldPath);
            fos = new FileOutputStream(newPath);

            //创建缓冲流
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] bytes = new byte[1024];

            while (bis.read(bytes) != -1) {
                bos.write(bytes);
            }
            bos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void directoryCopy(String oldPath, String newPath) {
        File start = new File(oldPath);
        File end = new File(newPath);

        if (!end.exists()) {
            end.mkdirs();
        }

        //注意加上路径分隔符，不要遗漏
        String[] list = start.list();
        for (String name : list) {
            if (new File(oldPath + File.separator + name).isDirectory()) {
                directoryCopy(oldPath + File.separator + name, newPath + File.separator + name);
            } else {
                singleFileCopy(oldPath + File.separator + name, newPath + File.separator + name);
            }
        }
    }
}
