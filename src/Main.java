import constant.Color;

import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
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

                if(Employee.Role.contains("Manager")){
                    while(ManagerMenu.isLogin){
                        managerMenu.ManagerFunction(userID);
                    }
                }else{
                    while(EmployeeMenu.isLogin){
                        employeeMenu.EmployeeFunction(userID);
                    }
                }
            }else {
                System.out.println("\nLogin" + Color.RED + " Failed: Invalid ID or Password.\n" + Color.RESET);
            }
        }


    }

}

