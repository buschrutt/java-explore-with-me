package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@PackagePrivate
public class CompilationDto {
    Integer id;
    @NotNull
    List<Integer> events;
    @NotNull
    Boolean pinned;
    @NotNull
    String title;
}
