package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
