package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@PackagePrivate
public class UserDto {
    int id;
    @NotNull
    String name;
    @NotNull
    String email;
}
