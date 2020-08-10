# Learn-JPA-Cascade

how to use: startup then goto to http://localhost:8080/console/login.do?jsessionid=f53cdd4eff580a1922df88097fe36893 user:sa, password:123456 to check h2 database data & validate the reuslt

three controller map to three senario with cascade persist & cascade remove, remove at @ManyToOne side, orphanRemoval = true

CascadeType https://www.jianshu.com/p/e8caafce5445

# [Save()](https://github.com/PeaceUCR/Learn-JPA-Cascade/blob/master/src/main/java/com/example/demo/service/EmployeeService.java#L23)
CascadeType.PERSIST : 级联保存/持久化，例如现在有以下两个实体(实体代码和报存代码如下)，只保存employee的同时也保存address到各自对应的表里面去， 
如果没有CascadeType.PERSIST， 则保存时会报错 
org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing :

https://github.com/PeaceUCR/Learn-JPA-Cascade/blob/master/src/main/java/com/example/demo/service/EmployeeService.java#L23

https://github.com/PeaceUCR/Learn-JPA-Cascade/blob/master/src/main/java/com/example/demo/domain/Employee.java#L36

CascadeType.REMOVE : 如果没有这个，以下删除employee的操作只删除employee表的数据，与之关联的address表中的数据会被保留
如果加上了，删除只employee的操作也会同时删除关联的address.


# [Save2()](https://github.com/PeaceUCR/Learn-JPA-Cascade/blob/master/src/main/java/com/example/demo/service/EmployeeService.java#L46)
注意维护关联的实体一定要把被维护的加进去，比如employee.setAddress(address);否则存的email是没有 employeeid的， 必须要email.setEmployee(employee);

```
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
   
```

并且通过email/ manytoone这边删除的时候，一定要先把依赖设置成null, 不然删不掉
```
    //correct way, we must delete the relation first, then delete the entity
    Email toDelete2 = emailRepository.findById(email.getId()).orElse(null);
    toDelete2.getEmployee().getEmails().removeIf(e -> toDelete2.getId().equals(e.getId()));
    toDelete2.setEmployee(null);//if only this, then it's just update sql, to set employeeid -> null
    emailRepository.delete(toDelete);
```

# [Save3()](https://github.com/PeaceUCR/Learn-JPA-Cascade/blob/master/src/main/java/com/example/demo/service/EmployeeService.java#L82)

orphanRemoval = true
如果删除关系，是否删除关系上的实体

https://blog.csdn.net/liyiming2017/article/details/90613707

https://www.oschina.net/question/925076_157346

https://stackoverflow.com/questions/18813341/what-is-the-difference-between-cascadetype-remove-and-orphanremoval-in-jpa

例子是通过删除employee email list里面的一个email，然后保存就可以删除email table里面相应的数据。
