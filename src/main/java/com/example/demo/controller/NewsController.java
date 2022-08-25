package com.example.demo.controller;

import com.example.demo.annotation.RoleAdmin;
import com.example.demo.domain.Comment;
import com.example.demo.domain.News;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.comment.CreateSubCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.response.CommentResponse;
import com.example.demo.response.NewSearchResponse;
import com.example.demo.response.NewsResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.JwtService;
import com.example.demo.service.NewsService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("demo/v1/news/")
@RequiredArgsConstructor
public class NewsController {


    private final NewsService newsService;

    private final JwtService jwtService;

    @RoleAdmin
    @PostMapping()
    public ResponseEntity<ResponseObject> createNews(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateNewsRequest request) {
        NewsResponse newsEntity = newsService.insert(token, request);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.value(),
                "Creat news success", newsEntity));
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> findAllNewsByHashTag(@RequestParam("hashTag") String hashTag,
                                                               @RequestParam(defaultValue = "0", required = false) int page,
                                                               @RequestParam(defaultValue = "24", required = false) int pageSize) {
        NewSearchResponse newsResponse = newsService.findByHashTag(hashTag, page, pageSize);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Creat news success", newsResponse));
    }

    @GetMapping("detail/{newsId}")
    public ResponseEntity<ResponseObject> getDetailNewsById(@PathVariable(name = "newsId") String newsId) {
        NewsResponse newsResponse = newsService.getNewsDetailById(newsId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value()
                , "Get detail news by id" + newsId, newsResponse));
    }

    @RoleAdmin
    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateNews(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "id") String id,
            @RequestBody UpdateNewsRequest request) {
        String updateBy = jwtService.parseTokenToUserId(token);
        JwtData data = jwtService.parseToken(token);
        data.getUserId();
        if (updateBy != null) {
            request.setUpdateUserId(updateBy);
        }

        NewsResponse news = newsService.save(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK.value()
                        , "Update successfully"
                        , news));
    }


    @DeleteMapping("{newsId}")
    public ResponseEntity<ResponseObject> deleteNews(@PathVariable(name = "newsId") String newsId
            , @RequestHeader("Authorization") String token) {
        newsService.deleteNewsById(newsId, token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK.value()
                        , "DELETE SUCCESSFULLY!", null));
    }

    @PostMapping("{newsId}/comments")
    public ResponseEntity<ResponseObject> addCommentToNews(@PathVariable(name = "newsId") String newsId,
                                                           @RequestHeader("Authorization") String token,
                                                           @RequestBody CreateCommentRequest request) {
        News newsEntity = newsService.addComment(newsId, token, request);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Comment To News Success", newsEntity));
    }

    @RoleAdmin
    @DeleteMapping("{commentId}/comments")
    public ResponseEntity<ResponseObject> deleteComment(@PathVariable(name = "commentId") String commentId,
                                                        @RequestHeader("Authorization") String token) {
        newsService.deleteComment(commentId, token);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Delete Comment Success", null));
    }

    @PostMapping("{commentId}/subComment")
    public ResponseEntity<ResponseObject> addSubCommentToComment(@RequestHeader("Authorization") String token,
                                                                 @PathVariable(name = "commentId") String commentId,
                                                                 @RequestBody CreateSubCommentRequest createSubCommentRequest) {
        Comment comment = newsService.addSubCommentToComment(token, commentId, createSubCommentRequest);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Add SubComment To Success", comment));
    }

    @RoleAdmin
    @DeleteMapping("{subCommentId}/subcomment")
    public ResponseEntity<ResponseObject> deleteSubCommentById(@RequestHeader("Authorization") String token,
                                                               @PathVariable(name = "subCommentId") String subCommentId) {
        newsService.deleteSubComment(token, subCommentId);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Delete SubComment Success", null));
    }

    @GetMapping("{newsId}/comments")
    public ResponseEntity<ResponseObject> getListCommentByNewsId(@PathVariable(name = "newsId") String newsId,
                                                                 @RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "24", required = false) int pageSize) {
        List<CommentResponse> commentResponse = newsService.getListComment(newsId, page, pageSize);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Get List Comment By News Id Success", commentResponse));
    }


}