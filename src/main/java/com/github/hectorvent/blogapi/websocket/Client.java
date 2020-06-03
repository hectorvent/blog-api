package com.github.hectorvent.blogapi.websocket;

import com.github.hectorvent.blogapi.user.User;
import java.util.Optional;

/**
 *
 * @author Hector Ventura <hectorvent@gmail.com>
 */
public class Client {

    private String socketId;
    private User user;
    private Optional<String> tokenCode;

    public Client(String socketId, String tokenCode) {
        this.socketId = socketId;
        this.tokenCode = Optional.ofNullable(tokenCode);
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public Optional<String> getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = Optional.of(tokenCode);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
