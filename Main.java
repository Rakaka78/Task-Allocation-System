import java.io.*;
import java.util.*;

enum Availability {
    AVAILABLE,
    BUSY
}

class User {
    int id;
    String name;
    Set<String> skills;
    Availability availability;
    String workload;

    public User(int id, String name, Set<String> skills, Availability availability, String workload) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.availability = availability;
        this.workload = workload;

    }



    public boolean hasRequiredSkills(Set<String> requiredSkills) {
        return skills.containsAll(requiredSkills);
    }

    public boolean isAvailable() {
        return availability == Availability.AVAILABLE;
    }

    public void updateWorkload(String additionalWorkload) {
        workload = additionalWorkload;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                ", availability=" + availability +
                ", workload=" + workload +
                '}';
    }
}

class Task {
    static int taskIdCounter = 1;
    int id;
    String title;
    String description;
    Set<String> requiredSkills;
    String workload;
    User assignedUser;

    String status;

    public Task(String title, String description, Set<String> requiredSkills, String workload, String Status) {
        this.id = taskIdCounter++;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.workload = this.title;
        this.status = Status;

    }

    public void assignToUser(User user) {
        this.assignedUser = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", workload=" + workload +
                ", assignedUser=" + (assignedUser != null ? assignedUser.name : "Unassigned") +
                ",status="+status+
                '}';
    }
}

public class Main {
    private static final String USER_FILE = "users.txt";
    private static final String TASK_FILE = "tasks.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<User> team = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        loadUsers(team);
        loadTasks(tasks, team);

        int choice = 0;
        while (choice != 7) {
            System.out.println("Menu:");
            System.out.println("1. User Management");
            System.out.println("2. Task Management");
            System.out.println("3. Workload Tracking");
            System.out.println("4. Report");
            System.out.println("5. Save All Data");
            System.out.println("6. Exit");
            System.out.println("Enter your choice:");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    userManagement(scanner, team);
                    break;
                case 2:
                    taskManagement(scanner, tasks, team);
                    break;
                case 3:
                    // Workload tracking
                    trackWorkload(team);
                    break;
                case 4:
                    // Report
                    break;
                case 5:
                    saveAllData(team, tasks);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }

        scanner.close();
    }

    private static void userManagement(Scanner scanner, List<User> team) {
        int choice = 0;
        while (choice != 5) {
            System.out.println("User Management:");
            System.out.println("1. Add User");
            System.out.println("2. List Users");
            System.out.println("3. Update Status");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addUser(scanner, team);
                    break;
                case 2:
                    listUsers(team);
                    break;
                case 3:
                    updateStatus(scanner, team);
                    break;
                case 4:
                    updateUser(scanner, team);
                    break;
                case 5:
                    deleteUser(scanner, team);
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void updateStatus(Scanner scanner, List<User> team) {
        int choice = 0;
        System.out.println("=================updateStatus===============");
        System.out.println("Status Option");
        System.out.println("1. Ready");
        System.out.println("2. processing");
        System.out.println("3. Cancel");
        System.out.println("Enter Choice: ");
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice){
            case 1:
                upReady(scanner, team);
                break;
            case 2:

                break;
            case 3:

                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }

    }

    private static void upReady(Scanner scanner, List<User> team) {
        System.out.println("Enter the ID of the user to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        boolean found = false;
        for (User user : team) {
            if (user.id == id) {
                updateStatusData(scanner, user);
                System.out.println("Status updated successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void updateStatusData(Scanner scanner, User user) {

        user.availability = Availability.AVAILABLE;
        user.workload = "null";
    }

    private static void trackWorkload(List<User> team) {
        System.out.println("Workload Tracking:");
        for (User user : team) {
            System.out.println("User: " + user.name + ", Workload: " + user.workload);
        }
    }

    private static void addUser(Scanner scanner, List<User> team ) {
        System.out.println("Enter user ID:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        System.out.println("Enter user skills (comma-separated):");
        String[] skillsArray = scanner.nextLine().split(",");
        Set<String> skills = new HashSet<>(Arrays.asList(skillsArray));

        team.add(new User(id, name, skills, Availability.AVAILABLE, "null"));
        System.out.println("User added successfully.");
    }

    private static void listUsers(List<User> team) {
        if (team.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("List of Users:");
            for (User user : team) {
                System.out.println(user);
            }
        }
    }

    private static void updateUser(Scanner scanner, List<User> team) {
        System.out.println("Select update option:");
        System.out.println("1. Update by ID");
        System.out.println("2. Update by Name");
        System.out.println("3. Update Skills");
        System.out.println("Enter your choice:");

        int updateOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (updateOption) {
            case 1:
                updateUserByID(scanner, team);
                break;
            case 2:
                updateUserByName(scanner, team);
                break;
            case 3:
                updateSkills(scanner, team);
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void updateUserByID(Scanner scanner, List<User> team) {
        System.out.println("Enter the ID of the user to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        boolean found = false;
        for (User user : team) {
            if (user.id == id) {
                updateUserData(scanner, user);
                System.out.println("User updated successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void updateUserByName(Scanner scanner, List<User> team) {
        System.out.println("Enter the name of the user to update:");
        String name = scanner.nextLine();
        boolean found = false;
        for (User user : team) {
            if (user.name.equals(name)) {
                updateUserData(scanner, user);
                System.out.println("User updated successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void updateUserData(Scanner scanner, User user) {
        System.out.println("Enter new user name:");
        user.name = scanner.nextLine();
        System.out.println("Enter new user skills (comma-separated):");
        String[] skillsArray = scanner.nextLine().split(",");
        user.skills = new HashSet<>(Arrays.asList(skillsArray));
    }

    private static void updateSkills(Scanner scanner, List<User> team) {
        System.out.println("Enter the ID of the user whose skills you want to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        boolean found = false;
        for (User user : team) {
            if (user.id == id) {
                System.out.println("Enter new skills (comma-separated):");
                String[] newSkillsArray = scanner.nextLine().split(",");
                user.skills = new HashSet<>(Arrays.asList(newSkillsArray));
                System.out.println("User skills updated successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }


    private static void deleteUser(Scanner scanner, List<User> team) {
        System.out.println("Enter the ID of the user to delete:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Iterator<User> iterator = team.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.id == id) {
                iterator.remove();
                System.out.println("User deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void taskManagement(Scanner scanner, List<Task> tasks, List<User> team) {
        int choice = 0;
        while (choice != 5) {
            System.out.println("Task Management:");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask(scanner, tasks, team);
                    break;
                case 2:
                    listTasks(tasks);
                    break;
                case 3:
                    updateTask(scanner, tasks, team);
                    break;
                case 4:
                    deleteTask(scanner, tasks);
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void addTask(Scanner scanner, List<Task> tasks, List<User> team) {
        System.out.println("Enter task title:");
        String title = scanner.nextLine();
        System.out.println("Enter task description:");
        String description = scanner.nextLine();
        System.out.println("Enter required skills (comma-separated):");
        String[] skillsArray = scanner.nextLine().split(",");
        Set<String> requiredSkills = new HashSet<>(Arrays.asList(skillsArray));

        String workload = title;


        Task task = new Task(title, description, requiredSkills, workload,);
        assignTask(task, team);
        tasks.add(task);
        System.out.println("Task added successfully.");
    }

    private static void assignTask(Task task, List<User> team) {
        for (User user : team) {
            if (user.hasRequiredSkills(task.requiredSkills) && user.isAvailable()) {
                user.availability = Availability.BUSY;
                task.assignToUser(user);
                user.updateWorkload(task.workload);
                break;
            }
        }
    }

    private static void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("List of Tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void updateTask(Scanner scanner, List<Task> tasks, List<User> team) {
        System.out.println("Enter the ID of the task to update:");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        boolean found = false;
        for (Task task : tasks) {
            if (task.id == taskId) {
                System.out.println("Enter new task title:");
                task.title = scanner.nextLine();
                System.out.println("Enter new task description:");
                task.description = scanner.nextLine();
                System.out.println("Enter new required skills (comma-separated):");
                String[] skillsArray = scanner.nextLine().split(",");
                task.requiredSkills = new HashSet<>(Arrays.asList(skillsArray));

                task.workload = task.title;

                assignTask(task, team);
                System.out.println("Task updated successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask(Scanner scanner, List<Task> tasks) {
        System.out.println("Enter the ID of the task to delete:");
        int taskId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Iterator<Task> iterator = tasks.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.id == taskId) {
                iterator.remove();
                System.out.println("Task deleted successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Task not found.");
        }
    }

    private static void loadUsers(List<User> team) {
        try (Scanner scanner = new Scanner(new File(USER_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                Set<String> skills = new HashSet<>(Arrays.asList(parts[2].split(";"))); // Assuming skills are separated by semicolons
                Availability availability = Availability.valueOf(parts[3]);
                String workload = String.valueOf(parts[4]);
                team.add(new User(id, name, skills, availability, workload));
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Creating new file...");
        }
    }


    private static void loadTasks(List<Task> tasks, List<User> team) {
        try (Scanner scanner = new Scanner(new File(TASK_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String title = parts[0];
                String description = parts[1];
                Set<String> requiredSkills = new HashSet<>(Arrays.asList(parts[2].split(";"))); // Assuming skills are separated by semicolons
                String workload = String.valueOf(parts[3]);
                Task task = new Task(title, description, requiredSkills, workload);
                int userId = Integer.parseInt(parts[4]);
                for (User user : team) {
                    if (user.id == userId) {
                        task.assignToUser(user);
                        break;
                    }
                }
                tasks.add(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Task file not found. Creating new file...");
        }
    }


    private static void saveAllData(List<User> team, List<Task> tasks) {
        saveUsers(team);
        saveTasks(tasks);
    }

    private static void saveUsers(List<User> team) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : team) {
                writer.println(user.id + "," + user.name + "," + String.join(";", user.skills) + "," + user.availability + "," + user.workload);
            }
            System.out.println("Users saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
        }
    }

    private static void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASK_FILE))) {
            for (Task task : tasks) {
                int userId = task.assignedUser != null ? task.assignedUser.id : -1;
                writer.println(task.title + "," + task.description + "," + String.join(";", task.requiredSkills) + "," + task.workload + "," + userId);
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

}
