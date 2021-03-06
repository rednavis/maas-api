package com.rednavis.database.service;

import com.rednavis.shared.dto.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> findByEmail(String email);

  Mono<User> findByUserName(String userName);

  Mono<User> findById(String id);

  Mono<User> save(User user);

  Mono<String> user();

  Mono<String> admin();

  Flux<User> findAll();
}
