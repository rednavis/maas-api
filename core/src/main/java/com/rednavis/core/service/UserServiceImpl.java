package com.rednavis.core.service;

import com.rednavis.core.mapper.UserMapper;
import com.rednavis.core.repository.UserRepository;
import com.rednavis.shared.dto.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private static final UserMapper USER_MAPPER = UserMapper.USER_MAPPER;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<User> findByEmail(String email) {
    return userRepository.findByEmail(email)
        .map(userEntity -> USER_MAPPER.entityToDto(userEntity));
  }

  @Override
  public Mono<User> findById(String id) {
    return userRepository.findById(id)
        //.switchIfEmpty(Mono.error(new NotFoundException("User not found [id: " + id + "]")))
        .map(userEntity -> USER_MAPPER.entityToDto(userEntity));
  }

  @Override
  public Mono<User> save(User user) {
    return Mono.just(user)
        .map(userMono -> USER_MAPPER.dtoToEntity(user))
        .flatMap(userEntity -> userRepository.save(userEntity))
        .map(userEntity -> USER_MAPPER.entityToDto(userEntity));
  }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
