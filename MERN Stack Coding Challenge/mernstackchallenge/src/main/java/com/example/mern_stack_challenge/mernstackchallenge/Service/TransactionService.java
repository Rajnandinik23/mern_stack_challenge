package com.example.mern_stack_challenge.mernstackchallenge.Service;
import java.time.LocalDate;
import com.example.mern_stack_challenge.mernstackchallenge.Entity.Transaction;
import com.example.mern_stack_challenge.mernstackchallenge.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final String API_URL = "https://s3.amazonaws.com/roxiler.com/product_transaction.json";

    public void initializeDatabase() {
        RestTemplate restTemplate = new RestTemplate();
        Transaction[] transactions = restTemplate.getForObject(API_URL, Transaction[].class);
        if (transactions != null) {
            transactionRepository.saveAll(List.of(transactions));
        }
    }
    public Page<Transaction> getAllTransactions(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return transactionRepository.findAll(pageable);
        } else {
            return transactionRepository.findByTitleContainingOrDescriptionContainingOrPriceContaining(search, search, search, pageable);
        }
    }

   

    public Map<String, Object> getStatistics(LocalDate month) {
        List<Transaction> transactions = transactionRepository.findAll();
        
        // Filter transactions based on the month and year
        int selectedMonth = month.getMonthValue();
        int selectedYear = month.getYear();

        double totalSaleAmount = transactions.stream()
                .filter(t -> t.getDateOfSale().getMonthValue() == selectedMonth && t.getDateOfSale().getYear() == selectedYear)
                .filter(Transaction::isSold)
                .mapToDouble(Transaction::getPrice)
                .sum();

        long totalSoldItems = transactions.stream()
                .filter(t -> t.getDateOfSale().getMonthValue() == selectedMonth && t.getDateOfSale().getYear() == selectedYear)
                .filter(Transaction::isSold)
                .count();

        long totalNotSoldItems = transactions.stream()
                .filter(t -> t.getDateOfSale().getMonthValue() == selectedMonth && t.getDateOfSale().getYear() == selectedYear)
                .filter(t -> !t.isSold())
                .count();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalSaleAmount", totalSaleAmount);
        statistics.put("totalSoldItems", totalSoldItems);
        statistics.put("totalNotSoldItems", totalNotSoldItems);
        return statistics;
    }
    public void saveAll(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    public List<Transaction> getTransactionsByMonth(Month month) {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.findByDateOfSaleBetween(startDate, endDate);
    }

    public List<Transaction> searchTransactions(String query) {
        return transactionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrPriceBetween(query, query, 0, Double.MAX_VALUE);
    }
}

