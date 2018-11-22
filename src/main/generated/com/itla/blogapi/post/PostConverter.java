/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.itla.blogapi.post;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link com.itla.blogapi.post.Post}.
 *
 * NOTE: This class has been automatically generated from the {@link com.itla.blogapi.post.Post} original class using Vert.x codegen.
 */
public class PostConverter {

  public static void fromJson(JsonObject json, Post obj) {
    if (json.getValue("body") instanceof String) {
      obj.setBody((String)json.getValue("body"));
    }
    if (json.getValue("createdAt") instanceof Number) {
      obj.setCreatedAt(((Number)json.getValue("createdAt")).longValue());
    }
    if (json.getValue("id") instanceof Number) {
      obj.setId(((Number)json.getValue("id")).intValue());
    }
    if (json.getValue("liked") instanceof Boolean) {
      obj.setLiked((Boolean)json.getValue("liked"));
    }
    if (json.getValue("likes") instanceof Number) {
      obj.setLikes(((Number)json.getValue("likes")).intValue());
    }
    if (json.getValue("tags") instanceof JsonArray) {
      java.util.LinkedHashSet<java.lang.String> list = new java.util.LinkedHashSet<>();
      json.getJsonArray("tags").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setTags(list);
    }
    if (json.getValue("title") instanceof String) {
      obj.setTitle((String)json.getValue("title"));
    }
    if (json.getValue("userEmail") instanceof String) {
      obj.setUserEmail((String)json.getValue("userEmail"));
    }
    if (json.getValue("userId") instanceof Number) {
      obj.setUserId(((Number)json.getValue("userId")).intValue());
    }
    if (json.getValue("userName") instanceof String) {
      obj.setUserName((String)json.getValue("userName"));
    }
    if (json.getValue("views") instanceof Number) {
      obj.setViews(((Number)json.getValue("views")).intValue());
    }
  }

  public static void toJson(Post obj, JsonObject json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody());
    }
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