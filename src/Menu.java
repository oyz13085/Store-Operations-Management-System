import constant.Color;

import java.util.Scanner;

public class Menu {

    static void searchMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===Search Information ===");
        System.out.println("1. Search for Stock");
        System.out.println("2. Search for Sales");
        System.out.print("\nEnter your choice (1-2): ");
        int choice = scanner.nextInt();

        if(choice == 1){
            System.out.println("\n===Search Stock Information===");
            System.out.print("Search Model Name: ");
            scanner.nextLine();
            String model = scanner.nextLine();

            System.out.println(" ");
            System.out.println(Color.GREEN + "Searching..." + Color.RESET);

            Stock.searchStock(model);
        }else{
            System.out.println("\n===Search Sales Information===");
            System.out.println("Search Through: 1. Date 2. Customer Name 3. Model Name ");
            System.out.print("Enter Your Choice: ");
            choice = scanner.nextInt();

            System.out.print("Search for: ");
            scanner.nextLine();
            String search = scanner.nextLine();

            System.out.println(" ");
            System.out.println(Color.GREEN + "Searching..." + Color.RESET);

            Sales.searchSales(search,choice);
        }
    }

    static void editMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("===Edit Information ===");
        System.out.println("1. Edit Stock");
        System.out.println("2. Edit Sales");
        System.out.print("\nEnter your choice (1-2): ");
        int choice = scanner.nextInt();

        if(choice == 1){
            System.out.println("\n===Edit Stock Information===");
            System.out.print("Search Model Name: ");
            scanner.nextLine();
            String model = scanner.nextLine();

            Stock.editStock(model);

        }else{
            System.out.println("\n===Edit Sales Information===");
            System.out.print("Enter Transaction Date: ");
            scanner.nextLine();
            String date = scanner.nextLine();
            System.out.print("Enter Customer Name: ");
            String name = scanner.nextLine();


            System.out.println(" ");

            Sales.printOneSales(date,name);

            System.out.println("Edit: 1. Customer Name 2. Transaction Method ");
            System.out.print("Enter Your Choice: ");
            choice = scanner.nextInt();

            Sales.editSales(date,name,choice);
        }
    }
}
