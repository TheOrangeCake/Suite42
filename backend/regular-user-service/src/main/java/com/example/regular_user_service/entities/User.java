package com.example.regular_user_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="REGULAR_USER")
public class User {

	@Id
	@GeneratedValue
	Long id;

	@NotBlank(message="Username can not be blank")
	@Pattern(
			regexp = "^[A-Za-z0-9_]+( [A-Za-z0-9_]+)*$",
			message = "Username may only contain letters, numbers, spaces, and underscores. No leading or trailing whitespaces."
	)
	@Column(name = "username", unique = true, nullable = false)
	String username;

	@NotBlank(message="Email can not be blank")
	@Email(regexp = ".+@.+\\..+")
	@Column(name = "email", unique = true, nullable = false)
	String email;

	@NotBlank(message="Password can not be blank")
	@Column(name = "password")
	String password;

	@Column(name = "custom_avatar_url")
	String customAvatarUrl;

	@Column(name = "custom_banner_url")
	String customBannerUrl;

	@Column(name = "first_name")
	String firstName;

	@Column(name = "last_name")
	String lastName;
}