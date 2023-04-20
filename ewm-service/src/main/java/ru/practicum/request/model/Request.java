package ru.practicum.request.model;

import lombok.*;
import lombok.experimental.PackagePrivate;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@PackagePrivate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "requester")
    Integer requester;
    @Column(name = "event")
    Integer event;
    @Column(name = "created")
    LocalDateTime created;
    @Column (name = "status")
    String status;
}
