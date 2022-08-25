package com.example.demo.converter;

import com.example.demo.domain.Category;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.response.CategoryResponse;
import com.example.demo.response.RequestResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CategoryConverter {

    private static ModelMapper modelMapper;
    @Autowired
    private ModelMapper modelMapperObj;

    @PostConstruct
    private  void postConstruct() {
        modelMapper = this.modelMapperObj;
    }

    public static Category convertBoxRequestToBox(String userId, CreateCategoryRequest boxRequest) {
        Long boxTime = System.currentTimeMillis();
        Category category = new Category();
        category.setCreateUserId(userId);
        category.setBanner(boxRequest.getBanner());
        category.setTitle(boxRequest.getTitle());
        category.setCreateTime(boxTime);
        category.setLastUpdateTime(boxTime);
        return category;
    }

    public static Category convertUpdateCategoryRequestToCategory(String userId, UpdateCategoryRequest boxRequest, Category category) {
        category.setBanner(boxRequest.getBanner());
        category.setTitle(boxRequest.getTitle());
        category.setUpdateUserId(userId);
        category.setLastUpdateTime(System.currentTimeMillis());
        return category;
    }

    public static CategoryResponse convertCategoryToCategoryResponse(Category category) {
//        CategoryResponse boxResponse = new CategoryResponse();
//        boxResponse.setId(category.getId());
//        boxResponse.setBanner(category.getBanner());
//        boxResponse.setTitle(category.getTitle());
//        return boxResponse;
        return modelMapper.map(category, CategoryResponse.class);
    }

}
