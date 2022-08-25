package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.News;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.comment.CreateSubCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.response.CommentResponse;
import com.example.demo.response.NewSearchResponse;
import com.example.demo.response.NewsResponse;

import java.util.List;

public interface NewsService {
    NewsResponse insert(String token, CreateNewsRequest request);

    NewsResponse save(String id, UpdateNewsRequest request);

    News findById(String id);

    NewSearchResponse findByHashTag(String hashTags, int page, int pageSize);

    NewsResponse getNewsDetailById(String newsId);

    void deleteNewsById(String id, String token);

    News addComment(String newsId, String token, CreateCommentRequest createCommentRequest);

    void deleteComment(String commentId, String token);

    Comment addSubCommentToComment(String token, String commentId, CreateSubCommentRequest createSubCommentRequest);

    void deleteSubComment(String token, String subCommentId);

    List<CommentResponse> getListComment(String newsId, int page, int pageSize);
}

