package com.org.java.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EmployeeDto {
	
	private Long empid;
	@NotBlank(message = "Name is mandatory")
	private String name;
	@Min(value = 18, message = "Age must be at least 18")
	private int age;
	@Positive(message = "Salary must be positive")
	private double salary;
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;
	private String workLocation;
	private String platform;
	private String projectName;
	private Long addharNumber;
	private String panNumber;
	private Long mobbileNumber;


}
