package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
