package com.demo.empmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public Department() {}
    //
    // public Department(Long id, String name, List<Employee> employees) {
    //     this.id = id;
    //     this.name = name;
    //     this.employees = employees;
    // }
    //
    // public Long getId() { return id; }
    // public void setId(Long id) { this.id = id; }
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public List<Employee> getEmployees() { return employees; }
    // public void setEmployees(List<Employee> employees) { this.employees = employees; }
    //
    // public static DepartmentBuilder builder() { return new DepartmentBuilder(); }
    // public static class DepartmentBuilder { ... }
}
