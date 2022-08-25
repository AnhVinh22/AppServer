package com.example.demo.service.impl;

import com.example.demo.converter.CategoryConverter;
import com.example.demo.converter.DateConvert;
import com.example.demo.domain.Category;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.NewsNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.response.*;
import com.example.demo.service.CategoryService;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final JwtService jwtService;

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    @Override
    public CategoryResponse insertCategory(String token, CreateCategoryRequest boxRequest) {
        JwtData data = jwtService.parseToken(token);
        String userId = data.getUserId();
        Category category = CategoryConverter.convertBoxRequestToBox(userId, boxRequest);
        categoryRepository.insert(category);
        return CategoryConverter.convertCategoryToCategoryResponse(category);
    }

    @Override
    public Category save(String token, UpdateCategoryRequest request, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (categoryId.isEmpty()) {
            throw new CustomException(ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + categoryId);
        }
        Category updateCategory = category.get();
        JwtData data = jwtService.parseToken(token);
        String userId = data.getUserId();
        return categoryRepository.save(CategoryConverter.convertUpdateCategoryRequestToCategory(userId, request, updateCategory));
    }

    @Override
    public void deleteCategoryById(String token, String categoryId) {
        JwtData data = jwtService.parseToken(token);
        String createBoxBy = data.getUserId();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new CustomException(ResponseObject.MESSAGE_CATEGORY_NOT_FOUND);
        }
        Category category1 = category.get();
        if (!createBoxBy.equals(category1.getCreateUserId())) {
            throw new CustomException(ResponseObject.MESSAGE_CATEGORY_NOT_FOUND + categoryId);
        }
        questionRepository.deleteAllByCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public ListCategoryResponse getListCategory(int page, int pageSize) {
        List<CategoryResponse> collect = categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Category::getLastUpdateTime).reversed())
                .map(category -> {
                    int num = questionRepository.countAllByCategoryId(category.getId());
                    return CategoryResponse.builder()
                            .id(category.getId())
                            .banner(category.getBanner())
                            .title(category.getTitle())
                            .lastUpdateTime(DateConvert.convertLongToDate(category.getLastUpdateTime()))
                            .questionNum(num)
                            .build();
                })
                .skip((long) page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return new ListCategoryResponse(collect);
    }

    @Override
    public List<ListQuestionResponse> findAllQuestionByCategoryId(String categoryId) {
        List<ListQuestionResponse> questionResponses = questionRepository.findAllByCategoryIdOrderByCreateTimeDesc(categoryId)
                .stream()
                .map(question ->
                    ListQuestionResponse.builder()
                            .id(question.getId())
                            .content(question.getContentQuestion())
                            .build()
                )
                .collect(Collectors.toList());

        return questionResponses;
    }
}
