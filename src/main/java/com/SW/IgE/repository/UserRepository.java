package com.SW.IgE.repository;

import com.SW.IgE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUseremail(String useremail);

    // 이메일로 사용자 검색 (Optional<User> 반환)
    Optional<User> findByUseremail(String useremail);

    // 이메일로 사용자 검색 (user_ige 포함)
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.user_ige WHERE u.useremail = :email")
    Optional<User> findUserWithAllDetailsByEmail(@Param("email") String useremail);



}
