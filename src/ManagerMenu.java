import java.util.Scanner;

public class ManagerMenu {
    static Scanner scanner = new Scanner(System.in);
    public static boolean isLogin = true;


    public void ManagerFunction(String UserID){
        System.out.println("\n=== Manager Menu ===");
        System.out.println("1. Register New Employee");
        System.out.println("2. Clock In/Clock Out");
        System.out.println("3. Log Out");
        System.out.print("Enter your choice (1-3): ");
        int choice = scanner.nextInt();
        System.out.println(" ");

        switch(choice){
            case 1 : //register function
                System.out.print("Enter Employee Name: ");
                scanner.nextLine();
                String name = scanner.nextLine();
                System.out.print("Enter Employee ID: ");
                String id = scanner.nextLine();
                System.out.print("Set Password: ");
                String password = scanner.nextLine();
                System.out.print("Set Role: ");
                String role = scanner.nextLine();
                Employee.registerEmployee(name,id,password,role);
                break;
            case 2 :
                EmployeeMenu.Clocking(UserID);
                break;
            case 3:
                isLogin = false;
                break;
            default : System.out.println("Invalid choice");
        }

    }
}
