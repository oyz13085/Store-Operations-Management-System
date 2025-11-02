import constant.Color;

import java.util.Scanner;

public class Main {
    static Employee employee = new Employee();
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        boolean isLogin = true;
        boolean running = true;

        Employee employee = new Employee();
        ManagerMenu managerMenu = new ManagerMenu();
        EmployeeMenu employeeMenu = new EmployeeMenu();
        employee.printDetails("C6013");

        //login page
        while(running){
            System.out.println("=== Employee Login ===");
            System.out.print("Enter User ID: ");
            String userID = input.nextLine();
            System.out.print("Enter Password: ");
            String password = input.nextLine();
            if(employee.checkLogin(userID,password)){
                System.out.println("\nLogin " + Color.GREEN + "Successful" + Color.RESET + "!\n");

                //if manager -> manager menu, employee -> employee menu
                while(managerMenu.isLogin){
                    managerMenu.ManagerFunction(userID);
                }
                while(employeeMenu.isLogin){
                    employeeMenu.EmployeeFunction(userID);
                }

            }else {
                System.out.println("\nLogin" + Color.RED + " Failed: Invalid ID or Password.\n" + Color.RESET);
            }
        }


    }
        //login and logout/registration for employees

        //working hours(clock in and clock out)

}

