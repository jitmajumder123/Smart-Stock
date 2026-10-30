package com.smartstock.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {
    @EntityGraph(attributePaths="product") List<CustomerOrder> findAllByOrderByOrderDateDesc();
    @EntityGraph(attributePaths="product") Optional<CustomerOrder> findWithProductById(Long id);
    long countByStatusNot(String status);
    @EntityGraph(attributePaths="product") List<CustomerOrder> findByOrderDateBetweenOrderByOrderDateDesc(LocalDate from, LocalDate to);
}
