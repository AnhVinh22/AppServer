package com.example.demo.response;

import com.example.demo.constant.StatusEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSearchResponse {
    private String id;
    private String title;
    private String timeBegin;
    private StatusEvent statusEvent;
}
