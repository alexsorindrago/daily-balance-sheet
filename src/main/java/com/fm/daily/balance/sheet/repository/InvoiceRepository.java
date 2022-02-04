package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByNumber(String number);

    @Query("FROM Invoice i " +
            "WHERE (:invoiceType is null or i.invoiceType = :invoiceType) " +
            "AND (:entityName is null or i.entityName = :entityName) " +
            "AND (:startDate is null or i.dueDate >= :startDate) " +
            "AND (:endDate is null or i.dueDate <= :endDate)"
    )
    List<Invoice> findInvoices(@Param("invoiceType") InvoiceType invoiceType,
                               @Param("entityName") String entityName,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate
    );
}
