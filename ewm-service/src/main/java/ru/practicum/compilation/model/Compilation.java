package ru.practicum.compilation.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "compilations")
@Getter
@Setter
@ToString
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name = "pinned")
    Boolean pinned;
    @Column (name = "title")
    String title;
}
