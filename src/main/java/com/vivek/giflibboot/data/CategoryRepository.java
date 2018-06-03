package com.vivek.giflibboot.data;

import com.vivek.giflibboot.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CategoryRepository extends CrudRepository<Category, String> {
    Category findById(Long id);

    Category findByName(String name);

    Category findByColorCode(String colorCode);
}
