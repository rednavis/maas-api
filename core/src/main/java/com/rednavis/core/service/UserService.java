package com.rednavis.core.service;

import com.rednavis.shared.dto.user.User;

public interface UserService {

  User findByEmail(String email);

  User findById(String id);

  User save(User user);

  boolean existsByEmail(String email);
}