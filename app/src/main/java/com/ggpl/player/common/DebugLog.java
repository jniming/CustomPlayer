package com.ggpl.player.common;

import android.util.Log;




/**
 * 日志工具类 在发布时不显示日志
 * @author RANDY_ZHANG
 */
public class DebugLog {
	public static final boolean DEBUG = Boolean.parseBoolean("false");
	public static final String APPLICATION_ID = "com.ggpl.player";
	public static final String BUILD_TYPE = "debug";
	public static final String FLAVOR = "";
	public static final int VERSION_CODE = 1;
	public static final String VERSION_NAME = "1.0";
	static String className="player";
	static String methodName;
	static int lineNumber;
	
    private DebugLog(){
        /* Protect from instantiations */
    }

	public static boolean isDebuggable() {
		return DEBUG;
	}

	private static String createLog(String log ) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);

		return buffer.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements){
//		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void e(String message){
		if (!isDebuggable())
			return;

		// Throwable instance must be created before any methods  
		getMethodNames(new Throwable().getStackTrace());
		Log.e(className, createLog(message));
	}

	public static void i(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.i(className, createLog(message));
	}
	
	public static void d(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.d(className, createLog(message));
	}
	
	public static void v(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.v(className, createLog(message));
	}
	
	public static void w(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.w(className, createLog(message));
	}
	
	public static void wtf(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.wtf(className, createLog(message));
	}

}
