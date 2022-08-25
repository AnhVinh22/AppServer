package com.example.demo.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {
    @Size(max = 200, message = "Title's Character is less than 200")
    @NotBlank(message = "Title must not be blank")
    private String banner;

    @NotBlank(message = "Content must not be blank")
    private String title;
}
