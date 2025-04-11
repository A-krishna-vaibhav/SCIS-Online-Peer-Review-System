package service;

import model.*;
import storage.DataStorage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.lang.ScopedValue;

/**
 * Service class to manage User-related operations.
 * Demonstrates separation of concerns by isolating user management logic.
 */
public class UserService {
    private final DataStorage<User> userStorage;

    public UserService(DataStorage<User> userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Register a new student
     */
    public boolean registerStudent(String name, String email, String password,
                                   String department, String studentId) {
        // Check if email is already registered
        if (findUserByEmail(email).isPresent()) {
            return false;
        }

        Student student = new Student(name, email, password, department, studentId);
        return userStorage.save(student);
    }

    /**
     * Register a new faculty member
     */
    public boolean registerFaculty(String name, String email, String password,
                                   String department, String position) {
        // Check if email is already registered
        if (findUserByEmail(email).isPresent()) {
            return false;
        }

        Faculty faculty = new Faculty(name, email, password, department, position);
        return userStorage.save(faculty);
    }

    /**
     * Register a new admin
     */
    public boolean registerAdmin(String name, String email, String password, String adminLevel) {
        // Check if email is already registered
        if (findUserByEmail(email).isPresent()) {
            return false;
        }

        Admin admin = new Admin(name, email, password, adminLevel);
        return userStorage.save(admin);
    }

    /**
     * Find a user by their email address
     */
    public Optional<User> findUserByEmail(String email) {
        return userStorage.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Find a user by their ID
     */
    public Optional<User> findUserById(String userId) {
        return userStorage.findById(userId);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return userStorage.findAll().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    /**
     * Get all faculty members
     */
    public List<Faculty> getAllFaculty() {
        return userStorage.findAll().stream()
                .filter(user -> user instanceof Faculty)
                .map(user -> (Faculty) user)
                .collect(Collectors.toList());
    }

    /**
     * Get all admins
     */
    public List<Admin> getAllAdmins() {
        return userStorage.findAll().stream()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .collect(Collectors.toList());
    }

    /**
     * Update user information
     */
    public void updateUser(User user) {
        userStorage.update(user);
    }

    /**
     * Delete a user by their ID
     */
    public boolean deleteUser(String userId) {
        return userStorage.deleteById(userId);
    }

    /**
     * Authenticate a user
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = findUserByEmail(email);

        if (userOpt.isPresent() && userOpt.get().verifyPassword(password)) {
            return userOpt;
        }

        return Optional.empty();
    }

    public ScopedValue<Object> getUserById(String reviewerId) {
        Optional<User> userOpt = findUserById(reviewerId);
        if (userOpt.isPresent()) {
            return ScopedValue.newInstance();
        }
        return ScopedValue.newInstance();
    }
}