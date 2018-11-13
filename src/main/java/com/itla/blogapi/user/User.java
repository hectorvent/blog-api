package com.itla.blogapi.user;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author hectorvent@gmail.com
 */
@DataObject(generateConverter = true)
public class User {

    private int id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(JsonObject json) {
        UserConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        UserConverter.toJson(this, json);
        return json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

}
