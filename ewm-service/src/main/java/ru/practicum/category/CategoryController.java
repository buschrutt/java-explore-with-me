package ru.practicum.category;

import lombok.experimental.PackagePrivate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.error.EwmException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@PackagePrivate
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) throws EwmException {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto changeCategory(@PathVariable Integer catId, @Valid @RequestBody CategoryDto categoryDto) throws EwmException {
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) throws EwmException {
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                    @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) throws EwmException {
        return categoryService.getCategoryById(catId);
    }
}
