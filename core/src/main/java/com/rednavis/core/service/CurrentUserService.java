package com.rednavis.core.service;

import com.rednavis.core.dto.CurrentUser;
import reactor.core.publisher.Mono;

public interface CurrentUserService {

  Mono<CurrentUser> getCurrentUser();
}
