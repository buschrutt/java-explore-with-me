package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.error.ewmException;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto) throws ewmException;

    CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) throws ewmException;

    void deleteCategory(Integer catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);
}
