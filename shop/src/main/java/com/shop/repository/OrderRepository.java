package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.member.email =:email order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);

//:email -> 쿼리에서 메서드의 파라미터값으로 변겨오딤
    //@Param : 변수와 메서드의 파라미터 연결 하기 위해 사용

}
