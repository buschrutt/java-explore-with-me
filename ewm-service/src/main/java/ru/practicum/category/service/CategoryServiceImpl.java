package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.EwmException;
import ru.practicum.error.model.EwmExceptionModel;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryDtoMapper.toCategory(categoryDto);
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) {
        Category category = CategoryDtoMapper.toCategory(categoryDto);
        Category newCategory = categoryRepository.findById(catId).orElseThrow();
        newCategory.setName(category.getName());
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategory(Integer catId) throws EwmException {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Category id:" + catId + " was not found", "The required object was not found.", "NOT_FOUND",
                                HttpStatus.NOT_FOUND)));
        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new EwmException(new EwmExceptionModel(e.getMessage(), "Data Integrity Violation.", "CONFLICT", // 409
                    HttpStatus.CONFLICT));
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        List<Category> categoryList = categoryRepository.findCategoriesByIdNot(-1, pageable);
        categoryList.forEach(category -> categoryDtoList.add(CategoryDtoMapper.toCategoryDto(category)));
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) throws EwmException {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new EwmException(new EwmExceptionModel("Category id:" + catId + " was not found", "The required object was not found.", "NOT_FOUND",
                        HttpStatus.NOT_FOUND)));
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }
}
