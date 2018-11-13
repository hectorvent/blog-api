package com.itla.blogapi.post;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author hectorvent@gmail.com
 */
@DataObject(generateConverter = true)
public class Post {

    private int id;
    private int userId;
    private String title;
    private String body;

    public Post() {
    }

    public Post(JsonObject json) {
        PostConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PostConverter.toJson(this, json);
        return json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
