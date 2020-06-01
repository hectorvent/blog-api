package com.itla.blogapi.user;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Hector Ventura <hectorvent@gmail.com>
 */
@DataObject(generateConverter = true)
public class Token {

    private int userId;
    private String token;
    private long createdAt;
    private String description;

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

    public Token setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Token setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Token setDescription(String description) {
        this.description = description;
        return this;
    }

}
