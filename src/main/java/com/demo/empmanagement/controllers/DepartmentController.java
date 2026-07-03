package com.demo.empmanagement.controllers;

import com.demo.empmanagement.models.Department;
import com.demo.empmanagement.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }
}
