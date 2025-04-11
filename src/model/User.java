package model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract base class representing a user in the peer review system.
 * Implements the concept of abstraction by defining common attributes and behaviors.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String name;
    private String email;
    private String password;

    /**
     * Constructor for creating a new user with generated ID
     */
    public User(String name, String email, String password) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor for loading a user from storage with existing ID
     */
    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters (encapsulation)
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Password is protected with limited access (information hiding)
    protected String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verify if the provided password matches the user's password
     */
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Abstract method to get user role, to be implemented by subclasses
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return STR."User{userId='\{userId}', name='\{name}', email='\{email}', role='\{getRole()}'}";
    }
}