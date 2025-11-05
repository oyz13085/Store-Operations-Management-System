import constant.Color;

import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        boolean running = true;

        //login page
        while(running){
            System.out.println("=== Employee Login ===");
            System.out.print("Enter User ID: ");
            String userID = input.nextLine();
            System.out.print("Enter Password: ");
            String password = input.nextLine();
            if(Employee.checkLogin(userID,password)){
                System.out.println("\nLogin " + Color.GREEN + "Successful" + Color.RESET + "!\n");

                if(Employee.Role.contains("Manager")){
                    while(ManagerMenu.isLogin){
                        ManagerMenu.displayMenu(userID);
                    }
                }else{
                    while(EmployeeMenu.isLogin){
                        EmployeeMenu.displayMenu(userID);
                    }
                }
            }else {
                System.out.println("\nLogin" + Color.RED + " Failed: Invalid ID or Password.\n" + Color.RESET);
            }
        }


    }

}

