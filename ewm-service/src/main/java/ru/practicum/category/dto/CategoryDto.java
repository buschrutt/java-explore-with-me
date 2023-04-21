package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;

@Data
@Builder
@PackagePrivate
public class CategoryDto {
    int id;
    @NotNull
    String name;
}
