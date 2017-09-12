package com.zp.easilyshot.easilyshot.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class FileToolUtils {
    private static String TAG = "FileToolUtils";
    private static String shortFolder = "房产调查照片";

    // 删除文件夹
    // param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                MyLogUtil.showLogI(TAG, "delete file " + temp.getAbsolutePath());
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                // delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断文件夹是否存在，不存在就创建出来
     *
     * @param strFolder
     */
    public static void isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param strFolder
     */
    public static boolean isFileExists(String strFolder) {
        File file = new File(strFolder);
        return file.exists();
    }

    public static void installApk(Context context, String apkFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /** */
    /**
     * 文件重命名
     * <p>
     * 文件目录
     *
     * @param oldPath 原来的文件名
     * @param newPath 新文件名
     */
    public static void renameFile(String oldPath, String newPath) {
        if (!oldPath.equals(newPath)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!oldfile.exists()) {
                return;// 重命名文件不存在
            }
            if (newfile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                MyLogUtil.showLogE("FileToolUtils", "文件已存在");
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            MyLogUtil.showLogE("FileToolUtils", "新文件名和旧文件名相同...");
        }
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File oldfile = new File(path);
        if (oldfile.exists()) {
            oldfile.delete();
        }
    }

    /**
     * 查询指定文件夹中的文件
     */
    public static ArrayList<String> getFileName(String fileAbsolutePath) {
        ArrayList<String> allFile = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile == null)
            return allFile;
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".ttf")) {
                    allFile.add(filename.substring(filename.lastIndexOf("/") + 1, filename.lastIndexOf(".")));
                }
            }
        }
        return allFile;
    }

    /**
     * 查询指定文件夹中的文件
     */
    public static ArrayList<String> getFilePath(String fileAbsolutePath) {
        ArrayList<String> allFile = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile == null)
            return allFile;
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filePath = subFile[iFileLength].getAbsolutePath();
                allFile.add(filePath);
            }
        }
        return allFile;
    }

    /**
     * 查询指定文件夹中的文件夹路径
     */
    public static ArrayList<String> getFolderPath(String fileAbsolutePath) {
        ArrayList<String> allFile = new ArrayList<String>();
        if (TextUtils.isEmpty(fileAbsolutePath))
            return allFile;
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile == null)
            return allFile;
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (subFile[iFileLength].isDirectory()) {
                String filePath = subFile[iFileLength].getName();
                allFile.add(filePath);
            }
        }
        return allFile;
    }


    public static String getSdDirPath() {
        File sdDir = null;
        boolean sdCardExist = sdExist();// 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.getAbsolutePath();
        }
        return null;
    }

    public static boolean sdExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取图片路径
     *
     * @param fileName
     * @return
     */
    public static String getShortPath(String fileName) {
        if (fileName != null) {
            String dir = getSdDirPath() + File.separatorChar + shortFolder;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return (dir + File.separatorChar + fileName).trim();
        }
        return null;
    }

    /**
     * 获取图片文件夹路径
     *
     * @return
     */
    public static String getShortFolderPath(String shortFolderName) {
        if (shortFolderName != null) {
            String dir = getSdDirPath() + File.separatorChar + shortFolder + File.separatorChar + shortFolderName;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return (dir).trim();
        }
        return null;
    }

    /**
     * 获取图片文件夹对象
     *
     * @return
     */
    public static File getShortFolder(String shortFolderName) {
        if (shortFolderName != null) {
            String dir = getSdDirPath() + File.separatorChar + shortFolder + File.separatorChar + shortFolderName;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        return null;
    }

    /**
     * 获取拍摄文件夹根目录
     *
     * @return
     */
    public static String getShortFolder() {
        String dir = getSdDirPath() + File.separatorChar + shortFolder;
        try {
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            return null;
        }
        return dir;
    }

}
