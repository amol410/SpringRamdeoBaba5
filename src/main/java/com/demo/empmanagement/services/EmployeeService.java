package com.demo.empmanagement.services;

import com.demo.empmanagement.dto.EmployeeDto;
import com.demo.empmanagement.exceptions.ResourceNotFoundException;
import com.demo.empmanagement.models.Department;
import com.demo.empmanagement.models.Employee;
import com.demo.empmanagement.repository.DepartmentRepository;
import com.demo.empmanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // =====================================================================
    // @RequiredArgsConstructor replaces this constructor (commented out):
    // =====================================================================
    //
    // public EmployeeService(EmployeeRepository employeeRepository,
    //                        DepartmentRepository departmentRepository) {
    //     this.employeeRepository = employeeRepository;
    //     this.departmentRepository = departmentRepository;
    // }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeDto employeeDto) {
        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));

        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .email(employeeDto.getEmail())
                .department(department)
                .build();

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));

        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }
}
