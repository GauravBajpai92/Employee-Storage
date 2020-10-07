package com.example.employee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class EmployeeDto implements Serializable {
    @Null
    private UUID empId;
    @NotBlank
    @NotEmpty
    private String empName;

    @Min(0)
    @Digits(fraction = 2,integer = 7)
    private double salary;
    @Min(18)
    @Max(60)
    private int age;



}
