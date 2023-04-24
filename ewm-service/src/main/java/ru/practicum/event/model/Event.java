package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@PackagePrivate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "category")
    Integer category;
    @Column(name = "initiator")
    Integer initiator;
    @Column (name = "annotation")
    String annotation;
    @Column (name = "description")
    String description;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @Column(name = "location")
    Integer location;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Column (name = "state")
    String state;
    @Column (name = "title")
    String title;
}
