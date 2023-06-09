package com.example.financetracker.service;

import com.example.financetracker.model.entities.*;
import com.example.financetracker.model.exceptions.NotFoundException;
import com.example.financetracker.model.exceptions.UnauthorizedException;
import com.example.financetracker.model.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public abstract class AbstractService {

    @Autowired
    protected CurrencyRepository currencyRepository;
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected FrequencyRepository frequencyRepository;

    @Autowired
    protected PlannedPaymentRepository plannedPaymentRepository;

    @Autowired
    protected LoginLocationRepository loginLocationRepository;

    @Autowired
    protected ModelMapper mapper;

    protected Logger logger = LogManager.getLogger(getClass().getName());

    protected User getUserById(int id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    protected Account getAccountById(int id){
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
    }

    protected Transaction getTransactionById(int id){
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    protected Category getCategoryById(int id){
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    protected Frequency getFrequencyById(int id){
        return frequencyRepository.findById(id).orElseThrow(() -> new NotFoundException("Frequency not found"));
    }

    protected PlannedPayment getPlannedPaymentById(int id){
        return plannedPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Planned payment not found"));
    }

    protected Currency getCurrencyById(int id){
        return currencyRepository.findById(id).orElseThrow(() -> new NotFoundException("Currency not found"));
    }

    protected void checkSufficientFunds(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new UnauthorizedException("Insufficient funds in sender account.");
        }
    }

    protected void authenticateUser(User accountOwner, User user){
        if (!accountOwner.equals(user)) {
            throw new UnauthorizedException("Unauthorized access. The service cannot be executed.");
        }
    }

    protected void checkUserAuthorization(int id, int loggedUserId){
        if (id != loggedUserId) {
            throw new UnauthorizedException("Unauthorized access. The service cannot be executed.");
        }
    }

    protected void checkIfTransactionsExist(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            throw new NotFoundException("Transactions not found");
        }
    }

    protected void checkIfTransactionsExist(Page<Transaction> transactions) {
        if (transactions.isEmpty()) {
            throw new NotFoundException("Transactions not found");
        }
    }
}
