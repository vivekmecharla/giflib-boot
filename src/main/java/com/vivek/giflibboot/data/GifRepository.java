package com.vivek.giflibboot.data;

import com.vivek.giflibboot.model.Gif;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface GifRepository extends CrudRepository<Gif, String> {
    Gif findById(Long gifId);
}
