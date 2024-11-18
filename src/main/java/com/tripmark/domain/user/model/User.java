package com.tripmark.domain.user.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

  private Long userId;
  private String email;
  private String username;
  private String password;
  private int currentPoints;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
