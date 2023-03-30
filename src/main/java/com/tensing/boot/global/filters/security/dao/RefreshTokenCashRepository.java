package com.tensing.boot.global.filters.security.dao;

import com.tensing.boot.global.filters.security.model.vo.RefreshTokenCache;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenCashRepository extends CrudRepository<RefreshTokenCache, Long> {
}
