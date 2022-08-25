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
public class EventResponse {
    private String id;
    private String banner;
    private String title;
    private String content;
    private StatusEvent statusEvent;
    private String timeBegin;
    private String timeEnd;
}
