import java.sql.*;
import java.util.Scanner;


public class EmployeeMenu extends Menu{
    static Scanner scanner = new Scanner(System.in);
    public static boolean isLogin = true;

    public static void displayMenu(String UserID){
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Clock In/Clock Out");
        System.out.println("2. Stock Management");
        System.out.println("3. Sales");
        System.out.println("4. Search Information");
        System.out.println("5. Edit Information");
        System.out.println("6. Log Out");
        System.out.print("Enter your choice (1-6): ");
        int choice = scanner.nextInt();
        System.out.println(" ");

        switch(choice){
            case 1 :
                Attendance.clockTime(UserID);
                break;
            case 2 :
                Stock.stockSystem(UserID);
                break;
            case 3:
                Sales.newSales(UserID);
                break;
            case 4:
                searchMenu();
                break;
            case 5:
                editMenu();
                break;
            case 6:
                isLogin = false;
                break;

            default : System.out.println("Invalid choice");
        }
    }



}

