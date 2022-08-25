package com.example.demo.controller;


import com.example.demo.response.CommentResponse;
import com.example.demo.response.ResponseObject;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("demo/v1/comments/")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("{commentId}")
    public ResponseEntity<ResponseObject> getListSubCommentByCommentId(@PathVariable(name = "commentId") String commentId,
                                                                       @RequestParam(defaultValue = "0", required = false) int page,
                                                                       @RequestParam(defaultValue = "24", required = false) int pageSize) {
        List<CommentResponse> listSubComment = commentService.getListSubComment(commentId, page, pageSize);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.value(),
                "Get List SubComment By Comment Id Success", listSubComment));
    }
}
