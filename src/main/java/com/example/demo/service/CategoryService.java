package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.response.CategoryResponse;
import com.example.demo.response.ListCategoryResponse;
import com.example.demo.response.ListQuestionResponse;
import com.example.demo.response.QuestionResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse insertCategory(String token, CreateCategoryRequest boxRequest);

    Category save(String token, UpdateCategoryRequest updateCategoryRequest, String categoryId);

    void deleteCategoryById(String token, String categoryId);

    ListCategoryResponse getListCategory(int page, int pageSize);

    List<ListQuestionResponse> findAllQuestionByCategoryId(String categoryId);
}
