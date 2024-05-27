package com.dattran.ecommerceapp;

import com.dattran.ecommerceapp.entity.Category;
import com.dattran.ecommerceapp.entity.Flavor;
import com.dattran.ecommerceapp.entity.Role;
import com.dattran.ecommerceapp.entity.User;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.repository.CategoryRepository;
import com.dattran.ecommerceapp.repository.FlavorRepository;
import com.dattran.ecommerceapp.repository.RoleRepository;
import com.dattran.ecommerceapp.repository.UserRepository;
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
	public CommandLineRunner runner(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, FlavorRepository flavorRepository, CategoryRepository categoryRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty() && roleRepository.findByName("ADMIN").isEmpty()) {
				List<Role> roles = List.of(Role.builder().name("USER").build(), Role.builder().name("ADMIN").build());
				roleRepository.saveAll(roles);
			}
			if (!userRepository.existsByFullName("ADMIN")) {
				Role role = roleRepository.findByName("ADMIN").orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
				User user = User.builder().fullName("Admin").phoneNumber("0987822222").password(passwordEncoder.encode("@Admin123")).active(true).role(role).build();
				userRepository.save(user);
			}
			List<Category> categories = List.of(
					Category.builder().name("Whey protein, Vegan protein").build(),
					Category.builder().name("Sữa mass tăng cân").build(),
					Category.builder().name("BCAA, EAA phục hồi cơ").build(),
					Category.builder().name("Pre-workout, Creatine").build(),
					Category.builder().name("Phụ kiện tập luyện").build(),
					Category.builder().name("Hỗ trợ giảm cân, yến mạch").build(),
					Category.builder().name("Thực phẩm chức năng khác").build()
			);
			for (Category category : categories) {
				if (!categoryRepository.existsByName(category.getName())) {
					categoryRepository.save(category);
				}
			}
		};
	}
}
