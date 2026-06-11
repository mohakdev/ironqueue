package com.ironqueue.util;

public class Logger {
    public static boolean printLogs = true;
    public static void Log(Class<?> caller,String msg) {
        if(!printLogs) {return;}
        if(caller == null) {
            System.out.println("<Main> " + msg);
        }
        else {
            System.out.println("<" + caller.getSimpleName() + "> " + msg);
        }
    }
}