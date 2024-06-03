package com.dattran.ecommerceapp.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleDTO {
    String title;
    MultipartFile imageFile;
    String content;
    String articleCategoryId;
}
