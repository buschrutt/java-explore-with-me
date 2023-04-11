package ru.practicum.explore.stats.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO stage stat_svc.
 */

@Entity
@Builder
@Table(name = "stats")
@Getter
@Setter
@ToString
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "app")
    String app;

    @Column(name = "uri")
    Integer uri;

    @Column(name = "ip")
    Integer ip;

    @Column(name = "view_date")
    LocalDateTime timestamp;
}
