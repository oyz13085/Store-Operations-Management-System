import constant.Clock_Type;
import constant.Color;
import constant.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class EmployeeMenu {
    static Scanner scanner = new Scanner(System.in);
    public static boolean isLogin = true;

    public static void Clocking(String id,String type){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement clocking = connection.prepareStatement("INSERT INTO " + SQL.DB_Clock + " (EmployeeID,ClockType,ClockTime)"
                    + " VALUES (?,?,?)");
            clocking.setString(1, id);
            //1 for clockin, 2 for clockout
            switch (type){
                case "clockin":
                    clocking.setString(2, "1");
                    break;
                case "clockout":
                    clocking.setString(2, "2");
                    break;
                default:
                    System.out.println("Wrong Type");
                    break;
            }
            LocalDateTime now = LocalDateTime.now();
            clocking.setObject(3,now);
            clocking.executeUpdate();

            System.out.println("\nEmployee ID: "+id);
            System.out.println("Name: ");

            System.out.println("\nClock In " + Color.GREEN + "Successful!" + Color.RESET);
            System.out.println("Date: " + LocalDate.now());
            System.out.println("Time: " + LocalTime.now());


        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void EmployeeFunction(String UserID){
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Clock In");
        System.out.println("2. Log Out");
        System.out.println("3. ");
        System.out.print("Enter your choice (1-3): ");
        int choice = scanner.nextInt();
        System.out.println(" ");

        switch(choice){
            case 1 :
                Clocking(UserID,"clockin");
                break;
            case 2 :
                isLogin = false;
                break;
            case 3:

            default : System.out.println("Invalid choice");
        }

    }
}
