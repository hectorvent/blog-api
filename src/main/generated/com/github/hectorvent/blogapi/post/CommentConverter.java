package com.github.hectorvent.blogapi.post;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.github.hectorvent.blogapi.post.Comment}.
 * NOTE: This class has been automatically generated from the {@link com.github.hectorvent.blogapi.post.Comment} original class using Vert.x codegen.
 */
public class CommentConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Comment obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "body":
          if (member.getValue() instanceof String) {
            obj.setBody((String)member.getValue());
          }
          break;
        case "createdAt":
          if (member.getValue() instanceof Number) {
            obj.setCreatedAt(((Number)member.getValue()).longValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof Number) {
            obj.setId(((Number)member.getValue()).intValue());
          }
          break;
        case "postId":
          if (member.getValue() instanceof Number) {
            obj.setPostId(((Number)member.getValue()).intValue());
          }
          break;
        case "userEmail":
          if (member.getValue() instanceof String) {
            obj.setUserEmail((String)member.getValue());
          }
          break;
        case "userId":
          if (member.getValue() instanceof Number) {
            obj.setUserId(((Number)member.getValue()).intValue());
          }
          break;
        case "userName":
          if (member.getValue() instanceof String) {
            obj.setUserName((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Comment obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Comment obj, java.util.Map<String, Object> json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody());
    }
    json.put("createdAt", obj.getCreatedAt());
    json.put("id", obj.getId());
    json.put("postId", obj.getPostId());
    if (obj.getUserEmail() != null) {
      json.put("userEmail", obj.getUserEmail());
    }
    json.put("userId", obj.getUserId());
    if (obj.getUserName() != null) {
      json.put("userName", obj.getUserName());
    }
  }
}
