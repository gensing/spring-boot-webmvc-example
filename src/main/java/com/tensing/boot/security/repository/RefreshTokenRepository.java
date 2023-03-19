package com.tensing.boot.security.repository;

import com.tensing.boot.security.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
