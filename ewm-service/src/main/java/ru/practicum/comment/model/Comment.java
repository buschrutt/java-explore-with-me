package ru.practicum.comment.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "comments")
@Getter
@Setter
@ToString
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name = "event_id")
    Integer eventId;
    @Column (name = "commentator_id")
    Integer commentatorId;
    @Column (name = "text")
    String text;
    @Column (name = "created")
    LocalDateTime created;
    @Column (name = "status")
    String status;
}
