package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
    @Query(value = "SELECT SUM(o.totalMoney) FROM Order o WHERE o.status = 'Success' AND FUNCTION('YEAR', o.createdAt) = :year")
    Double calculateTotalMoneyOfSuccessfulOrders(@Param("year") int year);
    long count();
    @Query(value = "SELECT COUNT(o) FROM Order o WHERE o.status = :status AND FUNCTION('YEAR', o.createdAt) = :year")
    long countOrdersByStatusAndYear(@Param("status") String status, @Param("year") int year);
    @Query(value = "SELECT MONTH(o.created_at) as month, SUM(o.total_money) as totalMoney " +
            "FROM orders o " +
            "WHERE o.status = :status AND YEAR(o.created_at) = :year " +
            "GROUP BY MONTH(o.created_at)", nativeQuery = true)
    List<Object[]> getTotalMoneyByStatusAndYearGroupedByMonth(@Param("status") String status, @Param("year") int year);
}
