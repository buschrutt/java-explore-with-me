package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Data
@PackagePrivate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "lat")
    Float lat;
    @Column(name = "lon")
    Float lon;
}
