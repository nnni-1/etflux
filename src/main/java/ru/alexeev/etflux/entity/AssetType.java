package ru.alexeev.etflux.entity;


import lombok.*;
import ru.alexeev.etflux.dictionary.AssetTypeName;

import javax.persistence.*;


@Entity
@Table(name = "asset_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssetTypeName name;

    private String description;
}
