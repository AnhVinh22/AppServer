package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventIncomingResponse {
    private String title;
    private String banner;
    private String timeBegin;
    private String timeEnd;
}
