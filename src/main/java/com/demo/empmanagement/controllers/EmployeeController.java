package com.demo.empmanagement.controllers;

import com.demo.empmanagement.models.Employee;
import com.demo.empmanagement.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // @RequiredArgsConstructor replaces:
    // public EmployeeController(EmployeeService employeeService) { this.employeeService = employeeService; }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}
