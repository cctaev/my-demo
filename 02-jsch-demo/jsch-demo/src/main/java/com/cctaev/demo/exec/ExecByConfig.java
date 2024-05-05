package com.cctaev.demo.exec;

import com.cctaev.demo.config.SSHConfig;
import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 以配置的形式远程执行SSH命令
 *
 * @author cctaev
 * @since 2024-05-05
 */
public class ExecByConfig {
    public static void main(String[] args) {
        // 创建SSH配置
        SSHConfig config = new SSHConfig("47.92.161.168", 50100, "root", "Lrf411524.");
        // 创建SSH客户端
        JSch sshClient = new JSch();
        try {
            Session session = sshClient.getSession(config.getUsername(), config.getHost(), config.getPort());
            session.setUserInfo(config.getUserInfo());
            // 超时时间3s
            session.connect(3000);
            // 创建通道
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls /tmp");
            channel.setInputStream(null); // 从Java程序的视角，setInputStream用来设置向SSH服务端输入数据的流
//            channel.setOutputStream(null); // 从Java程序的视角，setOutputStream用来设置接收SSH服务端输出数据的流
            channel.setErrStream(System.err); // 从Java程序的视角，setErrStream用来设置接收SSH服务端错误输出数据的流
            InputStream is = channel.getInputStream(); // 从Java程序的视角，getInputStream用来获取SSH服务端输出数据的流
//            OutputStream os = channel.getOutputStream(); // 从Java程序的视角，getOutputStream用来获取向SSH服务端输入数据的流
//            InputStream es = channel.getErrStream();// 从Java程序的视角，getErrStream用来获取SSH服务端错误输出数据的流

            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (is.available() > 0) {
                    int i = is.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (is.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
