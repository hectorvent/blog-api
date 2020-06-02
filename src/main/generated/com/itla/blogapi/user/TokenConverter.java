package com.itla.blogapi.user;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.itla.blogapi.user.Token}.
 * NOTE: This class has been automatically generated from the {@link com.itla.blogapi.user.Token} original class using Vert.x codegen.
 */
public class TokenConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Token obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "createdAt":
          if (member.getValue() instanceof Number) {
            obj.setCreatedAt(((Number)member.getValue()).longValue());
          }
          break;
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
          }
          break;
        case "token":
          if (member.getValue() instanceof String) {
            obj.setToken((String)member.getValue());
          }
          break;
        case "userId":
          if (member.getValue() instanceof Number) {
            obj.setUserId(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(Token obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Token obj, java.util.Map<String, Object> json) {
    json.put("createdAt", obj.getCreatedAt());
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getToken() != null) {
      json.put("token", obj.getToken());
    }
    json.put("userId", obj.getUserId());
  }
}
