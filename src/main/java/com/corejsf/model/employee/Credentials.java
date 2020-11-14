package com.corejsf.model.employee;


/**
 * This class represents credentials for employees
 * and admin to log in to
 * 
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */
public class Credentials {
    
    /**
     * Represents the username of the login phase
     */
    private String username;
    /**
     * Represents the passowrd of the login phase
     */
    private String password;
    /**
     * Represents the employee number of the Employee
     */
    private int empNumber;
    
    public Credentials() {
    	
    }

    public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Setter for username
     * @param username
     */
    public void setUsername(final String id) {
        username = id;
    }
    
    /**
     * Getter for password
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Setter for password
     * @param password
     */
    public void setPassword(final String pw) {
        password = pw;
    }
    
    /**
     * Getters for the empNumber
     * @return empNumber
     */
    public int getEmpNumber() {
        return empNumber;
    }
    
    /**
     * Setters for the empNumber
     * @param empNumber
     */
    public void setEmpNumber(int empNumber) {
        this.empNumber = empNumber;
    }

	@Override
	/**
	 * Creates hash code for a credentials object
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + empNumber;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	/**
	 * Checks if two credentials objects are equal
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		if (empNumber != other.empNumber)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}   
}
