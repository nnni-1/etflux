package ru.alexeev.etflux.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "asset_transaction",
            joinColumns = @JoinColumn(name = "transaction_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "asset_id", unique = false))
    private List<Asset> assets;
}
