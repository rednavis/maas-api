package com.rednavis.auth.service;

import com.rednavis.auth.security.UserPrincipal;
import com.rednavis.shared.dto.auth.UserSummary;

public interface UserAuthService {

  UserSummary getCurrentUser(UserPrincipal userPrincipal);
}
