package com.smartstock.supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SupplierRepository extends JpaRepository<Supplier,Long> {
 List<Supplier> findAllByOrderByNameAsc();
 boolean existsByNameIgnoreCase(String name);
}
