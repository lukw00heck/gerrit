// Copyright (C) 2016 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.extensions.api.projects;

import java.util.Objects;

public class CommentLinkInfo {
  public String match;
  public String link;
  public String html;
  public Boolean enabled; // null means true

  public transient String name;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof CommentLinkInfo) {
      CommentLinkInfo that = (CommentLinkInfo) o;
      return Objects.equals(this.match, that.match)
          && Objects.equals(this.link, that.link)
          && Objects.equals(this.html, that.html)
          && Objects.equals(this.enabled, that.enabled);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (this.match == null) ? 0 : this.match.hashCode();
    h *= 1000003;
    h ^= (this.link == null) ? 0 : this.link.hashCode();
    h *= 1000003;
    h ^= (this.html == null) ? 0 : this.html.hashCode();
    h *= 1000003;
    h ^= (this.enabled == null) ? 0 : this.enabled.hashCode();
    return h;
  }
}
