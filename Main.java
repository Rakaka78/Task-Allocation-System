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
    int status;

    public User(int id, String name, Set<String> skills, Availability availability, String workload, int status) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.workload = workload;
        this.availability = availability;
        this.status = status;
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
        String statusString = (status == 0) ? "Completed" : (status == 1) ? "Processing" : "On Hold";

        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                ", availability=" + availability +
                ", workload=" + workload +
                ", status= " + statusString +
                '}';
    }
}

class Task {
    static int taskIdCounter = 1;
    int id;
    String title;
    String description;
    Set<String> requiredSkills;
    User assignedUser;
    int status;

    public Task(String title, String description, Set<String> requiredSkills, int Status) {
        this.id = taskIdCounter++;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.status = Status;

    }

    public void assignToUser(User user) {
        this.assignedUser = user;
    }

    @Override
    public String toString() {
        String statusString = (status == 0) ? "Completed" : (status == 1) ? "Processing" : "On Hold";
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", assignedUser=" + (assignedUser != null ? assignedUser.name : "Unassigned") +
                ",status="+ (assignedUser != null ? statusString : "On Hold") +
                '}';
    }
}

public class Main {
    private static final String USER_FILE = "users.txt";
    private static final String TASK_FILE = "tasks.txt";
    private static Task tasks;

    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<User> team = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        loadUsers(team);
        loadTasks(tasks, team);

        int choice = 0;
        while (choice != 7) {
            System.out.println("Menu:");
            System.out.println("1. Worker Management");
            System.out.println("2. Task Management");
            System.out.println("3. Workload Tracking");
            System.out.println("4. Exit");
            System.out.println("Enter your choice:");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    userManagement(scanner, team,tasks);
                    break;
                case 2:
                    taskManagement(scanner, tasks, team);
                    break;
                case 3:
                    trackWorkload(team);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }

        scanner.close();
    }
    private static void userManagement(Scanner scanner, List<User> team, List<Task> task) {
        int choice = 0;
        while (choice != 6) {
            System.out.println("User Management:");
            System.out.println("1. Add User");
            System.out.println("2. List Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Save User");
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
                    updateUser(scanner, team);
                    break;
                case 4:
                    deleteUser(scanner, team);
                    break;
                case 5:
                    saveUsers(team,task);
                    saveTasks(task);
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void trackWorkload(List<User> team) {
        System.out.println("Workload Tracking:");
        for (User user : team) {
            System.out.println("User: " + user.name + ", Workload: " + user.workload);
        }
    }

    private static void addUser(Scanner scanner, List<User> team ) {
        System.out.print("Enter user ID:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter user name:");
        String name = scanner.nextLine();
        System.out.print("Enter user skills (comma-separated):");
        String[] skillsArray = scanner.nextLine().split(",");
        Set<String> skills = new HashSet<>(Arrays.asList(skillsArray));
        team.add(new User(id, name, skills, Availability.AVAILABLE, "null", 0));
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
    private static void updateUser(Scanner scanner, List<User> team){
        System.out.println("Enter the ID or Name of User you want to update:");
        String keyword = scanner.nextLine();
        boolean found = false;
        for (User user : team) {
            if (isInteger(keyword)) {
                if (user.id == Integer.parseInt(keyword)) {
                    System.out.println(user);
                    updateUserData(scanner, user);
                    System.out.println("User found successfully.");
                    found = true;
                    break;
                }
            } else {
                if (user.name.equals(keyword)) {
                    System.out.println(user);
                    System.out.println("User found successfully.");
                    updateUserData(scanner, user);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void updateUserData(Scanner scanner, User user) {
        System.out.println("Select update option:");
        System.out.println("1. Update Name");
        System.out.println("2. Update Skills");
        System.out.println("3. Update Status");
        System.out.println("4. Exit");
        System.out.println("Enter your choice:");

        int updateOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (updateOption) {
            case 1:
                updateUserName(scanner, user);
                break;
            case 2:
                updateUserSkills(scanner, user);
                break;
            case 3:
                updateUserStatus(scanner, user);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void updateUserName(Scanner scanner, User user) {
        System.out.println(user.name);
        System.out.println("Enter new user name:");
        user.name = scanner.nextLine();
        System.out.println("User skills updated successfully.");
    }

    private static void updateUserStatus(Scanner scanner, User user) {
        String statusString = (user.status == 0) ? "Completed" : (user.status == 1) ? "Processing" : "On Hold";
        System.out.println(statusString);
        System.out.println("0. Completed");
        System.out.println("1. Processing");
        System.out.println("2. On Hold");
        System.out.println("Enter new status value:");
        user.status = scanner.nextInt();

        System.out.println("User skills updated successfully.");

    }

    private static void updateUserSkills(Scanner scanner, User user) {
        System.out.println(user.skills);
        System.out.println("Enter new skills (comma-separated):");
        String[] newSkillsArray = scanner.nextLine().split(",");
        user.skills = new HashSet<>(Arrays.asList(newSkillsArray));
        System.out.println("User skills updated successfully.");
    }

    private static void deleteUser(Scanner scanner, List<User> team){
        System.out.println("Enter the ID or Name of User you want to update:");
        String keyword = scanner.nextLine();
        Iterator<User> iterator = team.iterator();
        boolean found = false;
        for (User user : team) {
            if (isInteger(keyword)) {
                if (user.id == Integer.parseInt(keyword)) {
                    iterator.remove();
                    System.out.println("User deleted successfully.");
                    found = true;
                    break;
                }
            } else {
                if (user.name.equals(keyword)) {
                    iterator.remove();
                    System.out.println("User deleted successfully.");
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("User not found.");
        }
    }

    private static void saveUsers(List<User> team, List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : team) {
                if (user.status == 0) {
                    user.availability = Availability.AVAILABLE;
                }
                writer.println(user.id + "," + user.name + "," + String.join(";", user.skills) + "," + user.availability + "," + user.workload + "," + user.status);
            }

            System.out.println("Users saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
        }
    }

    private static void taskManagement(Scanner scanner, List<Task> tasks, List<User> team) {
        int choice = 0;
        while (choice != 6) {
            System.out.println("Task Management:");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Save Task");
            System.out.println("6. Back to Main Menu");
            System.out.println("Enter your choice:");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask(scanner, tasks, team);
                    break;
                case 2:
                    listTasks(tasks ,team);
                    break;
                case 3:
                    updateTask(scanner, tasks, team);
                    break;
                case 4:
                    deleteTask(scanner, tasks);
                    break;
                case 5:
                    saveTasks(tasks);
                    break;
                case 6:
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
        int status = Integer.parseInt("2");
        Task task = new Task(title, description, requiredSkills, status);
        assignTask(task, team);
        tasks.add(task);
        System.out.println("Task added successfully.");
    }

    private static void assignTask(Task task, List<User> team) {
        for (User user : team) {
            if (user.hasRequiredSkills(task.requiredSkills) && user.isAvailable()) {
                user.availability = Availability.BUSY;
                user.status = Integer.parseInt("1");
                task.assignToUser(user);
                user.updateWorkload(task.title);
                break;
            }
        }
    }

    private static void listTasks(List<Task> tasks, List<User> user) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("List of Tasks:");

            for (Task task : tasks) {
//                    for (User users : user){
//                        if ( == task.title){
//                            task.status = users.status;
//                        }
//
//                        break;
//                    }
                System.out.println(task);


            }

        }
    }

    private static void updateTask(Scanner scanner, List<Task> tasks, List<User> team) {
        System.out.println("Enter the ID of the task to update:");
        int taskId = scanner.nextInt();
        scanner.nextLine();
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
                int status = Integer.parseInt(parts[5]);
                team.add(new User(id, name, skills, availability, workload, status));
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
                Set<String> requiredSkills = new HashSet<>(Arrays.asList(parts[2].split(";")));
                int status = Integer.parseInt(parts[4]);

                Task task = new Task(title, description, requiredSkills, status);
                int userId = Integer.parseInt(parts[3]);
                for (Task task1 : tasks) {
                    if (task.status!=0 ) {
                        for (User user : team) {


                            if (user.id == userId) {
//                                task1.status == user.status
                                task.assignToUser(user);
                                break;
                            }

                        }
                    }
                }



                tasks.add(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Task file not found. Creating new file...");
        }
    }


    private static void saveAllData(List<User> team, List<Task> tasks) {

        saveUsers(team,tasks);
        saveTasks(tasks);
    }

    private static void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASK_FILE))) {
            for (Task task : tasks) {

                int userId = task.assignedUser != null ? task.assignedUser.id : -1;
                writer.println(task.title + "," + task.description + "," + String.join(";", task.requiredSkills) + "," + userId + "," + task.status);
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

}