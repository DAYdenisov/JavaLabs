import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DatabaseMediator
{
    private Connection connection;

    public boolean Connect(String userSystemName, String dataBaseName, String userName, String password)
    {
        boolean isConnected;
        try
        {
            connection = DriverManager.getConnection("jdbc:" + userSystemName + "://localhost:5432/" + dataBaseName,
                    userName,
                    password);
            connection.setAutoCommit(false);
            isConnected = true;
            System.out.println("Opened database successfully");
        } catch (SQLException e)
        {
            isConnected = false;
            System.out.println("Database connection is failed");
        }
        return isConnected;
    }

    public void Disconnect()
    {
        try
        {
            connection.close();
            System.out.println("Database disconnected successfully");
        } catch (SQLException e)
        {
            System.out.println("Database disconnect is failed");
        }
    }

    public boolean Insert(String tableName, String nameOfColumns, String values)
    {
        System.out.println("Start");
        boolean isInserted;
        String requestText = "insert into " + tableName + " (" + nameOfColumns + ") values (" + values + ");";
        try
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(requestText);
            statement.close();
            connection.commit();
            isInserted = true;
            System.out.println("New " + tableName + " has been added to the database");
        } catch (SQLException e)
        {
            isInserted = false;
            System.out.println(e);
            System.out.println("adding a new " + tableName + " to the database failed");
        }
        return isInserted;
    }

    public ArrayList<String> SelectFieldsFromTable(String tableName, LinkedList<String> allFields)
    {
        ArrayList<String> allObjectsValues = new ArrayList<String>();
        StringBuilder requestResult = new StringBuilder();
        String requestText = "select * from " + tableName + ";";
        try
        {
            Statement statement = connection.createStatement();
            ResultSet dataFromRequest = statement.executeQuery(requestText);
            while (dataFromRequest.next())
            {
                for (String field : allFields)
                {
                    requestResult.append(String.valueOf(dataFromRequest.getObject(field)).trim() + " ");
                }
                allObjectsValues.add(requestResult.toString());
                requestResult.setLength(0);
            }
            dataFromRequest.close();
            statement.close();
            connection.commit();
        } catch (SQLException e)
        {
            allObjectsValues.add("adding a new " + tableName + " to the database failed");
        }

        for (String obj : allObjectsValues)
            System.out.println(obj);

        return allObjectsValues;
    }

    public void DeleteAllFromTable(String tableName)
    {
        String requestText = "delete from " + tableName + ";";
        try
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(requestText);
            statement.close();
            connection.commit();
            System.out.println("Delete from " + tableName + " success");
        } catch (SQLException e)
        {
            System.out.println("Delete from " + tableName + " is failed");
        }
    }

    public void UpdateLineInTable(String tableName, LinkedList<String> allFields, String field, String newValue)
    {
        allFields.addFirst("id");
        SelectFieldsFromTable(tableName, allFields);
        Scanner in = new Scanner(System.in);
        System.out.print("Input id of updatable record: ");
        String id = in.nextLine();
        in.close();
        String requestText = "update " + tableName + " set " + field + " = '" + newValue + "' where id = " + id + ";";
        try
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(requestText);
            connection.commit();
            statement.close();
            System.out.println("Update in " + tableName + " success");
        } catch (SQLException e)
        {
            System.out.println("Update if " + tableName + " is failed");
        }
    }


    public void  InsertRow()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input type: ");
        String typeEmployee = scanner.nextLine();

        System.out.print("Input first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Input second name: ");
        String secondName = scanner.nextLine();

        System.out.print("Input salary: ");
        int salary = scanner.nextInt();

        System.out.print("Input product hours: ");
        int hours = scanner.nextInt();

        System.out.print("Input product department: ");
        String department = scanner.nextLine();

        System.out.print("\n");

        try {
                String sql = "INSERT INTO lab3 (typeEmployee, firstName, secondName, salary, hours, department) Values (?, ?, ?, ?, ?, ?)";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, typeEmployee);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, secondName);
                preparedStatement.setInt(4, salary);
                preparedStatement.setInt(5, hours);
                preparedStatement.setString(6, department);
                int rows = preparedStatement.executeUpdate();

                System.out.printf("%d rows added", rows);
                connection.commit();
                preparedStatement.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failed...");
        }
    }
}