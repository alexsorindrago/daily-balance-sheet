package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesmanRepository extends JpaRepository<Salesman, Long> {
}
