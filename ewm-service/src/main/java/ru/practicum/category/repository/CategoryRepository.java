package ru.practicum.category.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findCategoriesByIdNot(Integer unreal, Pageable pageable);
}
