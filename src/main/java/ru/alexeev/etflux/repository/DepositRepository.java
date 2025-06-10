package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeev.etflux.entity.assets.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
