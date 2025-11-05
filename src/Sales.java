import constant.Color;
import constant.SQL;
import constant.filePath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Sales {

    static DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
    static DecimalFormat df = new DecimalFormat("0.00");

//    public static void main(String[] args) {
//        //newSales("C6013");
//        //searchSales("Ali",1); need them to say find which type
//    }

    //havent do exception for situation if not enough stock (will do later on)
    public static void newSales(String UserID) {
        //name of customer
        //what is purchased
        //how much
        //transaction method
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.println("\nItem(s) Purchased: ");

        char answer;
        HashMap<String,Integer> itemsBought = new HashMap<>();
        double totalCost = 0;

        do {
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            System.out.printf("Unit Price: RM%.2f\n", Stock.getPricing(model));
            totalCost += Stock.getPricing(model) *  quantity;

            itemsBought.put(model, quantity);

            System.out.print("\nAre there more items purchased? (Y/N): ");
            answer = scanner.next().charAt(0);

            scanner.nextLine();

            System.out.println();

        }
        while (answer == 'Y');

        System.out.print("\nEnter Transaction Method: ");
        String transactionMethod = scanner.nextLine();

        System.out.printf("Subtotal: RM%.2f\n", totalCost);

        System.out.println("\nTransaction " + Color.GREEN + "Successful" + Color.RESET + ".");

        System.out.println("Sale Recorded " + Color.GREEN + "Successful" + Color.RESET + ".");
        generateSalesReceipt();

        for(String item : itemsBought.keySet()){
            Stock.updateStock(2,item,itemsBought.get(item));
            insertDB_Sales(customerName,UserID,item,itemsBought.get(item),transactionMethod,totalCost);
        }
        System.out.println("Model Quantities Updated " + Color.GREEN + "Successful" + Color.RESET + ".");

        writeSalesReceipt(UserID, customerName, itemsBought, totalCost, transactionMethod);

    }

    public static void generateSalesReceipt(){
        File receipt = new File(filePath.salesFile + LocalDate.now().format(date)+".txt");
    }

    public static void writeSalesReceipt(String UserID,String customerName, HashMap<String,Integer> itemsBought,double totalCost, String transactionMethod) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.salesFile + LocalDate.now().format(date)+".txt",true)) ){
            bw.newLine();
            bw.write("\n===Record New Sales===");
            bw.newLine();
            bw.write("Date: " + LocalDate.now().format(date));
            bw.newLine();
            bw.write("Time: " + LocalTime.now().format(time));
            bw.newLine();
            bw.write("Customer Name: " + customerName);
            bw.newLine();
            bw.write("Item(s) Purchased: ");
            bw.newLine();
            for(String i:itemsBought.keySet()){
                bw.write("  -"+ i +": "+itemsBought.get(i));
                bw.newLine();
            }
            bw.newLine();
            bw.write("Transaction Type: " + transactionMethod);
            bw.newLine();
            bw.write("Subtotal: RM" + df.format(totalCost));
            bw.newLine();
            bw.write("Transaction Done!");
            bw.newLine();
            bw.write("Employee in Charge:  " + Employee.getName(UserID));

            System.out.println("Receipt generated: Sales Record_" + LocalDate.now().format(date)+".txt");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void insertDB_Sales(String customerName, String UserID, String model, int quantity, String transactionMethod, double subtotal) {
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement addSalesRecord = connection.prepareStatement("INSERT INTO " + SQL.DB_Sales +
                            " (customer_name,employeeID,date, time, model, quantity, transaction_type, subtotal)" + "VALUES (?,?,?,?,?,?,?,?)"
                    );

            addSalesRecord.setString(1, customerName);
            addSalesRecord.setString(2, UserID);
            addSalesRecord.setString(3, LocalDate.now().format(date));
            addSalesRecord.setString(4, LocalTime.now().format(time));
            addSalesRecord.setString(5, model);
            addSalesRecord.setInt(6, quantity);
            addSalesRecord.setString(7, transactionMethod);
            addSalesRecord.setDouble(8, subtotal);

            addSalesRecord.execute();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void searchSales(String search, int type){
        String column;
        if(type == 1){
            column = "date";
        }else if(type == 2){
            column = "customer_name";
        }else{
            column = "model";
        }

        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement getRecord = connection.prepareStatement(
                    "SELECT * FROM " + SQL.DB_Sales +
                            " WHERE " + column +" = ?"
            );
            getRecord.setString(1, search);
            ResultSet resultSet = getRecord.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No Records Found");
            }

            String prevName = "",prevDate = "";

            while(resultSet.next()){
                String name = resultSet.getString("customer_name");
                String date = resultSet.getString("date");

                if(prevName.equals(name) && prevDate.equals(date) ){
                    continue;
                }

                printOneSales(name,date);
                prevName = name;
                prevDate = date;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void printOneSales(String name, String date){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement getRecord = connection.prepareStatement(
                    "SELECT * FROM " + SQL.DB_Sales +
                            " WHERE customer_name = ? AND date = ?"
            );

            getRecord.setString(1, name);
            getRecord.setString(2, date);

            ResultSet resultSet = getRecord.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("No Records Found");
            }

            resultSet.next();
            double total = 0;

            System.out.println("Sales Record Found:");
            System.out.println("Date: " + resultSet.getDate("date") + "     Time: " + resultSet.getTime("time"));
            System.out.println("Customer Name: " + resultSet.getString("customer_name"));
            System.out.println("Item(s) Purchased: ");

            while(true){
                System.out.println("    -" + resultSet.getString("model") + ": " + resultSet.getString("quantity"));
                total += Stock.getPricing(resultSet.getString("model")) *  resultSet.getInt("quantity");

                if(total ==  resultSet.getDouble("subtotal")){
                    break;
                }

                resultSet.next();
            }


            System.out.printf("Subtotal: RM%.2f\n", total);
            System.out.println("Transaction Method: " +  resultSet.getString("transaction_type"));
            System.out.println("Employee in Charge:  " + Employee.getName(resultSet.getString("employeeID")));
            System.out.println("Status: Transaction Verified!\n");


        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void editSales(String name, String date, int type){
        String column = "";
        String changed = "";
        String answer;
        Scanner scanner = new Scanner(System.in);

        while(true){
            switch(type){
                case 1:
                    column = "customer_name";
                    System.out.print("Enter New Customer Name: ");
                    changed = scanner.nextLine();
                    break;

                case 2:
                    column = "transaction_type";
                    System.out.print("\nEnter New Transaction Type: ");
                    changed = scanner.nextLine();
                    break;

                default:
                    System.out.println("Invalid Input");
                    continue;
            }

            System.out.print("\nConfirm Updates? (Y/N): ");
            answer = scanner.nextLine();
            if(answer.equalsIgnoreCase("Y")){
                break;
            }

        }

        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL,SQL.DB_Username,SQL.DB_Password);
            PreparedStatement updateStock = connection.prepareStatement(
                    "UPDATE " + SQL.DB_Sales +
                            " SET " + column + " = ? WHERE name = ? AND date = ?"
            );

            updateStock.setString(1, changed);
            updateStock.setString(2, name);
            updateStock.setString(3, date);

            updateStock.execute();

            System.out.println("Stock information updated " + Color.GREEN + "successfully" + Color.RESET + ".");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
