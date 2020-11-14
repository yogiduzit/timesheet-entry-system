package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.access.EmployeeManagers;
import com.corejsf.model.employee.Employee;


@Named
@ApplicationScoped
public class EmployeeListForm implements Serializable {
    private static final long serialVersionUID = 124444L;
    
    @Inject private EmployeeManagers employeeManager;
    
    List<EditableEmployee> list;
    
    public List<EditableEmployee> getList() {
        if (list == null) {
            refreshList();
        }
        return list;
    }
    
    public void refreshList() {
        Employee[] employees = employeeManager.getAll();
        list = new ArrayList<EditableEmployee>();
        for (int i = 0; i < employees.length; i++) {
            list.add(new EditableEmployee(employees[i]));
        }
    }
    
    public void setList(List<EditableEmployee> es) {
        list = es;
    }
    
    public String deleteRow(EditableEmployee e) {
        employeeManager.remove(e.getEmployee());
        list.remove(e);
        return null;
    }
    
    public String save() {
        for (EditableEmployee e : list) {
            if (e.getEditable()) {
                employeeManager.merge(e.getEmployee());
                e.setEditable(false);
            }
        }
        return null;
    }
    
    
    
}
