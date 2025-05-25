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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class EcommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, ArticleCategoryRepository articleCategoryRepository, CategoryRepository categoryRepository) {
		return args -> {
			List<ArticleCategory> articleCategories = List.of(
					ArticleCategory.builder().name("Dinh dưỡng thể hình").build(),
					ArticleCategory.builder().name("Giới thiệu sản phẩm").build(),
					ArticleCategory.builder().name("Y học và đời sống").build(),
					ArticleCategory.builder().name("Chương trình tập luyện").build()
			);
			articleCategoryRepository.saveAll(articleCategories);
			if (roleRepository.findByName("USER").isEmpty() && roleRepository.findByName("ADMIN").isEmpty()) {
				List<Role> roles = List.of(Role.builder().name("USER").build(), Role.builder().name("ADMIN").build());
				roleRepository.saveAll(roles);
			}
			if (roleRepository.findByName("DELIVERY").isEmpty()) {
				Role role = Role.builder().name("DELIVERY").build();
				roleRepository.save(role);
			}
//			Role role = roleRepository.findByName("DELIVERY").orElseThrow(() -> new AppException(ResponseStatus.ROLE_NOT_FOUND));
//			User user = User.builder()
//					.fullName("Nguyễn Huy Hùng")
//					.phoneNumber("0867655555")
//					.active(true)
//					.role(role)
//					.address("Bình Dương, Việt Nam")
//					.dateOfBirth(LocalDate.from(LocalDateTime.now().minusYears(25)))
//					.password(passwordEncoder.encode("@Huyhung1208"))
//					.build();
//			userRepository.save(user);
//			if (!userRepository.existsByFullName("ADMIN")) {
//				Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
//				User adminUser = User.builder().fullName("Admin").phoneNumber("0987822222").password(passwordEncoder.encode("@Admin123")).active(true).role(adminRole).build();
//				userRepository.save(adminUser);
//			}
			List<Category> categories = List.of(
					Category.builder().name("Whey protein, Vegan protein").isDeleted(false).build(),
					Category.builder().name("Sữa mass tăng cân").isDeleted(false).build(),
					Category.builder().name("BCAA, EAA phục hồi cơ").isDeleted(false).build(),
					Category.builder().name("Pre-workout, Creatine").isDeleted(false).build(),
					Category.builder().name("Hỗ trợ giảm cân, yến mạch").isDeleted(false).build(),
					Category.builder().name("Thực phẩm chức năng khác").isDeleted(false).build()
			);
			for (Category category : categories) {
				if (!categoryRepository.existsByName(category.getName())) {
					categoryRepository.save(category);
				}
			}
		};
	}
}
