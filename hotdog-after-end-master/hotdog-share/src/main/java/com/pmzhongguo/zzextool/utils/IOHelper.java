/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/29 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/29 16:06
 * @description：IO操作辅助类
 * @version: $
 */
@Slf4j
public class IOHelper {

    private String folder = new ApplicationHome(this.getClass()).getSource().getParentFile().getPath() + File.separator;

    private String basePath = folder + "status.config";

    /**
     * 字节输出流写入文件
     *
     * @throws IOException
     */
    public void outputStream(File file, String content) {
        //1.创建字节输出流对象
        FileOutputStream fos = null;
        try {
            file = this.createFile(file);
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            log.error("流操作异常： " + e.getLocalizedMessage());
        } catch (IOException e) {
            log.error("流操作异常： " + e.getLocalizedMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                fos = null;
                log.error("关闭流异常: FileOutputStream : " + e.getLocalizedMessage());
            }
        }
    }


    /**
     * 字节输入流读取文件
     *
     * @throws IOException
     */
    public String inputStream(File file) {
        //1.创建字节输入流对象
        FileInputStream fis = null;
        ByteArrayOutputStream out = null;
        int read = -1;
        try {
            file = this.createFile(file);
            fis = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            while ((read = fis.read()) != -1) {
                out.write(read);
            }
        } catch (FileNotFoundException e) {
            log.error("流操作异常： " + e.getLocalizedMessage());
        } catch (IOException e) {
            log.error("流操作异常： " + e.getLocalizedMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                out = null;
                fis = null;
                log.error("关闭流异常: " + e.getLocalizedMessage());
            }

        }
        return String.valueOf(out.toByteArray());
    }

    /**
     * 读取文件
     *
     * @param file
     * @return {@link String}
     * @throws IOException
     */
    public String readFile(File file) {
        Reader in = null;
        StringWriter out = null;
        try {
            file = this.createFile(file);
            in = new FileReader(file);
            out = new StringWriter();
            copy(in, out);
        } catch (IOException e) {
            log.error("操作异常: " + e.getLocalizedMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常: " + e.getLocalizedMessage());
            }
        }
        log.warn("读取到配置文件信息: " + file + " : " + out.toString());
        return out.toString();
    }

    /**
     * 根据文件和字符内容生成文件
     *
     * @param file
     * @param content
     * @throws IOException
     */
    public void saveFile(File file, String content) {
        Writer writer = null;
        try {
            file = this.createFile(file);
            writer = new FileWriter(file);
            writer.write(content);
        } catch (IOException e) {
            log.error("流操作异常： " + e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    writer = null;
                    log.error("关闭流异常: " + e.getLocalizedMessage());
                }
            }
        }
        log.warn("写入信息到配置文件: " + file.getName() + " : " + content);
    }

    /**
     * 拷贝文件
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(Reader in, Writer out) throws IOException {
        int c = -1;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
    }

    private File createFile(File file) throws IOException {
        if (file == null) {
            file = new File(basePath);
        }
        File tmpFile = new File(folder);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

}
