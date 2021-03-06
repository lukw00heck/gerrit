// Copyright (C) 2017 The Android Open Source Project
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

package com.google.gerrit.server.query.project;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.gerrit.index.project.ProjectData;
import com.google.gerrit.index.query.LimitPredicate;
import com.google.gerrit.index.query.Predicate;
import com.google.gerrit.index.query.QueryBuilder;
import com.google.gerrit.index.query.QueryParseException;
import com.google.gerrit.reviewdb.client.Project;
import com.google.inject.Inject;
import java.util.List;

/** Parses a query string meant to be applied to project objects. */
public class ProjectQueryBuilder extends QueryBuilder<ProjectData> {
  public static final String FIELD_LIMIT = "limit";

  private static final QueryBuilder.Definition<ProjectData, ProjectQueryBuilder> mydef =
      new QueryBuilder.Definition<>(ProjectQueryBuilder.class);

  @Inject
  ProjectQueryBuilder() {
    super(mydef);
  }

  @Operator
  public Predicate<ProjectData> name(String name) {
    return ProjectPredicates.name(new Project.NameKey(name));
  }

  @Operator
  public Predicate<ProjectData> inname(String namePart) {
    if (namePart.isEmpty()) {
      return name(namePart);
    }
    return ProjectPredicates.inname(namePart);
  }

  @Operator
  public Predicate<ProjectData> description(String description) throws QueryParseException {
    if (Strings.isNullOrEmpty(description)) {
      throw error("description operator requires a value");
    }

    return ProjectPredicates.description(description);
  }

  @Override
  protected Predicate<ProjectData> defaultField(String query) throws QueryParseException {
    // Adapt the capacity of this list when adding more default predicates.
    List<Predicate<ProjectData>> preds = Lists.newArrayListWithCapacity(3);
    preds.add(name(query));
    preds.add(inname(query));
    if (!Strings.isNullOrEmpty(query)) {
      preds.add(description(query));
    }
    return Predicate.or(preds);
  }

  @Operator
  public Predicate<ProjectData> limit(String query) throws QueryParseException {
    Integer limit = Ints.tryParse(query);
    if (limit == null) {
      throw error("Invalid limit: " + query);
    }
    return new LimitPredicate<>(FIELD_LIMIT, limit);
  }
}
