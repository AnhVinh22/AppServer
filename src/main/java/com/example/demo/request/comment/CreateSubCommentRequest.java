package com.example.demo.request.comment;

import lombok.Data;

@Data
public class CreateSubCommentRequest {

    private String userId;

    private String entityId;

    private String content;
}
