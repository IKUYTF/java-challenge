package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Entity of employee
 */
@Entity
@Table(name="EMPLOYEE")
public class Employee {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_NAME")
    @NotBlank(message = "cannot be blank")
    private String name;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    @Min(value = 0, message = "must be larger than 0")
    @NotNull(message = "cannot be null")
    private Integer salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    @NotBlank(message = "cannot be blank")
    private String department;

}
