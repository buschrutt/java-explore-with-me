package ru.practicum.category.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Data
@PackagePrivate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "name")
    String name;
}
