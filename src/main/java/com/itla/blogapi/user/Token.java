package com.itla.blogapi.user;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author hectorvent@gmail.com
 */
@DataObject(generateConverter = true)
public class Token {

    private int userId;
    private String token;

    public Token() {
    }

    public Token(JsonObject json) {
        TokenConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TokenConverter.toJson(this, json);
        return json;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
