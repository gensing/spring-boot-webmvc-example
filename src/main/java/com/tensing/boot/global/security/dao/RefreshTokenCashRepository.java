package com.tensing.boot.global.security.dao;

import com.tensing.boot.global.security.model.data.RefreshTokenCache;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenCashRepository extends CrudRepository<RefreshTokenCache, Long> {
}
