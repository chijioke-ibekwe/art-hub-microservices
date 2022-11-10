package com.arthub.artservice.entity;

import com.arthub.artservice.enums.ArtType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "arts")
public class Art {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ArtType type;

    private String description;

    private String countryOfOrigin;

    private BigDecimal price;

    private Long artistId;

    private String artistName;
}
