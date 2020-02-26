package com.rednavis.core.service;

import com.rednavis.core.dto.CurrentUserDetails;
import reactor.core.publisher.Mono;

public interface CurrentUserService {

  Mono<CurrentUserDetails> getCurrentUser();
}
