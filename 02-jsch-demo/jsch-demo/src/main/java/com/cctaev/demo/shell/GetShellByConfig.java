package com.cctaev.demo.shell;

import com.jcraft.jsch.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过配置获取shell
 *
 * @author cctaev
 * @since 2024/5/4
 */
public class GetShellByConfig {


    public static void main(String[] args) {
        SSHConfig config = createSSHConfig();
        ConnectStrategy strategy = new ConnectStrategy(config);
        JSch sshClient = new JSch();
        try {
            // 创建会话
            Session session = sshClient.getSession(config.getUsername(), config.getHost(), config.getPort());

            session.setUserInfo(strategy);
            session.connect(3000);
            // 创建通道
            Channel channel = session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect(3000);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    private static SSHConfig createSSHConfig() {
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setUsername("root");
        sshConfig.setHost("192.168.80.51");
        sshConfig.setPort(22);
        sshConfig.setPassword("");
        return sshConfig;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class SSHConfig {
        private String host;
        private int port;
        private String username;
        private String password;
    }

    @Slf4j
    @AllArgsConstructor
    static class ConnectStrategy implements UserInfo, UIKeyboardInteractive {

        private final SSHConfig sshConfig;

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            log.info("promptKeyboardInteractive {}", destination);
            return new String[0];
        }

        @Override
        public String getPassphrase() {
            log.info("getPassphrase");
            return null;
        }

        @Override
        public String getPassword() {
            log.info("getPassword");
            return sshConfig.getPassword();
        }

        @Override
        public boolean promptPassword(String message) {
            log.info("promptPassword {}", message);
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            log.info("promptPassphrase {}", message);
            return false;
        }

        /**
         * 这个方法会提示用户：是否接受服务器的公钥，默认false，否的话就是不接受
         * @param message
         * @return
         */
        @Override
        public boolean promptYesNo(String message) {
            log.info("promptYesNo {}", message);
            return true;
        }

        @Override
        public void showMessage(String message) {
            log.info("showMessage {}", message);
        }
    }
}
