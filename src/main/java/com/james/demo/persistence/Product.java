package com.james.demo.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    private UUID id;

    private String name;
    private String category;
}
