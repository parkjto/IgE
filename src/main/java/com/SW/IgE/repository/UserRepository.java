package com.SW.IgE.repository;

import com.SW.IgE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // 이메일로 사용자 검색 (Optional<User> 반환)
    Optional<User> findByUseremail(String useremail);

    // 이메일 존재 여부 확인
    boolean existsByUseremail(String useremail);
}
