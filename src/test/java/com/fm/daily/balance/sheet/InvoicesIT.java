package com.fm.daily.balance.sheet;

import com.fm.daily.balance.sheet.controller.InvoiceController;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InvoicesIT {


    @Autowired
    CsvReader csvReader;
    @Autowired
    private InvoiceController invoiceController;

    @Test
    void shouldDisplayCustomers() {
        // given

        // when
        List<InvoiceDto> customerInvoices = invoiceController.findCustomerInvoices();

        // then
        assertThat(customerInvoices).hasSize(2);
    }
}