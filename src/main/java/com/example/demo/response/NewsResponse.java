package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private String title;
    private String content;
    private String banner;
    private String hashTag;
    private String lastUpdatedDate;
    private int commentNumber;
}
