package com.rednavis.core.service;

import com.rednavis.shared.security.CurrentUser;
import reactor.core.publisher.Mono;

public interface CurrentUserService {

  Mono<CurrentUser> getCurrentUser();
}
