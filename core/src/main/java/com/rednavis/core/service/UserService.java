package com.rednavis.core.service;

import com.rednavis.shared.dto.user.User;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> findByEmail(String email);

  Mono<User> findById(String id);

  Mono<User> save(User user);

  Mono<Boolean> existsByEmail(String email);
}
