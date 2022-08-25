package com.example.demo.service.impl;

import com.example.demo.converter.DateConvert;
import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.CommentResponse;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CommentResponse> getListSubComment(String commentId, int page, int pageSize) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()) {
            throw new CommentNotFoundException("Can not find comment id " + commentId);
        }
        Comment comment = optionalComment.get();
        List<CommentResponse> responses = comment.getSubComment()
                .stream()
                .map(subComment -> {
                    User user = userRepository.findById(subComment.getUserId()).get();
                    return CommentResponse.builder()
                            .avatar(user.getAvatar())
                            .avatar(user.getName())
                            .content(subComment.getContent())
                            .createdDate(subComment.getCreatedDate())
                            .replyComment(DateConvert.convertReplyCommentToString(subComment.getCreatedDate()))
                            .build();
                })
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return responses;
    }
}

