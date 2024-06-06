package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByFullName(String fullName);
    Optional<User> findByRoleId(String roleId);
    long count();
    @Query(value = "SELECT COUNT(*) FROM users u WHERE YEAR(u.created_at) = :year", nativeQuery = true)
    long countUsersByYear(@Param("year") int year);
}
