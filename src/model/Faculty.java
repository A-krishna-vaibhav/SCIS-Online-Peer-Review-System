package model;

/**
 * Faculty class that extends the User class.
 * Another example of inheritance in the system.
 */
public class Faculty extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String position;  // e.g., "Assistant Professor", "Associate Professor"

    /**
     * Constructor for creating a new Faculty
     */
    public Faculty(String name, String email, String password, String department, String position) {
        super(name, email, password);
        this.department = department;
        this.position = position;
    }

    /**
     * Constructor for loading a Faculty from storage
     */
    public Faculty(String userId, String name, String email, String password,
                   String department, String position) {
        super(userId, name, email, password);
        this.department = department;
        this.position = position;
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

    @Override
    public String getRole() {
        return "Faculty";
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}