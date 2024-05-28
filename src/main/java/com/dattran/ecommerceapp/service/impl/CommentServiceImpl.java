package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.dto.CommentDTO;
import com.dattran.ecommerceapp.entity.Comment;
import com.dattran.ecommerceapp.entity.OrderDetail;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.CommentRepository;
import com.dattran.ecommerceapp.repository.OrderDetailRepository;
import com.dattran.ecommerceapp.repository.ProductRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
import com.dattran.ecommerceapp.service.ICommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {
    CommentRepository commentRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrderDetailRepository orderDetailRepository;
    @Override
    public List<Comment> getAllCommentForProducts(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ResponseStatus.PRODUCT_NOT_FOUND);
        }
        return commentRepository.findByProductId(productId);
    }

    @Override
    public Comment createComment(CommentDTO commentDTO) {
        if (commentRepository.findByProductIdAndUserId(commentDTO.getProductId(), commentDTO.getUserId())) {
            throw new AppException(ResponseStatus.COMMENT_ONLY_ONE);
        }
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(()->new AppException(ResponseStatus.USER_NOT_FOUND));
        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(()->new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
        OrderDetail orderDetail = orderDetailRepository.findByProductId(commentDTO.getProductId());
        if (!Objects.equals(orderDetail.getOrder().getUser().getId(), commentDTO.getUserId())) {
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
}
