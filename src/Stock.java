
import constant.Color;
import constant.SQL;
import constant.filePath;

import java.io.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stock {

    static Scanner scanner = new Scanner(System.in);

    static DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

//    public static void main(String[] args) {
//        //StockCount();
//        //writeStockReceipt();
//        //moveStock("C6013");
//        //System.out.println(getPricing("DW2300-1"));
//        //editStock("DW2300-1");
//
//    }

    public static void stockSystem(String UserID){
        System.out.println("===Stock Management===");
        System.out.println("1. Stock Count");
        System.out.println("2. Stock In/Stock Out");
        System.out.print("Enter your choice (1-2): ");
        int choice = scanner.nextInt();

        if(choice == 1){
            Stock.StockCount();
        }else if(choice == 2){
            Stock.moveStock(UserID);
        }else{
            System.out.println("Invalid choice");
        }
    }

    public static void StockCount(){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement getCount = connection.prepareStatement(
                    "SELECT * FROM " + SQL.DB_Stock);
            ResultSet resultSet = getCount.executeQuery();

            String titleWord;
            int noModel = 0;
            int noCorrect = 0;

            //morning or night count
            System.out.println("\n===Stock Count===");
            System.out.println("1. Morning Stock Count");
            System.out.println("2. Night Stock Count");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            titleWord = (choice==1) ? "Morning Stock Count":"Night Stock Count";

            System.out.println("\n=== " + titleWord + " ===");
            System.out.println("Date: " + LocalDate.now().format(date));
            System.out.println("Time: " + LocalTime.now().format(time));

            while(resultSet.next()){
                String model = resultSet.getString("model");
                int stock = resultSet.getInt("stock");

                System.out.println("\nModel: " + model + " - Counted: " + stock);
                System.out.print("Store Record: ");
                int record = scanner.nextInt();

                if(record!=stock){
                    System.out.println(Color.RED + "! Mismatch detected (" + Math.abs((stock-record)) +" unit difference)" + Color.RESET);
                }else{
                    System.out.println(Color.GREEN + "Stock tally correct. " + Color.RESET);
                    noCorrect+=1;
                }

                noModel += 1;
            }

            System.out.println("\nTotal Models Checked: " + noModel);
            System.out.println("Tally " + Color.GREEN + "Correct" + Color.RESET + ": " + noCorrect);
            System.out.println(Color.RED + "Mismatches" + Color.RESET + ": " + (noModel - noCorrect));
            System.out.println(titleWord + Color.GREEN + " Completed" + Color.RESET + ".");

            if(noModel != noCorrect){System.out.println(Color.RED + "Warning: Please verify stock. " + Color.RESET);}

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void generateStockReceipt(){
        File receipt = new File(filePath.stockFile + LocalDate.now().format(date)+".txt");
    }

    public static void writeStockReceipt(int type, String from, String to, HashMap<String, Integer> stock, int totalQuantity, String userID){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.stockFile + LocalDate.now().format(date)+".txt",true)) ){
            bw.newLine();
            bw.write("\n==="+((type==1)?"Stock In":"Stock Out")+"===");
            bw.newLine();
            bw.write("Date: " + LocalDate.now().format(date));
            bw.newLine();
            bw.write("Time: " + LocalTime.now().format(time));
            bw.newLine();
            bw.write("From: " + from);
            bw.newLine();
            bw.write("To: " + to);
            bw.newLine();
            bw.write((type==1)?"\nModels Received:":"\nModels Sent:");
            bw.newLine();
            for(String i: stock.keySet()){
                bw.write("    - " + i + " (Quantity: "+stock.get(i)+")");
                bw.newLine();
            }
            bw.write("Total Quantity: " + totalQuantity);
            bw.newLine();
            bw.newLine();
            bw.write("Employee in Charged:  " + Employee.getName(userID));

            System.out.println("Receipt generated: Stock Record_" + LocalDate.now().format(date)+".txt");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void moveStock(String userID){
        System.out.println("1. Stock In");
        System.out.println("2. Stock Out");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        System.out.println("\n==="+((choice==1)?"Stock In":"Stock Out")+"===");
        System.out.println("Date: " + LocalDate.now().format(date));
        System.out.println("Time: " + LocalTime.now().format(time));

        System.out.print("From: ");
        String fromOutlet = scanner.next();

        System.out.print("To: ");
        String toOutlet = scanner.next();

        System.out.println((choice==1)?"\nModels Received:":"\nModels Sent:");

        int totalQuantity = 0;
        HashMap<String, Integer> stockUpdated = new HashMap<>();

        while(true) {
            System.out.print("Model Name: ");
            String model = scanner.next();

            System.out.print("Quantity: ");
            int quantity = scanner.nextInt();

            totalQuantity += quantity;

            updateStock(choice,model,quantity);

            if(stockUpdated.containsKey(model)){
                stockUpdated.put(model, stockUpdated.get(model) + quantity);
            }else{
                stockUpdated.put(model, quantity);
            }

            System.out.print("\nContinue (Y/N): ");
            char answer =  scanner.next().charAt(0);

            if(answer != 'Y'){
                break;
            }

        }

        if(!stockUpdated.isEmpty()){
            System.out.println((choice==1)?"\nModels Received:":"\nModels Sent:");

            //print out all updated models and quantity
            for(String i: stockUpdated.keySet()){
                System.out.println("    - " + i + " (Quantity: "+stockUpdated.get(i)+")");
            }

            System.out.println("Total Quantity: " + totalQuantity);

            System.out.println("\nModel quantities updated " + Color.GREEN + "successfully" + Color.RESET + ".");
            System.out.println(((choice==1)?"Stock In":"Stock Out") + Color.GREEN + " recorded" + Color.RESET + ".");

            generateStockReceipt();
            writeStockReceipt(choice,fromOutlet,toOutlet,stockUpdated,totalQuantity,userID);
        }else{
            System.out.println(Color.RED + "No stock updated." + Color.RESET);
        }

        //hashmap for code of outlet

    }

    public static void updateStock(int choice, String modelName, int quantity){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement updateCount;
            if(choice==1){
                updateCount = connection.prepareStatement("UPDATE " + SQL.DB_Stock +
                        " SET stock = stock + ?" +
                        " WHERE model = ?");
            }else{
                updateCount = connection.prepareStatement("UPDATE " + SQL.DB_Stock +
                        " SET stock = stock - ?" +
                        " WHERE model = ?");
            }

            updateCount.setInt(1, quantity);
            updateCount.setString(2, modelName);

            updateCount.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static double getPricing(String modelName){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement getInfo = connection.prepareStatement("SELECT * FROM " + SQL.DB_Stock +
                    " WHERE model = ?");
            getInfo.setString(1, modelName);

            ResultSet resultSet = getInfo.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return 0;
            }

            resultSet.next();
            return resultSet.getDouble("unitprice");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void searchStock(String model){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement getRecord = connection.prepareStatement(
                    "SELECT * FROM " + SQL.DB_Stock +
                            " WHERE model = ?"
            );

            getRecord.setString(1, model);

            ResultSet resultSet = getRecord.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No Records Found");
            }

            resultSet.next();

            System.out.println("Model Record Found: ");

            System.out.print("Model Name: " + resultSet.getString("model"));
            System.out.printf("\nUnit Price: RM%.2f", resultSet.getDouble("unitprice"));

            System.out.println("\nStock by Outlet:");
            System.out.print(resultSet.getString("stock"));

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void editStock(String model){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);

            PreparedStatement getStock = connection.prepareStatement("SELECT * FROM " + SQL.DB_Stock +
                    " WHERE model = ?");
            getStock.setString(1, model);

            ResultSet resultSet = getStock.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No Records Found");
            }

            resultSet.next();

            System.out.println("Current Stock: " + resultSet.getString("stock"));

            System.out.print("Enter New Stock Value: ");
            int newStock = scanner.nextInt();

            PreparedStatement editStockLevel = connection.prepareStatement(
                    "UPDATE " + SQL.DB_Stock +
                            " SET stock = ? WHERE model = ?"
            );

            editStockLevel.setInt(1, newStock);
            editStockLevel.setString(2, model);

            editStockLevel.executeUpdate();

            System.out.println("Stock information updated " + Color.GREEN + "successfully" + Color.RESET + ".");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
