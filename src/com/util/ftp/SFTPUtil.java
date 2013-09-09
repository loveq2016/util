package com.util.ftp;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtil {
	
	private static final Logger log = Logger.getLogger(SFTPUtil.class);
	
    Session session = null;
    Channel channel = null;
    
    private String username,host,password;
    private int port,timeout;

    public SFTPUtil(String host,int port,String username,String password,int timeout) {
    	this.host =  host;
    	this.port = port;
    	this.username = username;
    	this.password = password;
    	this.timeout = timeout;
    }
    
    public SFTPUtil(String host,String username,String password) {
    	this.host =  host;
    	this.port = 22;
    	this.username = username;
    	this.password = password;
    	this.timeout = 2000000000;
    }
    
    

    public ChannelSftp getChannel() throws JSchException {

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(username, host, port); // 根据用户名，主机ip，端口获取一个Session对象
        log.debug("Session created.");
 
        session.setPassword(password); // 设置密码
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        log.debug("Session connected.");

        log.debug("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        log.debug("Connected successfully to ftpHost = " + host + ",as ftpUserName = " + username  + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
