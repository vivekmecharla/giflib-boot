package com.vivek.giflibboot.data;

import com.vivek.giflibboot.model.Category;
import com.vivek.giflibboot.model.Gif;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GifRepository extends CrudRepository<Gif, String> {
    Gif findById(Long gifId);

    List<Gif> findByCategory(Category category);

    List<Gif> findByFavorite(boolean isFavorite);
}
