package ru.practicum.compilation.model;

import lombok.*;
import lombok.experimental.PackagePrivate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Table(name = "events_to_compilations")
@Getter
@Setter
@PackagePrivate
@AllArgsConstructor
@NoArgsConstructor
public class CompilationEvent {
    @Column(name = "event_id")
    Integer eventId;
    @Id
    @Column(name = "compilation_id")
    Integer compilationId;
}
