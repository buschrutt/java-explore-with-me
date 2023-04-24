package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.error.EwmException;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto) throws EwmException;

    CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) throws EwmException;

    void deleteCategory(Integer catId) throws EwmException;

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId) throws EwmException;
}
