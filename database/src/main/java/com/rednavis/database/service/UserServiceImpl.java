package com.rednavis.database.service;

import static com.rednavis.database.mapper.MapperProvider.USER_MAPPER;

import com.rednavis.core.service.CurrentUserService;
import com.rednavis.database.repository.UserRepository;
import com.rednavis.shared.dto.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CurrentUserService currentUserService;

  @Override
  public Mono<User> findByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(USER_MAPPER::entityToDto);
  }

  @Override
  public Mono<User> findById(String id) {
    return userRepository.findById(id)
        .map(USER_MAPPER::entityToDto);
  }

  @Override
  public Mono<User> save(User user) {
    return Mono.just(user)
        .map(USER_MAPPER::dtoToEntity)
        .flatMap(userEntity -> userRepository.save(userEntity))
        .map(USER_MAPPER::entityToDto);
  }

  @Override
  public Mono<String> user() {
    return Mono.just("Content for user");
  }

  @Override
  public Mono<String> admin() {
    return Mono.just("Content for admin");
  }
}
