package index;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetDBConnection {
    public static Connection getConnection(){
        Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://47.98.162.4:3306/fosu?serverTimezone=UTC","fosu","hyh010710" );
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到数据库驱动");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }

    public static void close(Connection con){
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("数据库关闭异常");
            }
        }
    }

}