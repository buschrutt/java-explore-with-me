package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import java.util.List;

@Data
@Builder
@PackagePrivate
public class CompilationDto {
    Integer id;
    List<Integer> events;
    Boolean pinned;
    String title;
}
