package com.example.mern_stack_challenge.mernstackchallenge.Repository;
import com.example.mern_stack_challenge.mernstackchallenge.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
    List<Transaction> findByDateOfSaleBetween(LocalDate startDate, LocalDate endDate);
    List<Transaction> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrPriceBetween(String title, String description, double priceMin, double priceMax);
    Page<Transaction> findByTitleContainingOrDescriptionContainingOrPriceContaining(String search, String search2,
            String search3, Pageable pageable);
   
}

