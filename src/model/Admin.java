package model;

import java.io.Serial;

/**
 * Admin class that extends the User class.
 * Demonstrates inheritance and specialization.
 */
public class Admin extends User {
    @Serial
    private static final long serialVersionUID = 1L;
    private boolean isAdmin;

    private String adminLevel;  // e.g., "System Admin", "Department Admin"

    /**
     * Constructor for creating a new Admin
     */
    public Admin(String name, String email, String password, String adminLevel) {
        super(name, email, password);
        this.adminLevel = adminLevel;
        this.isAdmin = true;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String toString() {
        return STR."Admin{userId='\{getUserId()}', name='\{getName()}', email='\{getEmail()}', adminLevel='\{adminLevel}'}";
    }
}