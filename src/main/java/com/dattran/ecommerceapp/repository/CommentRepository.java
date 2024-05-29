package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByProductId(@Param("productId") String productId);
    boolean findByProductIdAndUserId(String productId, String userId);
    @Query("SELECT c FROM Comment c WHERE c.star > :star ORDER BY c.star DESC")
    List<Comment> findByStarGreaterThan(@Param("star") int star);
}
