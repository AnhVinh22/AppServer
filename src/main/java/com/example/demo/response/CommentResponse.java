package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private String avatar;
    private String userName;
    private String content;
    private Long createdDate;
    private int commentNumber;
    private int subCommentNumber;
    private String replyComment;
}
