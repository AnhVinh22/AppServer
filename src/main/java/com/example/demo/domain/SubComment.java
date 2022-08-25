package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubComment {
    @Id
    private String id;

    private String entityId;

    private String commentId;

    private String userId;

    private String content;

    private Long createdDate;
}
