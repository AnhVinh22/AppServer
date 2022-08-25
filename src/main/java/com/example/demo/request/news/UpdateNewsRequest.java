package com.example.demo.request.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNewsRequest {

    private String id;

    @Size(max = 200, message = "Title's Characters is less than 200")
    @NotNull(message = "Title must not be null")
    @NotBlank(message = "Title must not be null")
    @NotEmpty(message = "Title must not be null")
    private String title;

    private String content;

    private String hashTags;
    //TODO add update
    private String updateUserId;

    private long lastUpdateTime;
}
