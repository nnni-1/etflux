package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.InvestStrategy;

import java.math.BigDecimal;

@Entity
@Table(name = "user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance;

    private BigDecimal lastYearGrow;

    private BigDecimal lastMonthGrow;

    private String avatarUri;

    @Enumerated(EnumType.STRING)
    private InvestStrategy investStrategy;
}
