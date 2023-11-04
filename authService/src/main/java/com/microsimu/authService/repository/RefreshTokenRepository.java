package com.microsimu.authService.repository;

import com.microsimu.authService.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
	RefreshTokenEntity findByUsername(String username);
}
