package ru.practicum.user.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "users")
@Getter
@Setter
@ToString
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name = "name")
    String name;
    @Column (name = "email")
    String email;
}