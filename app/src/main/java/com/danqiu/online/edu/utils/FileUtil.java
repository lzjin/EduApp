package com.danqiu.online.edu.utils;


import com.danqiu.online.edu.config.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by lzj on 2019/7/1
 * Describe ：注释
 */
public class FileUtil {
    private static String TAG = "test";

    /**
     * 创建主目录
     */
    public static void createAppDirectory() {
        File file = new File(Constants.APP_ROOT_PATH);
        if (!file.exists()) {
            file.mkdirs();
            L.i(TAG, "----------主目录,创建" + file.getAbsolutePath());
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File createFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
            L.i(TAG, "--------------------下载目录,创建" + file.getAbsolutePath());
        }
        file = new File(filePath + "/" + fileName);
        return file;
    }

    /**
     * 写入文件
     * 断点续传
     *
     * @param is
     * @param file
     */
    public static void writeFile(InputStream is, File file) throws IOException {
        RandomAccessFile savedFile = null;
        long ltest = 0;
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (file != null && file.exists()) {
            ltest = file.length();
        }
        if (is != null) {
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(ltest);
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            L.e("test", "----------1------isLen="+len);
            while ((len = is.read(buffer)) != -1) {
                savedFile.write(buffer, 0, len);
                L.e("test", "--------2--------isLen="+len);
            }
            L.e("test", "-----------3----isLen="+len);
            is.close();
        } else {
            L.e("test", "----------------isxxx");
            try {
                throw new Exception("下载失败", new Throwable("检查网网咯"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制
     * 读取路径  写入路径
     *
     * @param isPath
     * @param osPath
     */
    public static void copyFile(String isPath, String osPath) {
        //读取a
        // File f=new File("D:/Test/a.txt");
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(isPath);
            os = new FileOutputStream(osPath);

            byte[] by = new byte[1024];
            int length = 0;
            while ((length = is.read(by)) != -1) {
                os.write(by, 0, length);
            }
            L.i(TAG, "--------------------复制完成");
            is.close();
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ioWriteFile(InputStream is,File file) {

        FileOutputStream os = null;
        byte[] buf = new byte[2048];
        int len = 0;
        try {
            os = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (Exception e) {
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 是否存在文件
     *
     * @param path
     * @return
     */
    public static boolean isExistsFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除当个文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                L.i(TAG, "删除单个文件" + path + "成功！");
                return true;
            } else {
                L.i(TAG, "删除单个文件" + path + "失败！");
                return false;
            }
        } else {
            L.i(TAG, "删除单个文件失败：" + path + "不存在！");
            return true;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            L.i(TAG, "删除目录失败：" + dir + "不存在！");
            return false;
        }
        //boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
                break;
            }// 删除子目录
            else if (files[i].isDirectory()) {
                deleteDirectory(files[i]
                        .getAbsolutePath());
            }
        }
        // 删除当前目录
        if (dirFile.delete()) {
            L.i(TAG, "删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
