package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

@Data
@Builder
@PackagePrivate
public class UserDto {
    int id;
    String name;
    String email;
}
