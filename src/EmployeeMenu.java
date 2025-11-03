import constant.Color;
import constant.SQL;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.Locale;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class EmployeeMenu {
    static Scanner scanner = new Scanner(System.in);
    public static boolean isLogin = true;

    public static void Clocking(String id){
        try{
            String type, word;
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement clocking = connection.prepareStatement("INSERT INTO " + SQL.DB_Clock + " (EmployeeID,ClockType,ClockTime)"
                    + " VALUES (?,?,?)");
            clocking.setString(1, id);
            //1 for clockin, 2 for clockout
            if(isClockOut(id)){
                type = "clockout";
            }else{
                type = "clockin";
            }
            switch (type){
                case "clockin":
                    clocking.setString(2, "1");
                    word = "Clock In";
                    break;
                case "clockout":
                    clocking.setString(2, "0");
                    word = "Clock Out";
                    break;
                default:
                    System.out.println("Wrong Type");
                    word = "";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
            LocalDateTime now = LocalDateTime.now();
            clocking.setObject(3,now);
            clocking.executeUpdate();

            System.out.println("===" + "Attendance " + word + " ===");
            System.out.println("\nEmployee ID: "+id);
            System.out.println("Name: ");

            if(type.equals("clockin")){
                System.out.println("\nClock In " + Color.GREEN + "Successful!" + Color.RESET);
            }else{
                System.out.println("\nClock Out " + Color.GREEN + "Successful!" + Color.RESET);

            }

            System.out.println("Date: " + LocalDate.now());
            System.out.println("Time: " + LocalTime.now().format(formatter));

            if(type.equals("clockout")){
                calculateTime(id);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void calculateTime(String id){
        try {
            Connection connection = DriverManager.getConnection(SQL.DB_URL, SQL.DB_Username, SQL.DB_Password);
            PreparedStatement getClockIn = connection.prepareStatement(
                    "SELECT * FROM  " + SQL.DB_Clock +
                            " WHERE EmployeeID = ?" +
                            " ORDER BY ClockID desc LIMIT 2"
            );

            getClockIn.setString(1, id);
            ResultSet resultSet = getClockIn.executeQuery();
            resultSet.next();
            LocalDateTime clockOut = (LocalDateTime) resultSet.getObject("ClockTime");
            resultSet.next();
            LocalDateTime clockIn = (LocalDateTime) resultSet.getObject("ClockTime");
            Duration duration = Duration.between(clockIn, clockOut);

            double totalHours = duration.toMinutes() / 60.0;
            System.out.printf("Total Hours Worked: %.2f hours\n",totalHours);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static boolean isClockOut(String id){
        try {
            Connection connection = DriverManager.getConnection(SQL.DB_URL, SQL.DB_Username, SQL.DB_Password);
            PreparedStatement checkClockIn = connection.prepareStatement(
                    "SELECT * FROM  " + SQL.DB_Clock +
                            " WHERE EmployeeID = ?" +
                            " ORDER BY ClockID desc LIMIT 1"
            );

            checkClockIn.setString(1, id);
            ResultSet resultSet = checkClockIn.executeQuery();
            if(!resultSet.next()){
                return false;
            }
            int result = resultSet.getInt("ClockType");
            if(result == 1){
                //clockin
                return true;
            }else{
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void EmployeeFunction(String UserID){
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Clock In/Clock Out");
        System.out.println("2. Clock Out");
        System.out.println("3. Log Out");
        System.out.print("Enter your choice (1-3): ");
        int choice = scanner.nextInt();
        System.out.println(" ");

        switch(choice){
            case 1 :
                Clocking(UserID);
                break;
            case 2 :


            case 3:
                isLogin = false;
                break;

            default : System.out.println("Invalid choice");
        }

    }
}
