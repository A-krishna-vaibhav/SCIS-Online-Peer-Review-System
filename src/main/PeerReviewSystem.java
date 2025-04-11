package main;

import model.*;
import service.*;
import storage.*;
import java.io.File;
import java.util.*;
import java.time.format.DateTimeFormatter;

/**
 * Main class for the SCIS Online Peer Review System.
 * This is the main entry point for the application and demonstrates composition
 * by bringing together all the components of the system.
 */
public class PeerReviewSystem {
    // Services
    private final UserService userService;
    private final PaperService paperService;
    private final ReviewService reviewService;

    // Current logged-in user
    private User currentUser;

    // Scanner for user input
    private final Scanner scanner;

    // Date formatter for displaying dates
    private final DateTimeFormatter dateFormatter;

    /**
     * Constructor - initializes the system
     */
    public PeerReviewSystem() {
        // Create data storage directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Initialize storage
        DataStorage<User> userStorage = new FileStorage<>("data/users.dat", User.class);
        DataStorage<Paper> paperStorage = new FileStorage<>("data/papers.dat", Paper.class);
        DataStorage<Review> reviewStorage = new FileStorage<>("data/reviews.dat", Review.class);

        // Initialize services
        userService = new UserService(userStorage);
        paperService = new PaperService(paperStorage, userService);
        reviewService = new ReviewService(reviewStorage, paperService, userService);

        // Initialize scanner and date formatter
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Create default admin if none exists
        if (userService.getAllAdmins().isEmpty()) {
            userService.registerAdmin("Admin", "admin@scis.edu", "admin123", "System Admin");
            System.out.println("Default admin created: admin@scis.edu / admin123");
        }
    }

    /**
     * Main method - program entry point
     */
    public static void main(String[] args) {
        PeerReviewSystem system = new PeerReviewSystem();
        system.start();
    }

    /**
     * Start the system
     */
    public void start() {
        boolean exit = false;

        while (!exit) {
            if (currentUser == null) {
                displayLoginMenu();
            } else {
                displayMainMenu();
            }

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (currentUser == null) {
                exit = handleLoginMenu(choice);
            } else {
                exit = handleMainMenu(choice);
            }
        }

        System.out.println("Thank you for using SCIS Online Peer Review System!");
    }

    /**
     * Display login menu
     */
    private void displayLoginMenu() {
        System.out.println("\n===== SCIS Online Peer Review System =====");
        System.out.println("1. Login");
        System.out.println("2. Register as Student");
        System.out.println("3. Register as Faculty");
        System.out.println("0. Exit");
        System.out.println("=========================================");
    }

    /**
     * Handle login menu choices
     */
    private boolean handleLoginMenu(String choice) {
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                registerStudent();
                break;
            case "3":
                registerFaculty();
                break;
            case "0":
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Display main menu based on user role
     */
    private void displayMainMenu() {
        System.out.println("\n===== SCIS Online Peer Review System =====");
        System.out.println("Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("-----------------------------------------");

        // Common options for all users
        System.out.println("1. View Profile");
        System.out.println("2. Change Password");

        if (currentUser instanceof Student || currentUser instanceof Faculty) {
            System.out.println("3. Submit Paper");
            System.out.println("4. View My Papers");
            System.out.println("5. View Assigned Reviews");
            System.out.println("6. Submit Review");
        }

        if (currentUser instanceof Admin) {
            System.out.println("3. Manage Users");
            System.out.println("4. Manage Papers");
            System.out.println("5. Assign Reviewers");
            System.out.println("6. View Reviews");
        }

        System.out.println("0. Logout");
        System.out.println("=========================================");
    }

    /**
     * Handle main menu choices
     */
    private boolean handleMainMenu(String choice) {
        switch (choice) {
            case "1":
                viewProfile();
                break;
            case "2":
                changePassword();
                break;
            case "3":
                if (currentUser instanceof Admin) {
                    manageUsers();
                } else {
                    submitPaper();
                }
                break;
            case "4":
                if (currentUser instanceof Admin) {
                    managePapers();
                } else {
                    viewMyPapers();
                }
                break;
            case "5":
                if (currentUser instanceof Admin) {
                    assignReviewers();
                } else {
                    viewAssignedReviews();
                }
                break;
            case "6":
                if (currentUser instanceof Admin) {
                    viewReviews();
                } else {
                    submitReview();
                }
                break;
            case "0":
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    /**
     * Login functionality
     */
    private void login() {
        System.out.println("\n----- Login -----");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    /**
     * Register student functionality
     */
    private void registerStudent() {
        System.out.println("\n----- Register as Student -----");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();

        boolean success = userService.registerStudent(name, email, password, department, studentId);

        if (success) {
            System.out.println("Registration successful. You can now login.");
        } else {
            System.out.println("Registration failed. Email might already be registered.");
        }
    }

    /**
     * Register faculty functionality
     */
    private void registerFaculty() {
        System.out.println("\n----- Register as Faculty -----");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        System.out.print("Position: ");
        String position = scanner.nextLine();

        boolean success = userService.registerFaculty(name, email, password, department, position);

        if (success) {
            System.out.println("Registration successful. You can now login.");
        } else {
            System.out.println("Registration failed. Email might already be registered.");
        }
    }

    /**
     * View profile functionality
     */
    private void viewProfile() {
        System.out.println("\n----- My Profile -----");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Role: " + currentUser.getRole());

        if (currentUser instanceof Student student) {
            System.out.println("Department: " + student.getDepartment());
            System.out.println("Student ID: " + student.getStudentId());
        } else if (currentUser instanceof Faculty faculty) {
            System.out.println("Department: " + faculty.getDepartment());
            System.out.println("Position: " + faculty.getPosition());
        } else if (currentUser instanceof Admin admin) {
            System.out.println("Admin Level: " + admin.getAdminLevel());
        }
    }

    /**
     * Change password functionality
     */
    private void changePassword() {
        System.out.println("\n----- Change Password -----");
        System.out.print("Current Password: ");
        String currentPassword = scanner.nextLine();

        if (!currentUser.verifyPassword(currentPassword)) {
            System.out.println("Incorrect current password.");
            return;
        }

        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Confirm New Password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        currentUser.setPassword(newPassword);
        userService.updateUser(currentUser);
        System.out.println("Password changed successfully.");
    }

    /**
     * Submit paper functionality
     */
    private void submitPaper() {
        System.out.println("\n----- Submit Paper -----");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Abstract: ");
        String abstractText = scanner.nextLine();
        System.out.println("Content (type 'END' on a new line when finished):");
        StringBuilder contentBuilder = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            contentBuilder.append(line).append("\n");
        }
        String content = contentBuilder.toString();

        System.out.print("Keywords (comma-separated): ");
        String keywordsInput = scanner.nextLine();
        List<String> keywords = Arrays.asList(keywordsInput.split(",\\s*"));

        boolean success = paperService.submitPaper(title, abstractText, content,
                currentUser.getUserId(), keywords);

        if (success) {
            System.out.println("Paper submitted successfully.");
        } else {
            System.out.println("Paper submission failed.");
        }
    }

    /**
     * View my papers functionality
     */
    private void viewMyPapers() {
        System.out.println("\n----- My Papers -----");
        List<Paper> papers = paperService.getPapersByAuthor(currentUser.getUserId());

        if (papers.isEmpty()) {
            System.out.println("You haven't submitted any papers yet.");
            return;
        }

        for (int i = 0; i < papers.size(); i++) {
            Paper paper = papers.get(i);
            System.out.println((i + 1) + ". " + paper.getTitle() +
                    " (Status: " + paper.getStatus() + ")");
        }

        System.out.print("\nEnter paper number to view details (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.size()) {
                Paper paper = papers.get(index);
                viewPaperDetails(paper, true);
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * View assigned reviews functionality
     */
    private void viewAssignedReviews() {
        System.out.println("\n----- Papers Assigned for Review -----");
        List<Paper> papers = paperService.getPapersForReviewer(currentUser.getUserId());

        if (papers.isEmpty()) {
            System.out.println("You don't have any papers assigned for review.");
            return;
        }

        for (int i = 0; i < papers.size(); i++) {
            Paper paper = papers.get(i);
            System.out.println((i + 1) + ". " + paper.getTitle());

            // Check if user has already submitted a review for this paper
            Optional<Review> reviewOpt = reviewService
                    .getReviewByPaperAndReviewer(paper.getPaperId(), currentUser.getUserId());

            if (reviewOpt.isPresent()) {
                System.out.println("   [Review submitted]");
            } else {
                System.out.println("   [Review pending]");
            }
        }

        System.out.print("\nEnter paper number to view details (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.size()) {
                Paper paper = papers.get(index);
                viewPaperDetails(paper, false);
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Submit review functionality
     */
    private void submitReview() {
        System.out.println("\n----- Submit Review -----");
        List<Paper> papers = paperService.getPapersForReviewer(currentUser.getUserId());

        if (papers.isEmpty()) {
            System.out.println("You don't have any papers assigned for review.");
            return;
        }

        System.out.println("Papers available for review:");
        List<Paper> pendingReviews = new ArrayList<>();

        for (Paper paper : papers) {
            // Check if user has already submitted a review for this paper
            Optional<Review> reviewOpt = reviewService
                    .getReviewByPaperAndReviewer(paper.getPaperId(), currentUser.getUserId());

            if (reviewOpt.isEmpty()) {
                pendingReviews.add(paper);
                System.out.println((pendingReviews.size()) + ". " + paper.getTitle());
            }
        }

        if (pendingReviews.isEmpty()) {
            System.out.println("You have already reviewed all assigned papers.");
            return;
        }

        System.out.print("\nEnter paper number to review (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < pendingReviews.size()) {
                Paper paper = pendingReviews.get(index);

                System.out.println("\nPaper: " + paper.getTitle());
                System.out.println("Abstract: " + paper.getAbstractText());
                System.out.println("Content: " + paper.getContent());

                System.out.print("\nRating (1-5): ");
                int rating = Integer.parseInt(scanner.nextLine());

                System.out.println("Comments (type 'END' on a new line when finished):");
                StringBuilder commentsBuilder = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).equals("END")) {
                    commentsBuilder.append(line).append("\n");
                }
                String comments = commentsBuilder.toString();

                boolean success = reviewService.submitReview(paper.getPaperId(),
                        currentUser.getUserId(),
                        rating, comments);

                if (success) {
                    System.out.println("Review submitted successfully.");
                } else {
                    System.out.println("Review submission failed.");
                }
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Manage users functionality (Admin only)
     */
    private void manageUsers() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("Access denied.");
            return;
        }

        boolean back = false;

        while (!back) {
            System.out.println("\n----- Manage Users -----");
            System.out.println("1. List All Users");
            System.out.println("2. View User Details");
            System.out.println("3. Delete User");
            System.out.println("4. Create Admin User");
            System.out.println("0. Back");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listAllUsers();
                    break;
                case "2":
                    viewUserDetails();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    createAdminUser();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * List all users functionality (Admin only)
     */
    private void listAllUsers() {
        System.out.println("\n----- All Users -----");
        List<User> users = userService.getAllUsers();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + user.getName() + " (" + user.getRole() + ") - " + user.getEmail());
        }
    }

    /**
     * View user details functionality (Admin only)
     */
    private void viewUserDetails() {
        System.out.println("\n----- View User Details -----");
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        Optional<User> userOpt = userService.findUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role: " + user.getRole());

            switch (user) {
                case Student student -> {
                    System.out.println("Department: " + student.getDepartment());
                    System.out.println("Student ID: " + student.getStudentId());
                }
                case Faculty faculty -> {
                    System.out.println("Department: " + faculty.getDepartment());
                    System.out.println("Position: " + faculty.getPosition());
                }
                case Admin admin -> System.out.println("Admin Level: " + admin.getAdminLevel());
                default -> {
                }
            }

            // Show papers authored by this user
            List<Paper> papers = paperService.getPapersByAuthor(user.getUserId());
            if (!papers.isEmpty()) {
                System.out.println("\nPapers authored:");
                for (Paper paper : papers) {
                    System.out.println("- " + paper.getTitle() + " (Status: " + paper.getStatus() + ")");
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * Delete user functionality (Admin only)
     */
    private void deleteUser() {
        System.out.println("\n----- Delete User -----");
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();

        Optional<User> userOpt = userService.findUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Prevent deleting yourself
            if (user.getUserId().equals(currentUser.getUserId())) {
                System.out.println("You cannot delete your own account.");
                return;
            }

            System.out.println("Are you sure you want to delete user " + user.getName() + "? (y/n)");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                boolean success = userService.deleteUser(user.getUserId());

                if (success) {
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("Failed to delete user.");
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    /**
     * Create admin user functionality (Admin only)
     */
    private void createAdminUser() {
        System.out.println("\n----- Create Admin User -----");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Admin Level: ");
        String adminLevel = scanner.nextLine();

        boolean success = userService.registerAdmin(name, email, password, adminLevel);

        if (success) {
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Failed to create admin user. Email might already be registered.");
        }
    }

    /**
     * Manage papers functionality (Admin only)
     */
    private void managePapers() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("Access denied.");
            return;
        }

        boolean back = false;

        while (!back) {
            System.out.println("\n----- Manage Papers -----");
            System.out.println("1. List All Papers");
            System.out.println("2. View Paper Details");
            System.out.println("3. Change Paper Status");
            System.out.println("4. Delete Paper");
            System.out.println("0. Back");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listAllPapers();
                    break;
                case "2":
                    viewPaperDetailsAdmin();
                    break;
                case "3":
                    changePaperStatus();
                    break;
                case "4":
                    deletePaper();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * List all papers functionality (Admin only)
     */
    private void listAllPapers() {
        System.out.println("\n----- All Papers -----");
        List<Paper> papers = paperService.getAllPapers();

        if (papers.isEmpty()) {
            System.out.println("No papers found in the system.");
            return;
        }

        for (int i = 0; i < papers.size(); i++) {
            Paper paper = papers.get(i);
            String authorName = userService.findUserById(paper.getAuthorId())
                    .map(User::getName)
                    .orElse("Unknown");

            System.out.println(STR."\{i + 1}. \{paper.getTitle()} (Author: \{authorName}, Status: \{paper.getStatus()})");
        }
    }

    /**
     * View paper details functionality (Admin only)
     */
    private void viewPaperDetailsAdmin() {
        System.out.println("\n----- View Paper Details -----");
        System.out.print("Enter paper ID or title: ");
        String search = scanner.nextLine();

        Optional<Paper> papers = PaperService.findPaperById(search);

        if (papers.isEmpty()) {
            System.out.println("No papers found matching your search.");
            return;
        }

        System.out.println("\nFound papers:");
        for (int i = 0; i < papers.stream().count(); i++) {
            Paper paper = papers.get();
            System.out.println(STR."\{i + 1}. \{paper.getTitle()}");
        }

        System.out.print("\nEnter paper number to view details (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.stream().count()) {
                Paper paper = papers.get();
                viewPaperDetails(paper, true);
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Change paper status functionality (Admin only)
     */
    private void changePaperStatus() {
        System.out.println("\n----- Change Paper Status -----");
        System.out.print("Enter paper ID or title: ");
        String search = scanner.nextLine();

        Optional<Paper> papers = PaperService.findPaperById(search);

        if (papers.isEmpty()) {
            System.out.println("No papers found matching your search.");
            return;
        }

        System.out.println("\nFound papers:");
        for (int i = 0; i < papers.stream().count(); i++) {
            Paper paper = papers.get();
            System.out.println(STR."\{i + 1}. \{paper.getTitle()} (Current status: \{paper.getStatus()})");
        }

        System.out.print("\nEnter paper number to change status (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.stream().count()) {
                Paper paper = papers.get();

                System.out.println("\nAvailable statuses:");
                System.out.println("1. SUBMITTED");
                System.out.println("2. UNDER_REVIEW");
                System.out.println("3. ACCEPTED");
                System.out.println("4. REJECTED");
                System.out.println("5. REVISIONS_REQUIRED");

                System.out.print("Enter new status number: ");
                String statusChoice = scanner.nextLine();

                String newStatus;
                switch (statusChoice) {
                    case "1":
                        newStatus = "SUBMITTED";
                        break;
                    case "2":
                        newStatus = "UNDER_REVIEW";
                        break;
                    case "3":
                        newStatus = "ACCEPTED";
                        break;
                    case "4":
                        newStatus = "REJECTED";
                        break;
                    case "5":
                        newStatus = "REVISIONS_REQUIRED";
                        break;
                    default:
                        System.out.println("Invalid status.");
                        return;
                }

                paper.setStatus(ReviewStatus.valueOf(newStatus));
                paperService.updatePaper(paper);
                System.out.println(STR."Paper status updated to: \{newStatus}");
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Delete paper functionality (Admin only)
     */
    private void deletePaper() {
        System.out.println("\n----- Delete Paper -----");
        System.out.print("Enter paper ID: ");
        String search = scanner.nextLine();

        Optional<Paper> papers = PaperService.findPaperById(search);

        if (papers.isEmpty()) {
            System.out.println("No papers found matching your search.");
            return;
        }

        System.out.println("\nFound papers:");
        for (int i = 0; i < papers.stream().count(); i++) {
            Paper paper = papers.get();
            System.out.println(STR."\{i + 1}. \{paper.getTitle()}");
        }

        System.out.print("\nEnter paper number to delete (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.stream().count()) {
                Paper paper = papers.get();

                System.out.println(STR."Are you sure you want to delete paper \"\{paper.getTitle()}\"? (y/n)");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y")) {
                    boolean success = paperService.deletePaper(paper.getPaperId());

                    if (success) {
                        System.out.println("Paper deleted successfully.");
                    } else {
                        System.out.println("Failed to delete paper.");
                    }
                }
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Assign reviewers functionality (Admin only)
     */
    private void assignReviewers() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("Access denied.");
            return;
        }

        System.out.println("\n----- Assign Reviewers -----");

        // Get papers that can be assigned for review
        List<Paper> papers = paperService.getAllPapers()
                .stream()
                .filter(p -> p.getStatus().equals("SUBMITTED") || p.getStatus().equals("UNDER_REVIEW"))
                .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()))
                .collect(java.util.stream.Collectors.toList());

        if (papers.isEmpty()) {
            System.out.println("No papers available for review assignment.");
            return;
        }

        System.out.println("Papers available for review assignment:");
        for (int i = 0; i < papers.size(); i++) {
            Paper paper = papers.get(i);
            System.out.println(STR."\{i + 1}. \{paper.getTitle()} (Status: \{paper.getStatus()})");
        }

        System.out.print("\nEnter paper number to assign reviewers (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.size()) {
                Paper paper = papers.get(index);
                assignReviewersForPaper(paper);
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Assign reviewers for a specific paper
     */
    private void assignReviewersForPaper(Paper paper) {
        System.out.println(STR."""
        Paper: \{paper.getTitle()}""");

        // Get current reviewers
        List<String> currentReviewerIds = reviewService.getReviewersForPaper(paper.getPaperId());
        List<User> currentReviewers = new ArrayList<>();

        for (String reviewerId : currentReviewerIds) {
            userService.findUserById(reviewerId).ifPresent(currentReviewers::add);
        }

        System.out.println("\nCurrent Reviewers:");
        if (currentReviewers.isEmpty()) {
            System.out.println("None");
        } else {
            for (User reviewer : currentReviewers) {
                System.out.println(STR."- \{reviewer.getName()} (\{reviewer.getRole()})");
            }
        }

        // Get potential reviewers (exclude the author)
        List<User> potentialReviewers = userService.getAllUsers()
                .stream()
                .filter(u -> !u.getUserId().equals(paper.getAuthorId()) && !currentReviewerIds.contains(u.getUserId()))
                .filter(u -> u instanceof Faculty || u instanceof Student) // Only faculty and students can review
                .sorted((u1, u2) -> u1.getName().compareTo(u2.getName()))
                .collect(java.util.stream.Collectors.toList());

        if (potentialReviewers.isEmpty()) {
            System.out.println("\nNo potential reviewers available.");
            return;
        }

        System.out.println("\nPotential reviewers:");
        for (int i = 0; i < potentialReviewers.size(); i++) {
            User user = potentialReviewers.get(i);
            System.out.println(STR."\{i + 1}. \{user.getName()} (\{user.getRole()})");
        }

        System.out.print("\nEnter reviewer number to assign (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < potentialReviewers.size()) {
                User reviewer = potentialReviewers.get(index);

                boolean success = paperService.assignReviewer(paper.getPaperId(), reviewer.getUserId());

                if (success) {
                    System.out.println(STR."\{reviewer.getName()} assigned as a reviewer successfully.");

                    // Update paper status if needed
                    if (paper.getStatus().equals("SUBMITTED")) {
                        paper.setStatus(ReviewStatus.valueOf("UNDER_REVIEW"));
                        paperService.updatePaper(paper);
                        System.out.println("Paper status updated to UNDER_REVIEW.");
                    }
                } else {
                    System.out.println("Failed to assign reviewer.");
                }
            } else {
                System.out.println("Invalid reviewer number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * View reviews functionality (Admin only)
     */
    private void viewReviews() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("Access denied.");
            return;
        }

        System.out.println("\n----- View Reviews -----");
        System.out.print("Enter paper ID: ");
        String search = scanner.nextLine();

        Optional<Paper> papers = PaperService.findPaperById(search);

        if (papers.isEmpty()) {
            System.out.println("No papers found matching your search.");
            return;
        }

        System.out.println("\nFound papers:");
        for (int i = 0; i < papers.stream().count(); i++) {
            Paper paper = papers.get();
            System.out.println((i + 1) + ". " + paper.getTitle());
        }

        System.out.print("\nEnter paper number to view reviews (0 to go back): ");
        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < papers.stream().count()) {
                Paper paper = papers.get();

                List<Review> reviews = reviewService.getReviewsForPaper(paper.getPaperId());

                if (reviews.isEmpty()) {
                    System.out.println("No reviews found for this paper.");
                    return;
                }

                System.out.println("\n----- Reviews for \"" + paper.getTitle() + "\" -----");

                for (Review review : reviews) {
                    User reviewer = (User) userService.getUserById(review.getReviewerId()).orElse(null);
                    String reviewerName = reviewer != null ? reviewer.getName() : "Unknown";

                    System.out.println("\nReviewer: " + reviewerName);
                    System.out.println("Rating: " + review.getRating() + "/5");
                    System.out.println("Date: " + review.getSubmissionDate().format(dateFormatter));
                    System.out.println("Comments: " + review.getComments());
                    System.out.println("----------------------------------------");
                }
            } else {
                System.out.println("Invalid paper number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * View paper details
     */
    private void viewPaperDetails(Paper paper, boolean isAuthor) {
        System.out.println("\n----- Paper Details -----");
        System.out.println("Title: " + paper.getTitle());

        User author = (User) userService.getUserById(paper.getAuthorId()).orElse(null);
        String authorName = author != null ? author.getName() : "Unknown";

        System.out.println("Author: " + authorName);
        System.out.println("Status: " + paper.getStatus());
        System.out.println("Submitted: " + paper.getSubmissionDate().format(dateFormatter));
        System.out.println("Keywords: " + String.join(", ", paper.getKeywords()));
        System.out.println("\nAbstract:\n" + paper.getAbstractText());

        if (isAuthor || currentUser instanceof Admin) {
            System.out.println("\nContent:\n" + paper.getContent());
        }

        // If admin or author, show reviews
        if (currentUser instanceof Admin || isAuthor) {
            List<Review> reviews = reviewService.getReviewsForPaper(paper.getPaperId());

            if (!reviews.isEmpty()) {
                System.out.println("\n----- Reviews -----");

                for (Review review : reviews) {
                    User reviewer = (User) userService.getUserById(review.getReviewerId()).orElse(null);
                    String reviewerName = reviewer != null ? reviewer.getName() : "Unknown";

                    System.out.println("\nReviewer: " + reviewerName);
                    System.out.println("Rating: " + review.getRating() + "/5");
                    System.out.println("Date: " + review.getSubmissionDate().format(dateFormatter));
                    System.out.println("Comments: " + review.getComments());
                    System.out.println("----------------------------------------");
                }
            } else {
                System.out.println("\nNo reviews submitted yet.");
            }
        }

        // Wait for user input before returning
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Logout functionality
     */
    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}