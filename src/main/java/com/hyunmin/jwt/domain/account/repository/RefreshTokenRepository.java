package com.hyunmin.jwt.domain.account.repository;

import com.hyunmin.jwt.domain.account.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
