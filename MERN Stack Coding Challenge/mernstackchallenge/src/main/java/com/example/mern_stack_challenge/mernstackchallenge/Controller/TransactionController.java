package com.example.mern_stack_challenge.mernstackchallenge.Controller;
import com.example.mern_stack_challenge.mernstackchallenge.Entity.Transaction;
import com.example.mern_stack_challenge.mernstackchallenge.Service.TransactionService;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;


@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

     @GetMapping("/api/transactions")
    public ResponseEntity<String> getEndpoint() {
        return ResponseEntity.ok("Access Granted");
    }

    @GetMapping("/initialize")
    public String initializeDatabase() {
        transactionService.initializeDatabase();
        return "Database initialized with seed data";
    }
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(@RequestParam int year, @RequestParam int month) {
        LocalDate selectedMonth = LocalDate.of(year, month, 1);
        return transactionService.getStatistics(selectedMonth);
    }
    @GetMapping
    public Page<Transaction> getAllTransactions(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) String search) {
        Pageable paging = PageRequest.of(page - 1, size);
        return transactionService.getAllTransactions(paging, search);
    }

  
      @GetMapping("/search")
    public List<Transaction> searchTransactions(@RequestParam String query) {
        return transactionService.searchTransactions(query);
    }
}
