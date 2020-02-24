package io.seanapse.shop.service;

import io.seanapse.shop.domain.Category;
import io.seanapse.shop.repository.CategoryRepository;
import io.seanapse.shop.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAll(){
        log.debug("Request to get all Categories.");

        return this.categoryRepository.findAll()
                .stream()
                .map(CategoryService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id){
        log.debug("Request to get category: {}", id);
        return this.categoryRepository.findById(id).map(CategoryService::mapToDto)
                .orElseThrow(IllegalAccessError::new);
    }

    public CategoryDto create(CategoryDto categoryDto) {
        log.debug("Request to create category: {}", categoryDto);
        Category category = new Category(
                categoryDto.getName(),
                categoryDto.getDescription(),
                Collections.emptySet()
        );
        return mapToDto(this.categoryRepository.save(category));
    }

    public void delete(Long id){
        log.debug("Request to delete category: {}, id");
        this.categoryRepository.deleteById(id);
    }

    public static CategoryDto mapToDto(Category category) {
        if (category != null) {
            return new CategoryDto(
                    category.getId(),
                    category.getName(),
                    category.getDescription(),
                    category.getProducts()
                            .stream()
                            .map(ProductService::mapToDto)
                            .collect(Collectors.toSet())
            );
        }
        return null;
    }
}
