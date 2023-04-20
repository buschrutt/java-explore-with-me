package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

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
        Category newCategory;
        newCategory = categoryRepository.findById(catId).orElseThrow();
        newCategory.setName(category.getName());
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategory(Integer catId) {
        categoryRepository.delete(categoryRepository.findById(catId).orElseThrow());
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        List<Category> categoryList = categoryRepository.findAllById(-1, pageable);
        categoryList.forEach(category -> categoryDtoList.add(CategoryDtoMapper.toCategoryDto(category)));
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow();
        return CategoryDtoMapper.toCategoryDto(categoryRepository.save(category));
    }
}
