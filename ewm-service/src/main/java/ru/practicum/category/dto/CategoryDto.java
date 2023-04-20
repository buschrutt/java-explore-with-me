package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@PackagePrivate
public class CategoryDto {
    int id;
    String name;
}
