package main.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class DBProps {

    public static void main(String [] args) {
        Connection conn = null;

        //connect to data source and create a connection instance
        //read db credentials from properties file
        Properties props = new Properties();
        BufferedInputStream propsFile = null;
        try {
            //read db credentials from properties file
            propsFile = (BufferedInputStream) DBProps.class.getResourceAsStream("/db.properties");
            props.load(propsFile);
            propsFile.close();

            //create the datasource
            MysqlDataSource ds = new MysqlDataSource();
            ds.setURL(props.getProperty("MYSQL_DB_URL"));
            ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));

            //create the connection
            conn = ds.getConnection();

            // insert a dog record
            PreparedStatement st = conn.prepareStatement("insert into dogs (dog_name, age) values (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, "Ralph");
            st.setInt(2, 7);
            st.executeUpdate();
            ResultSet newKeys = st.getGeneratedKeys();
            newKeys.first();

            // set the dog's id as the primary key returned from the db
            System.out.println("new dog id is " + newKeys.getInt(1));

            newKeys.close();
            st.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
