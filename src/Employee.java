import com.mysql.cj.x.protobuf.MysqlxPrepare;
import constant.Color;
import constant.SQL;

import java.sql.*;

public class Employee {
    static SQL sql = new SQL();

    public void printDetails(String id){
        try{
            Connection connection = DriverManager.getConnection(sql.DB_URL,
                    sql.DB_Username,
                    sql.DB_Password);

            PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM " + sql.DB_Employee +
                    " WHERE EmployeeID = ?");

            getDetails.setString(1, id);
            ResultSet resultSet = getDetails.executeQuery();
            if(!resultSet.isBeforeFirst()){
                System.out.println("Employee Not Found");
            }
            while(resultSet.next()){
                System.out.println(resultSet.getString("EmployeeID"));
                System.out.println(resultSet.getString("EmployeeName"));
                System.out.println(resultSet.getString("Role"));
                System.out.println(resultSet.getString("Password"));

            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String id,String password){
        try{
            Connection connection = DriverManager.getConnection(
                    sql.DB_URL,
                    sql.DB_Username,
                    sql.DB_Password
            );

            PreparedStatement getDetails = connection.prepareStatement("SELECT * FROM " + sql.DB_Employee +
                    " WHERE EmployeeID = ? AND Password = ?");
            getDetails.setString(1, id);
            getDetails.setString(2, password);
            ResultSet resultSet = getDetails.executeQuery();
            if(!resultSet.isBeforeFirst()){
                return false;
            }
            resultSet.next();
            resultSet.getString("EmployeeID");
            resultSet.getString("password");
            if(resultSet.getString("password").equals(password)){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void registerEmployee(String name,String id,String password,String role){
        try{
            Connection connection = DriverManager.getConnection(SQL.DB_URL ,
                    SQL.DB_Username ,
                    SQL.DB_Password);
            PreparedStatement insertDetails = connection.prepareStatement("INSERT INTO " + sql.DB_Employee +
                    " (EmployeeID,EmployeeName,Password,Role) VALUES (?,?,?,?)");
            insertDetails.setString(1, id);
            insertDetails.setString(2, name);
            insertDetails.setString(3, password);
            insertDetails.setString(4, role);
            insertDetails.executeUpdate();
            insertDetails.close();

            System.out.println("\nEmployee " + Color.GREEN +"successfully registered!"+Color.RESET);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}

