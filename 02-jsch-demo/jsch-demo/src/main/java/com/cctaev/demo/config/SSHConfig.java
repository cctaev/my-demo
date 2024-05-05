package com.cctaev.demo.config;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cctaev
 * @since 2024-05-05
 */
@RequiredArgsConstructor
public class SSHConfig {
    @Getter
    private final String host;
    @Getter
    private final int port;
    @Getter
    private final String username;
    @Getter
    private final String password;

    private UserInfo userInfo;

    public synchronized UserInfo getUserInfo() {
        if (this.userInfo == null) {
            this.userInfo = new DefaultUserInfo(this.password);
        }
        return this.userInfo;
    }

    @Slf4j
    @AllArgsConstructor
    static class DefaultUserInfo implements UserInfo, UIKeyboardInteractive {
        private final String password;

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction,
                                                  String[] prompt, boolean[] echo) {
            return new String[0];
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public boolean promptPassword(String message) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return true;
        }

        @Override
        public void showMessage(String message) {
        }
    }
}
