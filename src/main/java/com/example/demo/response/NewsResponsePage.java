package com.example.demo.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponsePage {
    private List<NewsResponse> contents;

    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
