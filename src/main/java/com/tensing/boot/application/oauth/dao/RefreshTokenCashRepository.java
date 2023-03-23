package com.tensing.boot.application.oauth.dao;

import com.tensing.boot.global.filters.security.model.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenCashRepository extends CrudRepository<RefreshToken, Long> {
}
