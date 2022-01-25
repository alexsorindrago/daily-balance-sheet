package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
