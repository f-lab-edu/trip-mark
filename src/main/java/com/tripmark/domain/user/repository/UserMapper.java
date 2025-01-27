package com.tripmark.domain.user.repository;

import com.tripmark.domain.user.model.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface UserMapper {

  Optional<User> findByEmail(String email);

  void insertUser(User user);

  void updateCurrentPoints(@Param("userId") Long userId, @Param("currentPoints") int currentPoints);

}
