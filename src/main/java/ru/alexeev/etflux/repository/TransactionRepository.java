package ru.alexeev.etflux.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alexeev.etflux.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
