package com.rednavis.core.service;

import com.rednavis.core.entity.UserEntity;
import com.rednavis.core.exception.NotFoundException;
import com.rednavis.core.mapper.UserMapper;
import com.rednavis.core.repository.UserRepository;
import com.rednavis.shared.dto.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private static final UserMapper USER_MAPPER = UserMapper.USER_MAPPER;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public User findByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found [email: " + email + "]"));
    return USER_MAPPER.entityToDto(userEntity);
  }

  @Override
  @Transactional
  public User loadUserById(long id) {
    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User not found [id: " + id + "]"));
    return USER_MAPPER.entityToDto(userEntity);
  }

  @Override
  public User save(User user) {
    UserEntity userEntity = USER_MAPPER.dtoToEntity(user);
    userEntity = userRepository.save(userEntity);
    return USER_MAPPER.entityToDto(userEntity);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
