// Copyright (C) 2013 The Android Open Source Project
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

package com.google.gerrit.server.account;

import com.google.common.collect.Lists;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.reviewdb.client.AccountExternalId;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

public class GetEmails implements RestReadView<AccountResource> {

  private final Provider<CurrentUser> self;
  private final Provider<ReviewDb> dbProvider;

  @Inject
  public GetEmails(Provider<CurrentUser> self, Provider<ReviewDb> dbProvider) {
    this.self = self;
    this.dbProvider = dbProvider;
  }

  @Override
  public List<EmailInfo> apply(AccountResource rsrc) throws AuthException,
      OrmException {
    if (!(self.get() instanceof IdentifiedUser)) {
      throw new AuthException("Authentication required");
    }
    IdentifiedUser s = (IdentifiedUser) self.get();
    if (s.getAccountId().get() != rsrc.getUser().getAccountId().get()
        && !s.getCapabilities().canAdministrateServer()) {
      throw new AuthException("not allowed to list email addresses");
    }

    List<EmailInfo> emails = Lists.newArrayList();
    ResultSet<AccountExternalId> ids =
        dbProvider.get().accountExternalIds()
            .byAccount(rsrc.getUser().getAccountId());
    for (AccountExternalId extId : ids) {
      String email = extId.getEmailAddress();
      if (email != null) {
        EmailInfo e = new EmailInfo();
        e.email = email;
        e.setPreferred(email.equals(rsrc.getUser().getAccount()
            .getPreferredEmail()));
        emails.add(e);
      }
    }
    return emails;
  }

  public static class EmailInfo {
    public String email;
    public Boolean preferred;

    void setPreferred(boolean preferred) {
      this.preferred = preferred ? true : null;
    }
  }
}
