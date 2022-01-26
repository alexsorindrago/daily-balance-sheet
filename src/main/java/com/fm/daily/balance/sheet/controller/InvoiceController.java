package com.fm.daily.balance.sheet.controller;

import com.fm.daily.balance.sheet.CsvReader;
import com.fm.daily.balance.sheet.dto.CustomerInvoiceVM;
import com.fm.daily.balance.sheet.dto.InvoiceDto;
import com.fm.daily.balance.sheet.models.Customer;
import com.fm.daily.balance.sheet.models.Invoice;
import com.fm.daily.balance.sheet.models.Supplier;
import com.fm.daily.balance.sheet.repository.CustomerRepository;
import com.fm.daily.balance.sheet.repository.InvoiceRepository;
import com.fm.daily.balance.sheet.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/invoices")
@RestController
public class InvoiceController {

    private static final String CUSTOMER_SOURCE = "src/main/resources/customerData.csv";
    private static final String SUPPLIER_SOURCE = "src/main/resources/supplierData.csv";

    private final CsvReader csvReader;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public InvoiceController(CsvReader csvReader,
                             InvoiceRepository invoiceRepository,
                             CustomerRepository customerRepository,
                             SupplierRepository supplierRepository) {
        this.csvReader = csvReader;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
    }

    @PostMapping("/customers")
    public void createCustomerInvoices() {
        List<List<String>> entries = csvReader.loadFromFile(CUSTOMER_SOURCE);

        entries.forEach(entry -> {
            CustomerInvoiceVM invoiceVM = new CustomerInvoiceVM();
            invoiceVM.name = entry.get(0);
            invoiceVM.number = entry.get(1);
            invoiceVM.value = new BigDecimal(entry.get(2));
            invoiceVM.dueDate = LocalDate.parse(entry.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            Customer customer = new Customer();
            customer.name = invoiceVM.name;
            customerRepository.save(customer);

            Invoice invoice = new Invoice();
            invoice.number = invoiceVM.number;
            invoice.value = invoiceVM.value;
            invoice.dueDate = invoiceVM.dueDate;
            invoice.customerId = customer.id;
            invoiceRepository.save(invoice);
        });
    }

    @PostMapping("/suppliers")
    public void createSupplierInvoices() {
        List<List<String>> entries = csvReader.loadFromFile(SUPPLIER_SOURCE);

        entries.forEach(entry -> {
            CustomerInvoiceVM invoiceVM = new CustomerInvoiceVM();
            invoiceVM.name = entry.get(0);
            invoiceVM.number = entry.get(1);
            invoiceVM.value = new BigDecimal(entry.get(2));
            invoiceVM.dueDate = LocalDate.parse(entry.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            Supplier supplier = new Supplier();
            supplier.name = invoiceVM.name;
            supplierRepository.save(supplier);

            Invoice invoice = new Invoice();
            invoice.number = invoiceVM.number;
            invoice.value = invoiceVM.value;
            invoice.dueDate = invoiceVM.dueDate;
            invoice.supplierId = supplier.id;
            invoiceRepository.save(invoice);
        });
    }


    @GetMapping("/customers")
    public List<InvoiceDto> findCustomerInvoices() {
        List<Customer> customers = customerRepository.findAll();

        List<InvoiceDto> result = new ArrayList<>();

        customers.forEach(customer -> {
            List<InvoiceDto> invoicesForCustomer = findInvoicesForCustomer(customer.id);
            result.addAll(invoicesForCustomer);
        });
        return result;
    }

    @GetMapping("/customers/{customerId}")
    public List<InvoiceDto> findInvoicesForCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException());

        List<Invoice> invoices = invoiceRepository.findAllByCustomerId(customerId);

        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice, customer.name))
                .collect(Collectors.toList());
    }

    @GetMapping("/suppliers")
    public List<InvoiceDto> findSupplierInvoices() {
        List<Supplier> suppliers = supplierRepository.findAll();

        List<InvoiceDto> result = new ArrayList<>();

        suppliers.forEach(supplier -> {
            List<InvoiceDto> invoicesForSupplier = findInvoicesForSupplier(supplier.id);
            result.addAll(invoicesForSupplier);
        });

        return result;
    }

    @GetMapping("/suppliers/{supplierId}")
    public List<InvoiceDto> findInvoicesForSupplier(@PathVariable Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException());

        List<Invoice> invoices = invoiceRepository.findAllBySupplierId(supplierId);

        return invoices.stream()
                .map(invoice -> toInvoiceDto(invoice, supplier.name))
                .collect(Collectors.toList());
    }

    // FIXME: is this ok?
//    @PutMapping("/{invoiceId}")
//    public void updateInvoice(Long invoiceId) {
//        invoiceRepository.findById(invoiceId)
//                .map(invoice -> {
//                    invoice.isPaid = !invoice.isPaid;
//                    return invoiceRepository.save(invoice);
//                })
//                .orElseThrow(() -> new RuntimeException());
//    }

    @PutMapping("/{invoiceId}")
    public void updateInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException());
        invoice.isPaid = !invoice.isPaid;
        invoiceRepository.save(invoice);
    }

    private InvoiceDto toInvoiceDto(Invoice invoice, String name) {
        InvoiceDto dto = new InvoiceDto();
        dto.id = invoice.id;
        dto.name = name;
        dto.number = invoice.number;
        dto.value = invoice.value;
        dto.dueDate = invoice.dueDate;
        dto.isPaid = invoice.isPaid;
        return dto;
    }

}
