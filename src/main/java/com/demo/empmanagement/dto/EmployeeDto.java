package com.demo.empmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String name;
    private String email;
    private Long departmentId;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public EmployeeDto() {}
    //
    // public EmployeeDto(String name, String email, Long departmentId) {
    //     this.name = name;
    //     this.email = email;
    //     this.departmentId = departmentId;
    // }
    //
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public String getEmail() { return email; }
    // public void setEmail(String email) { this.email = email; }
    // public Long getDepartmentId() { return departmentId; }
    // public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
}
