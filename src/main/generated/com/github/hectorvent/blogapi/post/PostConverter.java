package com.github.hectorvent.blogapi.post;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link com.github.hectorvent.blogapi.post.Post}.
 * NOTE: This class has been automatically generated from the {@link com.github.hectorvent.blogapi.post.Post} original class using Vert.x codegen.
 */
public class PostConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Post obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "body":
          if (member.getValue() instanceof String) {
            obj.setBody((String)member.getValue());
          }
          break;
        case "comments":
          if (member.getValue() instanceof Number) {
            obj.setComments(((Number)member.getValue()).intValue());
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
        case "liked":
          if (member.getValue() instanceof Boolean) {
            obj.setLiked((Boolean)member.getValue());
          }
          break;
        case "likes":
          if (member.getValue() instanceof Number) {
            obj.setLikes(((Number)member.getValue()).intValue());
          }
          break;
        case "tags":
          if (member.getValue() instanceof JsonArray) {
            java.util.LinkedHashSet<java.lang.String> list =  new java.util.LinkedHashSet<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setTags(list);
          }
          break;
        case "title":
          if (member.getValue() instanceof String) {
            obj.setTitle((String)member.getValue());
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
        case "views":
          if (member.getValue() instanceof Number) {
            obj.setViews(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(Post obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Post obj, java.util.Map<String, Object> json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody());
    }
    json.put("comments", obj.getComments());
    json.put("createdAt", obj.getCreatedAt());
    json.put("id", obj.getId());
    json.put("liked", obj.isLiked());
    json.put("likes", obj.getLikes());
    if (obj.getTags() != null) {
      JsonArray array = new JsonArray();
      obj.getTags().forEach(item -> array.add(item));
      json.put("tags", array);
    }
    if (obj.getTitle() != null) {
      json.put("title", obj.getTitle());
    }
    if (obj.getUserEmail() != null) {
      json.put("userEmail", obj.getUserEmail());
    }
    json.put("userId", obj.getUserId());
    if (obj.getUserName() != null) {
      json.put("userName", obj.getUserName());
    }
    json.put("views", obj.getViews());
  }
}
