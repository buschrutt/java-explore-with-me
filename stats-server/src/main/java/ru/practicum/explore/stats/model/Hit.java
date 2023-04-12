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
@Data
@ToString
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "app")
    String app;

    @Column(name = "uri")
    String uri;

    @Column(name = "ip")
    String ip;

    @Column(name = "view_date")
    LocalDateTime viewDate;
}
