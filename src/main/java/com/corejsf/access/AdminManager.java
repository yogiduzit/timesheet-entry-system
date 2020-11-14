package com.corejsf.access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.data.Admins;
import com.corejsf.data.Employees;
import com.corejsf.model.employee.Credentials;
import com.corejsf.model.employee.Employee;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.corejsf.model.employee.Admin;

@Named("adminManager")
@ConversationScoped
public class AdminManager implements Serializable {
    private static final long serialVersionUID =1233413L;
    @Resource(mappedName = "java:jobss/datasources/MySQLDS")
    private DataSource dataSource;
    
    @Inject EmployeeManagers employeeManager;
    
    
    public Employee find(int num) {
        
    }

}
