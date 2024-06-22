package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CommentDTO;
import com.dattran.ecommerceapp.dto.response.CommentResponse;
import com.dattran.ecommerceapp.entity.*;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.*;
import com.dattran.ecommerceapp.service.ICommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {
    CommentRepository commentRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    NotificationRepository notificationRepository;
    @Override
    public List<Comment> getAllCommentForProducts(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ResponseStatus.PRODUCT_NOT_FOUND);
        }
        return commentRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public Comment createComment(CommentDTO commentDTO) {
//        if (commentRepository.findByProductIdAndUserId(commentDTO.getProductId(), commentDTO.getUserId())) {
//            throw new AppException(ResponseStatus.COMMENT_ONLY_ONE);
//        }
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
        Order order = orderRepository.findById(commentDTO.getOrderId())
                .orElseThrow(()->new AppException(ResponseStatus.ORDER_NOT_FOUND));
        if (order.getIsCommented()) {
            throw new AppException(ResponseStatus.COMMENT_ONLY_ONE);
        }
        if (!Objects.equals(order.getUser().getId(), commentDTO.getUserId())) {
            throw new AppException(ResponseStatus.COMMENT_CREATED_FAILED);
        }
        Comment comment = Comment.builder()
                .user(user)
                .product(product)
                .content(commentDTO.getContent())
                .star(commentDTO.getStar())
                .build();
        return commentRepository.save(comment);
    }
    @Transactional
    @Override
    public List<Comment> createListComment(List<CommentDTO> commentDTOS) {
        List<Comment> comments = new ArrayList<>();
        for (CommentDTO commentDTO : commentDTOS) {
            Comment comment = createComment(commentDTO);
            comments.add(comment);
        }
        Order order = orderRepository.findById(commentDTOS.get(0).getOrderId())
                .orElseThrow(()->new AppException(ResponseStatus.ORDER_NOT_FOUND));
        order.setIsCommented(true);
        orderRepository.save(order);
        return comments;
    }

    @Override
    public List<CommentResponse> getAllCommentWithStarGreaterThan(int star) {
        List<Comment> comments = commentRepository.findByStarGreaterThan(star);
        return toCommentResponse(comments);
    }

    @Override
    public List<CommentResponse> getAllCommentByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        List<Comment> comments = commentRepository.findByUserIdAndIsDeleted(userId, false);
        return toCommentResponse(comments);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findByIsDeleted(false);
        return toCommentResponse(comments);
    }

    private List<CommentResponse> toCommentResponse(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .fullName(comment.getUser().getFullName())
                        .createdAt(comment.getCreatedAt().toString())
                        .updatedAt(comment.getUpdatedAt().toString())
                        .star(comment.getStar())
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
