package com.example.demo.controller;

import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PostMapping("/save")
  public void save() {
    employeeService.save();
  }

  @GetMapping("/save")
  public void save2() {
    employeeService.save2();
  }

  @PutMapping("/save")
  public void save3() {
    employeeService.save3();
  }

}
