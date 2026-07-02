package com.demo.empmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public Employee() {}
    //
    // public Employee(Long id, String name, String email, Department department) {
    //     this.id = id;
    //     this.name = name;
    //     this.email = email;
    //     this.department = department;
    // }
    //
    // public Long getId() { return id; }
    // public void setId(Long id) { this.id = id; }
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public String getEmail() { return email; }
    // public void setEmail(String email) { this.email = email; }
    // public Department getDepartment() { return department; }
    // public void setDepartment(Department department) { this.department = department; }
    //
    // public static EmployeeBuilder builder() { return new EmployeeBuilder(); }
    // public static class EmployeeBuilder { ... }
}
