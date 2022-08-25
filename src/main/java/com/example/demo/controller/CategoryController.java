package com.example.demo.controller;

import com.example.demo.annotation.RoleAdmin;
import com.example.demo.domain.Category;
import com.example.demo.request.category.CreateCategoryRequest;
import com.example.demo.request.category.UpdateCategoryRequest;
import com.example.demo.response.CategoryResponse;
import com.example.demo.response.ListCategoryResponse;
import com.example.demo.response.ListQuestionResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/category/")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> creatCategory(@RequestHeader("Authorization") String token,
                                                        @Valid @RequestBody CreateCategoryRequest boxRequest) {
        CategoryResponse boxResponse = categoryService.insertCategory(token, boxRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "CREATE CATEGORY SUCCESS", boxResponse));
    }

    @RoleAdmin
    @PutMapping("{boxId}")
    public ResponseEntity<ResponseObject> updateCategory(@RequestHeader("Authorization") String token,
                                                         @Valid @RequestBody UpdateCategoryRequest boxRequest,
                                                         @PathVariable(name = "boxId") String categoryId) {
        Category category = categoryService.save(token, boxRequest, categoryId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "UPDATE CATEGORY SUCCESS", category));
    }

    @RoleAdmin
    @DeleteMapping("{categoryId}")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestHeader("Authorization") String token,
                                                         @PathVariable(name = "categoryId") String categoryId)  {
        categoryService.deleteCategoryById(token, categoryId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "DELETE CATEGORY BY ID SUCCESS", null));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getListCategory(@RequestParam(defaultValue = "0", required = false) int page,
                                                          @RequestParam(defaultValue = "24", required = false) int pageSize) {
        ListCategoryResponse listCategoryResponse = categoryService.getListCategory(page, pageSize);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "GET LIST CATEGORY SUCCESS", listCategoryResponse));
    }

    @GetMapping("{categoryId}/questions")
    public ResponseEntity<ResponseObject> getListQuestionByCategoryId(@PathVariable("categoryId") String categoryId) {
        List<ListQuestionResponse> questionResponseList = categoryService.findAllQuestionByCategoryId(categoryId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "GET LIST QUESTION BY CATEGORYID SUCCESS", questionResponseList));
    }
}
