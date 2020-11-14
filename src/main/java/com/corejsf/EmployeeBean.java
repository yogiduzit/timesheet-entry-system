package com.corejsf;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import com.corejsf.model.employee.Employee;
import com.corejsf.access.EmployeeManagers;

@Named
@RequestScoped
public class EmployeeBean extends Employee {
    @Inject private EmployeeManagers em;
    @Inject private EmployeeController ec;
    
    public String addEmployee() {
        ec.getList().add(new EditableEmployee(this));
        em.persist(this);
        return "adminEmployeeList";
    }

}
