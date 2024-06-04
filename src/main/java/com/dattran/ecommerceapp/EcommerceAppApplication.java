package com.dattran.ecommerceapp;

import com.dattran.ecommerceapp.entity.ArticleCategory;
import com.dattran.ecommerceapp.entity.Category;
import com.dattran.ecommerceapp.entity.Role;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class EcommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, ArticleCategoryRepository articleCategoryRepository, CategoryRepository categoryRepository) {
		return args -> {
//			List<ArticleCategory> articleCategories = List.of(
//					ArticleCategory.builder().name("Dinh dưỡng thể hình").build(),
//					ArticleCategory.builder().name("Giới thiệu sản phẩm").build(),
//					ArticleCategory.builder().name("Y học và đời sống").build(),
//					ArticleCategory.builder().name("Chương trình tập luyện").build()
//			);
//			articleCategoryRepository.saveAll(articleCategories);
			if (roleRepository.findByName("USER").isEmpty() && roleRepository.findByName("ADMIN").isEmpty()) {
				List<Role> roles = List.of(Role.builder().name("USER").build(), Role.builder().name("ADMIN").build());
				roleRepository.saveAll(roles);
			}
			if (!userRepository.existsByFullName("ADMIN")) {
				Role role = roleRepository.findByName("ADMIN").orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
				User user = User.builder().fullName("Admin").phoneNumber("0987822222").password(passwordEncoder.encode("@Admin123")).active(true).role(role).build();
				userRepository.save(user);
			}
//			List<Category> categories = List.of(
//					Category.builder().name("Whey protein, Vegan protein").build(),
//					Category.builder().name("Sữa mass tăng cân").build(),
//					Category.builder().name("BCAA, EAA phục hồi cơ").build(),
//					Category.builder().name("Pre-workout, Creatine").build(),
//					Category.builder().name("Hỗ trợ giảm cân, yến mạch").build(),
//					Category.builder().name("Thực phẩm chức năng khác").build()
//			);
//			for (Category category : categories) {
//				if (!categoryRepository.existsByName(category.getName())) {
//					categoryRepository.save(category);
//				}
//			}
		};
	}
}
