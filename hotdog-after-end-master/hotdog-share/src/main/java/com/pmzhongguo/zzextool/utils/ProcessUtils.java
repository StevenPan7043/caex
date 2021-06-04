/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/20 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 20:17
 * @description：程序进程工具类
 * @version: $
 */
public class ProcessUtils
{

    /**
     * 获取当前进程的PID
     *
     * @return
     */
    public static String getCurrentPID()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

    /**
     * 获取Linux进程的PID
     *
     * @param command
     * @return
     */
    public static String getPID(String command)
    {
        BufferedReader reader = null;
        try
        {
            //显示所有进程
            Process process = Runtime.getRuntime().exec("ps -ef");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if (line.contains(command))
                {
                    System.out.println("相关信息 -----> " + command);
                    String[] strs = line.split("\\s+");
                    return strs[1];
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {

                }
            }
        }
        return null;
    }

    /**
     * 关闭Linux进程
     *
     * @param Pid 进程的PID
     */
    public static void closeProcess(String Pid)
    {
        Process process = null;
        BufferedReader reader = null;
        try
        {
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 " + Pid);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                System.out.println("kill PID return info -----> " + line);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (process != null)
            {
                process.destroy();
            }

            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {

                }
            }
        }
    }
}
