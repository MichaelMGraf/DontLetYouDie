package de.dontletyoudie.backend.persistence.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("categoryService")
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
}
