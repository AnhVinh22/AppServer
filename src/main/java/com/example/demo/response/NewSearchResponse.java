package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewSearchResponse {
    private int page;
    private int pageSize;
    private List<NewsResponse> news;
    private String hashTag;
}
