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
 * Converter for {@link com.itla.blogapi.post.Comment}.
 *
 * NOTE: This class has been automatically generated from the {@link com.itla.blogapi.post.Comment} original class using Vert.x codegen.
 */
public class CommentConverter {

  public static void fromJson(JsonObject json, Comment obj) {
    if (json.getValue("body") instanceof String) {
      obj.setBody((String)json.getValue("body"));
    }
    if (json.getValue("createdAt") instanceof Number) {
      obj.setCreatedAt(((Number)json.getValue("createdAt")).longValue());
    }
    if (json.getValue("id") instanceof Number) {
      obj.setId(((Number)json.getValue("id")).intValue());
    }
    if (json.getValue("postId") instanceof Number) {
      obj.setPostId(((Number)json.getValue("postId")).intValue());
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
  }

  public static void toJson(Comment obj, JsonObject json) {
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