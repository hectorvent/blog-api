/*
 * Copyright 2014 Red Hat, Inc.
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
    if (json.getValue("id") instanceof Number) {
      obj.setId(((Number)json.getValue("id")).intValue());
    }
    if (json.getValue("title") instanceof String) {
      obj.setTitle((String)json.getValue("title"));
    }
    if (json.getValue("userId") instanceof Number) {
      obj.setUserId(((Number)json.getValue("userId")).intValue());
    }
  }

  public static void toJson(Post obj, JsonObject json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody());
    }
    json.put("id", obj.getId());
    if (obj.getTitle() != null) {
      json.put("title", obj.getTitle());
    }
    json.put("userId", obj.getUserId());
  }
}