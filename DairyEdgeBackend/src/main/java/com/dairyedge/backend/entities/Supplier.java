package com.dairyedge.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    @Column(name = "is_active")
    private Boolean isActive;
}
