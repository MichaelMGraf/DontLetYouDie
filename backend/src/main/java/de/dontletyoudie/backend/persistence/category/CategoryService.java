package de.dontletyoudie.backend.persistence.category;

import de.dontletyoudie.backend.persistence.category.exceptions.CategoryAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("categoryService")
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(String name, Boolean essential) throws CategoryAlreadyExistsException {
        if (categoryRepository.findCategoryByName(name).isPresent())
            throw new CategoryAlreadyExistsException(name);

        return categoryRepository.save(
                new Category(name, essential));
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
