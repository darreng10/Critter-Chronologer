package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.getOne(id);
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee;
        Optional<Employee> byId = employeeRepository.findById(employeeId);
        if(byId.isPresent()){
            employee = byId.get();
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        } else {
            throw new NoSuchElementException("Employee with id:" + employeeId + " can not be found");
        }
    }

    public List<Employee> getAvailableEmployees(LocalDate date, Set<EmployeeSkill> employeeSkills) {
        List<Employee> employees = new ArrayList<>();
        List<Employee> matchedEmployees = new ArrayList<>();
        employees = employeeRepository.findAllByDaysAvailableContaining(date.getDayOfWeek());

        for (Employee employee : employees) {
            if (employee.getSkills().containsAll(employeeSkills)) {
                matchedEmployees.add(employee);
            }
        }
        return matchedEmployees;
    }
}
