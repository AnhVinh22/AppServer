package com.example.demo.service;

import com.example.demo.response.CommentResponse;

import java.util.List;


public interface CommentService {
    List<CommentResponse> getListSubComment(String commentId, int page, int pageSize);
}
