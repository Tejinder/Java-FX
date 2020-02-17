import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 

public class JdbcDerbyConnection {
 
	private static  String dbURL = "jdbc:derby://localhost:1527/employeedb;create=true";
    private static String tableName = "employee";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

	/*
	public static void main(String[] args) {
         
        try {
            // connect method #1 - embedded driver
        	String dbURL = "jdbc:derby:employeedb;create=true;";
            Connection conn1 = DriverManager.getConnection(dbURL);
            Statement stmt = null;

            if (conn1 != null) {
                System.out.println("Connected to database #1");
                
                try
                {
                    stmt = conn1.createStatement();
                    ResultSet results = stmt.executeQuery("select * from employee");
                    ResultSetMetaData rsmd = results.getMetaData();
                    int numberCols = rsmd.getColumnCount();
                    for (int i=1; i<=numberCols; i++)
                    {
                        //print Column Names
                        System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
                    }

                    System.out.println("\n-------------------------------------------------");

                    while(results.next())
                    {
                        int id = results.getInt(1);
                        String restName = results.getString(2);
                        String cityName = results.getString(3);
                        System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
                    }
                    results.close();
                    stmt.close();
                }
                catch (SQLException sqlExcept)
                {
                    sqlExcept.printStackTrace();
                }
            }
             
            // connect method #2 - network client driver
            String dbURL2 = "jdbc:derby://localhost:1527/employeedb;create=true";
            String user = "tom";
            String password = "dtee";
            Connection conn2 = DriverManager.getConnection(dbURL2);
            if (conn2 != null) {
                System.out.println("Connected to database #2");
                try
                {
                    stmt = conn2.createStatement();
                    ResultSet results = stmt.executeQuery("select * from employee");
                    ResultSetMetaData rsmd = results.getMetaData();
                    int numberCols = rsmd.getColumnCount();
                    for (int i=1; i<=numberCols; i++)
                    {
                        //print Column Names
                        System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
                    }

                    System.out.println("\n-------------------------------------------------");

                    while(results.next())
                    {
                        int id = results.getInt(1);
                        String restName = results.getString(2);
                        String cityName = results.getString(3);
                        System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
                    }
                    results.close();
                    stmt.close();
                }
                catch (SQLException sqlExcept)
                {
                    sqlExcept.printStackTrace();
                }
            }
 
            // connect method #3 - network client driver
            String dbURL3 = "jdbc:derby://localhost/webdb3";
            Properties properties = new Properties();
            properties.put("create", "true");
            properties.put("user", "tom");
            properties.put("password", "secret");
             
            Connection conn3 = DriverManager.getConnection(dbURL3, properties);
            if (conn3 != null) {
                System.out.println("Connected to database #3");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    */
    
    private static void createConnection()
    {
        try
        {
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    public static void insertEmployee(String name, String department, String position)
    {
    	
    	if(conn==null)
    	{
    		createConnection();
    	}
        try
        {
            stmt = conn.createStatement();
            stmt.execute("insert into " + tableName + "(name, department, position) values ('" +
            		name + "','" + department + "','" + position +"')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        
    }
    
    public static List<Employee> getAllEmployees()
    {
    	
    	List<Employee> employeeList = new ArrayList<Employee>();
    	if(conn==null)
    	{
    		createConnection();
    	}
    	try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from employee");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String name = results.getString(2);
                String department = results.getString(3);
                String position = results.getString(4);

                System.out.println(id + "\t\t" + name + "\t\t" + department + "\t\t" + position);
                Employee employee = new Employee();
                employee.setId(id);
                employee.setName(name);
                employee.setDepartment(department);
                employee.setPosition(position);
                employeeList.add(employee);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	return employeeList;
        
    }
    
    
    public static void updateEmployeeName(String name, Integer employeeid)
    {
    
    	
    	if(conn==null)
    	{
    		createConnection();
    	}
    	try
        {
            stmt = conn.createStatement();
            int record = stmt.executeUpdate("update employee set name = '"+name + "' where id = "+employeeid);
            
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	
        
    }
    
    public static void updateEmployeeDepartment(String department, Integer employeeid)
    {
    	
    	
    	if(conn==null)
    	{
    		createConnection();
    	}
    	try
        {
            stmt = conn.createStatement();
            int record = stmt.executeUpdate("update employee set department = '"+department + "' where id = "+employeeid);
            
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	
        
    }
    
    public static void updateEmployeePosition(String position, Integer employeeid)
    {
    	
    	if(conn==null)
    	{
    		createConnection();
    	}
    	try
        {
            stmt = conn.createStatement();
            int record = stmt.executeUpdate("update employee set position = '"+position + "' where id = "+employeeid);
            
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    	
        
    }
}