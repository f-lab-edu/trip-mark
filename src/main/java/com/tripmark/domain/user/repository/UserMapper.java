package com.tripmark.domain.user.repository;

import com.tripmark.domain.user.model.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  Optional<User> findByEmail(String email);

  void insertUser(User user);
}
