
import java.util.Scanner;

public class ManagerMenu extends Menu {
    static Scanner scanner = new Scanner(System.in);
    public static boolean isLogin = true;

    public static void displayMenu(String UserID) {
        System.out.println("\n=== Manager Menu ===");
        System.out.println("1. Register New Employee");
        System.out.println("2. Clock In/Clock Out");
        System.out.println("3. Stock Management");
        System.out.println("4. Sales");
        System.out.println("5. Search Information");
        System.out.println("6. Edit Information");
        System.out.println("7. Log Out");
        System.out.print("Enter your choice (1-7): ");
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
                Attendance.clockTime(UserID);
                break;
            case 3 :
                Stock.stockSystem(UserID);
                break;
            case 4:
                Sales.newSales(UserID);
                break;
            case 5:
                searchMenu();
                break;
            case 6:
                editMenu();
                break;
            case 7:
                isLogin = false;
                break;
            default : System.out.println("Invalid choice");
        }
    }

}
