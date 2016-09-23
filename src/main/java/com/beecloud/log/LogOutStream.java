/**
 * 
 */
package com.beecloud.log;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import com.beecloud.ui.BeeCloudWindow;


/**
 * @description //日志重定向
 * @author hong.lin@beecloud.com
 * @date 2016年9月19日 下午1:37:29
 * @version v1.0
 */
public class LogOutStream extends PrintStream{
	/**
	 * @param out
	 */
	public LogOutStream(OutputStream out) {
		super(out);
	}

	@Override
	public void write(byte[] buf, int off, int len) {
		 final String message = new String(buf, off, len);   
         SwingUtilities.invokeLater(new Runnable(){
        	 public void run(){
        		 BeeCloudWindow.JTextArea_logtout_1.append(message);
        	 }
         });
	}
}
