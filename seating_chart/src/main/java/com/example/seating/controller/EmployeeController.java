package com.example.seating.controller;

import com.example.seating.entity.Employee;
import com.example.seating.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:9000")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/updateSeat/{empId}/{seat}")
    public void updateSeat(@PathVariable String empId, @PathVariable Long seat) {
        employeeService.updateSeat(empId, seat);
    }


}

