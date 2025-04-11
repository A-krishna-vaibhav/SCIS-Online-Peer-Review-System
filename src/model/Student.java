package model;

/**
 * Student class that extends the User class.
 * Demonstrates inheritance by extending the base User class.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String studentId;  // Academic ID

    /**
     * Constructor for creating a new Student
     */
    public Student(String name, String email, String password, String department, String studentId) {
        super(name, email, password);
        this.department = department;
        this.studentId = studentId;
    }

    /**
     * Constructor for loading a Student from storage
     */
    public Student(String userId, String name, String email, String password,
                   String department, String studentId) {
        super(userId, name, email, password);
        this.department = department;
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}