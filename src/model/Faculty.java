package model;

import java.io.Serializable;

/**
 * Faculty class that extends the User class.
 * Represents a faculty member who can act as a reviewer.
 */
public class Faculty extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String position;  // e.g., "Assistant Professor", "Associate Professor"
    private boolean isReviewer;  // Indicates if the faculty member is a reviewer

    /**
     * Constructor for creating a new Faculty
     */
    public Faculty(String name, String email, String password, String department, String position) {
        super(name, email, password);
        this.department = department;
        this.position = position;
        this.isReviewer = true;  // Default to true, as Faculty are typically reviewers
    }

    /**
     * Constructor for loading a Faculty from storage
     */
    public Faculty(String userId, String name, String email, String password,
                   String department, String position, boolean isReviewer) {
        super(userId, name, email, password);
        this.department = department;
        this.position = position;
        this.isReviewer = isReviewer;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isReviewer() {
        return isReviewer;
    }

    public void setReviewer(boolean isReviewer) {
        this.isReviewer = isReviewer;
    }

    @Override
    public String getRole() {
        return "Faculty" + (isReviewer ? ", Reviewer" : "");
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", isReviewer=" + isReviewer +
                '}';
    }
}