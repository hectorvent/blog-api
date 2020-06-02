package com.itla.blogapi.user;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.itla.blogapi.user.User}.
 * NOTE: This class has been automatically generated from the {@link com.itla.blogapi.user.User} original class using Vert.x codegen.
 */
public class UserConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, User obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "createdAt":
          if (member.getValue() instanceof Number) {
            obj.setCreatedAt(((Number)member.getValue()).longValue());
          }
          break;
        case "email":
          if (member.getValue() instanceof String) {
            obj.setEmail((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).intValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "posts":
          if (member.getValue() instanceof Number) {
            obj.setPosts(((Number)member.getValue()).intValue());
          }
          break;
        case "token":
          if (member.getValue() instanceof JsonObject) {
            obj.setToken(new com.itla.blogapi.user.Token((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(User obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(User obj, java.util.Map<String, Object> json) {
    json.put("createdAt", obj.getCreatedAt());
    if (obj.getEmail() != null) {
      json.put("email", obj.getEmail());
    }
    json.put("id", obj.getId());
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    json.put("posts", obj.getPosts());
    if (obj.getToken() != null) {
      json.put("token", obj.getToken().toJson());
    }
  }
}
