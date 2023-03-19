package com.tensing.boot.global.security.repository;

import com.tensing.boot.global.security.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
