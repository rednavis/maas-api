package com.rednavis.database.service;

import com.rednavis.shared.dto.user.User;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> findByEmail(String email);

  Mono<User> findById(String id);

  Mono<User> save(User user);

  Mono<Boolean> existsByEmail(String email);

  Mono<String> user();

  Mono<String> admin();
}
