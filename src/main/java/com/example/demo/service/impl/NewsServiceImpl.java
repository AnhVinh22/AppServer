package com.example.demo.service.impl;

import com.example.demo.converter.DateConvert;
import com.example.demo.converter.NewsConverter;
import com.example.demo.converter.SubCommentConverter;
import com.example.demo.domain.Comment;
import com.example.demo.domain.News;
import com.example.demo.domain.SubComment;
import com.example.demo.domain.User;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.EventNotFoundException;
import com.example.demo.exception.NewsNotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.SubCommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.comment.CreateSubCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.response.CommentResponse;
import com.example.demo.response.NewSearchResponse;
import com.example.demo.response.NewsResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final MongoTemplate mongoTemplate;
    private final JwtService jwtService;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final UserRepository userRepository;

    @Override
    public NewsResponse insert(String token, CreateNewsRequest request) {
        String userId = jwtService.parseTokenToUserId(token);
        if (userId != null) {
            request.setCreateUserId(userId);
        }
        News news = newsRepository.insert(NewsConverter.convertNewsRequestToNews(userId, request));
        return NewsConverter.convertNewsToNewsResponse(news, 0);
    }

    @Override
    public NewsResponse save(String id, UpdateNewsRequest request) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new NewsNotFoundException("Can't find news with id = " + request.getId());
        }
        News updateEntity = news.get();
        NewsConverter.convertNewsRequestToNews1(request, updateEntity);
        News news1 = newsRepository.save(updateEntity);
        return NewsConverter.convertNewsToNewsResponse(news1, 0);
    }

    @Override
    public News findById(String id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new NewsNotFoundException("Can not find news with id = " + id);
        }
        return news.get();
    }

    @Override
    public NewSearchResponse findByHashTag(String hashTag, int page, int pageSize) {
        List<NewsResponse> newsResponses = newsRepository.findByHashTagLike(hashTag)
                .stream().skip(pageSize * page)
                .limit(pageSize)
                .map(item -> {
                    int sum = 0;
                    List<Comment> comments = item.getComments();
                    for (int i = 0; i < comments.size(); i++) {
                        sum += comments.get(i).getSubComment().size();
                    }
                    sum += comments.size();
                    return NewsResponse.builder()
                            .banner(item.getBanner())
                            .title(item.getTitle())
                            .content(item.getContent())
                            .hashTag(item.getHashTag())
                            .lastUpdatedDate(DateConvert.convertLongToDate(item.getLastUpdatedDate()))
                            .commentNumber(sum)
                            .build();
                }).collect(Collectors.toList());
        return new NewSearchResponse(page, pageSize, newsResponses, hashTag);
    }

    @Override
    public NewsResponse getNewsDetailById(String newsId) {
        News news = newsRepository.findById(newsId).get();
        if (news == null) {
            throw new NewsNotFoundException("News Id does not exist");
        }
        Integer commentNumber = 0;
        List<Comment> comments = news.getComments();
        for (int i = 0; i < comments.size(); i++) {
            Query query = new Query();
            query.addCriteria(Criteria.where("entityId").is(newsId));
            List<SubComment> countSubNumber = mongoTemplate.find(query, SubComment.class);
            commentNumber += countSubNumber.size();
            commentNumber += 1;
        }
        NewsResponse response = NewsResponse.builder()
                .banner(news.getBanner())
                .title(news.getTitle())
                .content(news.getContent())
                .lastUpdatedDate(DateConvert.convertLongToDate(news.getLastUpdatedDate()))
                .commentNumber(commentNumber)
                .build();
        return response;
    }

    @Override
    public void deleteNewsById(String id, String token) {
        String createUserIdCheck = jwtService.parseTokenToUserId(token);
        News news = newsRepository.findById(id).get();
        if (!createUserIdCheck.equals(news.getCreateUserId())) {
            throw new NewsNotFoundException("NewsId does not exist");
        }
        commentRepository.deleteAllByEntityId(id);
        subCommentRepository.deleteAllByEntityId(id);
        newsRepository.deleteById(id);
    }

    @Override
    public News addComment(String newsId, String token, CreateCommentRequest createCommentRequest) {
        News news = newsRepository.findById(newsId).get();
        if (news == null) {
            throw new NewsNotFoundException("NewsId does not exist");
        }
        String userId = jwtService.parseTokenToUserId(token);
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setEntityId(newsId);
        comment.setContent(createCommentRequest.getContent());
        comment.setCreatedDate(System.currentTimeMillis());
        commentRepository.save(comment);
        news.getComments().add(comment);
        return newsRepository.save(news);
    }

    @Override
    public void deleteComment(String commentId, String token) {
        Comment comment = commentRepository.findById(commentId).get();
        if (comment == null) {
            throw new EventNotFoundException("NewsId does not exit");
        }
        String userId = jwtService.parseTokenToUserId(token);
        if ((!userId.equals(comment.getUserId()))) {
            throw new EventNotFoundException("Unauthorized to delete comment");
        }
        for (SubComment c : comment.getSubComment()) {
            subCommentRepository.deleteById(c.getId());
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment addSubCommentToComment(String token, String commentId, CreateSubCommentRequest
            createSubCommentRequest) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new NewsNotFoundException("");
        }
        Comment comment1 = comment.get();
        String userId = jwtService.parseTokenToUserId(token);
        SubComment subComment = SubCommentConverter.convertSubCommentRequestToSubComment(createSubCommentRequest,
                comment1.getEntityId(), userId, commentId);
        subCommentRepository.save(subComment);

        comment1.getSubComment().add(subComment);
        Optional<News> news = newsRepository.findById(comment1.getEntityId());
        if (news.isPresent()) {
            List<Comment> comments = news.get().getComments();
            for (Comment c : comments) {
                if (c.getId().equals(comment1.getId())) {
                    c.getSubComment().add(subComment);
                }
            }
            newsRepository.save(news.get());
        }
        return commentRepository.save(comment1);
    }

    @Override
    public void deleteSubComment(String token, String subCommentId) {
        SubComment subComment = subCommentRepository.findById(subCommentId).get();
        if (subComment == null) {
            throw new NewsNotFoundException("SubCommentId does not exist");
        }
        Comment comment = commentRepository.findById(subComment.getCommentId()).get();
        String userId = comment.getUserId();

        String userId1 = jwtService.parseTokenToUserId(token);
        String commentId = subComment.getCommentId();
        if (!(subComment.getUserId().equals(userId1)) || !(userId.equals(commentId))) {
            throw new NewsNotFoundException("Unauthorized to delete Subcomment");

        }
        subCommentRepository.delete(subComment);
    }

    @Override
    public List<CommentResponse> getListComment(String newsId, int page, int pageSize) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (!optionalNews.isPresent()) {
            throw new CommentNotFoundException("News id does not exist");
        }

        News news = optionalNews.get();
        List<CommentResponse> result = news
                .getComments()
                .stream()
                .map(comment -> {
                    User user = userRepository.findById(comment.getUserId()).get();
                    return CommentResponse.builder()
                            .avatar(user.getAvatar())
                            .userName(user.getName())
                            .content(comment.getContent())
                            .createdDate(comment.getCreatedDate())
                            .subCommentNumber(comment.getSubComment().size())
                            .build();
                })
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        return result;
    }
}