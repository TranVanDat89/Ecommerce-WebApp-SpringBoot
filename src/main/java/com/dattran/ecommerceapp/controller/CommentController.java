package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.CommentDTO;
import com.dattran.ecommerceapp.dto.response.CommentResponse;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Comment;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.service.ICommentService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    ICommentService commentService;
    SecurityUtil securityUtil;
    @GetMapping("")
    public HttpResponse getAllCommentsForProduct(@RequestParam String productId, HttpServletRequest httpServletRequest) {
        List<Comment> comments = commentService.getAllCommentForProducts(productId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getMessage())
                .data(Map.of("comments", comments))
                .build();
        return httpResponse;
    }

    @GetMapping("/all/star-greater-than-3")
    public HttpResponse getAllCommentsWithStarGreaterThan3(HttpServletRequest httpServletRequest) {
        List<CommentResponse> comments = commentService.getAllCommentWithStarGreaterThan(3);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getMessage())
                .data(Map.of("comments", comments))
                .build();
        return httpResponse;
    }
    @GetMapping("/all")
    public HttpResponse getAllComments(HttpServletRequest httpServletRequest) {
        List<CommentResponse> comments = commentService.getAllComments();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getMessage())
                .data(Map.of("comments", comments))
                .build();
        return httpResponse;
    }
    @GetMapping("/user/{userId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse getAllCommentsByUserId(@PathVariable String userId, HttpServletRequest httpServletRequest) {
        List<CommentResponse> comments = commentService.getAllCommentByUserId(userId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_COMMENTS_SUCCESSFULLY.getMessage())
                .data(Map.of("comments", comments))
                .build();
        return httpResponse;
    }

    @PostMapping("/create-list-comment")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    HttpResponse createComments(@RequestBody List<CommentDTO> commentDTOS, HttpServletRequest httpServletRequest) {
        List<Comment> comments = commentService.createListComment(commentDTOS);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.COMMENT_CREATED.getCode())
                .message(ResponseStatus.COMMENT_CREATED.getMessage())
                .build();
        return httpResponse;
    }
    @PutMapping("/update-comment/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public HttpResponse updateComment(@PathVariable String id, @RequestBody @Valid CommentDTO commentDTO, HttpServletRequest httpServletRequest) {
        Comment comment = commentService.updateComment(id, commentDTO);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.COMMENT_UPDATED.getCode())
                .message(ResponseStatus.COMMENT_UPDATED.getMessage())
                .build();
        return httpResponse;
    }

    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse deleteComment(@PathVariable String id, HttpServletRequest httpServletRequest) {
        commentService.deleteComment(id);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.COMMENT_DELETED.getCode())
                .message(ResponseStatus.COMMENT_DELETED.getMessage())
                .build();
        return httpResponse;
    }
}
