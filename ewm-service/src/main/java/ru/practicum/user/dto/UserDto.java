package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;

@Data
@Builder
@PackagePrivate
public class UserDto {
    int id;
    @NotNull
    String name;
    @NotNull
    String email;
}
