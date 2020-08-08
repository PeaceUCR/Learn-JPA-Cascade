package com.example.demo.service;

import com.example.demo.domain.Address;
import com.example.demo.domain.Email;
import com.example.demo.domain.Employee;
import com.example.demo.repository.EmailRepository;
import com.example.demo.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmailRepository emailRepository;

  // test cascade persist & remove
  public void save() {
    System.out.println("Before SAVE");
    print();

    Employee employee = new Employee();
    employee.setName("e1");
    Address address = new Address();
    address.setCity("Wuhan");
    employee.setAddress(address);
    employeeRepository.save(employee);

    System.out.println("After SAVE");
    print();

    System.out.println("Before DELETE");
    print();
    employeeRepository.delete(employee);
    System.out.println("After DELETE");
    print();
  }

  // ONLY REMOVE ONE EMAIL BUT KEEP EMPLOYEE
  // remove one item (side of @ManyToOne)
  public void save2() {
    Employee employee = new Employee();
    employee.setName("e1");
    Email email = new Email();
    email.setEmail("email1");
    email.setEmployee(employee);// if no this statement, then the email saved employeeid is null
    Email email2 = new Email();
    email2.setEmail("email2");
    email2.setEmployee(employee);// if no this statement, then the email saved employeeid is null
    List<Email> emails = new ArrayList<>();
    emails.add(email);
    emails.add(email2);
    employee.setEmails(emails);
    employeeRepository.save(employee);

    System.out.println("After SAVE");
    print();

    //can't delete, it's still on employee
    Email toDelete = emailRepository.findById(email.getId()).orElse(null);
    emailRepository.delete(toDelete);
    System.out.println("After First DELETE");
    print();

    //correct way, we must delete the relation first, then delete the entity
    Email toDelete2 = emailRepository.findById(email.getId()).orElse(null);
    toDelete2.getEmployee().getEmails().removeIf(e -> toDelete2.getId().equals(e.getId()));
    toDelete2.setEmployee(null);//if only this, then it's just update sql, to set employeeid -> null
    emailRepository.delete(toDelete);
    System.out.println("After Second DELETE");
    print();

  }

  //orphan remove
  //save remove target but we will remove from user side
  public void save3() {
    Employee employee = new Employee();
    employee.setName("e1");
    Email email = new Email();
    email.setEmail("email1");
    email.setEmployee(employee);// if no this statement, then the email saved employeeid is null
    Email email2 = new Email();
    email2.setEmail("email2");
    email2.setEmployee(employee);// if no this statement, then the email saved employeeid is null
    List<Email> emails = new ArrayList<>();
    emails.add(email);
    emails.add(email2);
    employee.setEmails(emails);
    Employee saved = employeeRepository.save(employee);

    System.out.println("After SAVE");
    print();

    List<Email> emailList = saved.getEmails();
    emailList.removeIf(e -> "email1".equals(e.getEmail()));
    // if we need to remove email by save user(remove in email table),
    // we must add "orphanRemoval = true" at @OneToMany relation of Employee
    employeeRepository.save(saved);

    System.out.println("After Remove Email Then Save");
    // not correct, I don't know why?
    print();

  }

  private void print() {
    List<Employee> employees =  employeeRepository.findAll();
    employees.forEach(employee -> System.out.println(employee));
  }

}
