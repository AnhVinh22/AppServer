package com.example.demo.converter;

import com.example.demo.domain.SubComment;
import com.example.demo.request.comment.CreateSubCommentRequest;

public class SubCommentConverter {
    public static SubComment convertSubCommentRequestToSubComment(CreateSubCommentRequest request, String entityId, String userId, String commentid) {
        return SubComment.builder()
                .userId(userId)
                .entityId(entityId)
                .content(request.getContent())
                .commentId(commentid)
                .createdDate(System.currentTimeMillis())
                .build();
    }
}
