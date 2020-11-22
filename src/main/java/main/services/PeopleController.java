package main.services;

import com.j256.ormlite.field.types.SqlDateType;
import main.PeopleException;
import main.db.DBConnect;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@RestController
public class PeopleController{
    private static final Logger logger = LogManager.getLogger();

    private Connection connection;

    @PostConstruct
    public void startup(){
        try {
            connection = DBConnect.connectToDB();
            logger.info("*** MySQL Connection created ***");
        } catch (SQLException | IOException e) {
            logger.error("***" + e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> postLogin(@RequestBody String credits) {
        String sessionId = "";
        try {
            JSONObject credentials = new JSONObject(credits);
            PreparedStatement st = null;
            ResultSet rows = null;
            int rows2;
            try {
                connection.setAutoCommit(true);
                st = connection.prepareStatement("select * from Users where username = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                st.setString(1, (String) credentials.get("username"));
                rows = st.executeQuery();
                rows.first();
                if(rows.getString("password").compareTo((String) credentials.get("password")) != 0){
                    return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
                }
                sessionId = generateRandomString(20);
                st = connection.prepareStatement("INSERT INTO Sessions (id, token) values (?, ?)");
                st.setInt(1, Integer.parseInt(rows.getString("id")));
                st.setString(2, sessionId);
                rows2 = st.executeUpdate();
                return new ResponseEntity<String>("{\"session_id\":\""+sessionId+"\"}", HttpStatus.valueOf(200));

            }catch(SQLException e1){
                throw new PeopleException(e1);
            }
        }catch(Exception e){
            logger.error(e);
        }
        return new ResponseEntity<String>("Error during login", HttpStatus.valueOf(403));
    }

    @GetMapping("/people")
    public ResponseEntity<String> fetchPeoples(@RequestHeader Map<String, String> headers) {
        String sessionToken = "";
        Set<String> keys = headers.keySet();
        for(String key : keys){
            if(key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
        }
        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Session Id Invalid or Missing", HttpStatus.valueOf(401));
        }

        JSONObject newJson = new JSONObject();
        String returnString = "";
        try {
            st = connection.prepareStatement("select * from People");
            rows = st.executeQuery();
            while(rows.next()) {
                newJson.put("id", rows.getString("id"));
                newJson.put("lastName", rows.getString("lastName"));
                newJson.put("firstName", rows.getString("firstName"));
                newJson.put("dateOfBirth", rows.getString("dateOfBirth"));
                newJson.put("age", rows.getString("age"));
                returnString += newJson.toString();
                returnString += ", ";
            }
            returnString = "[" + returnString+ "]";

            return new ResponseEntity<String>(returnString, HttpStatus.valueOf(200));

        } catch (SQLException e1){
            throw new PeopleException(e1);
        }
    }

    @GetMapping
    public ResponseEntity<String> fetchPerson(@RequestHeader Map<java.lang.String, java.lang.String> headers,
                                                   @PathVariable("personId") java.lang.String personId) {
        String sessionToken = "";
        JSONObject passJson = new JSONObject();
        Set<String> keys = headers.keySet();
        for(String key : keys){
            if(key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
        }

        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Session Id Invalid or Missing", HttpStatus.valueOf(401));
        }

        try {
            st = connection.prepareStatement("SELECT * FROM People where id = ?");
            st.setInt(1, Integer.parseInt(personId));
            rows = st.executeQuery();

            //Put rows into JSON
            passJson.put("id", rows.getString("id"));
            passJson.put("lastName", rows.getString("lastName"));
            passJson.put("firstName", rows.getString("firstName"));
            passJson.put("dateOfBirth", rows.getString("dateOfBirth"));
            passJson.put("age", rows.getString("age"));

        }catch(SQLException e1){
            System.out.print(e1);
            return new ResponseEntity<String>("Invalid Id", HttpStatus.valueOf(404));
        }
        ResponseEntity<String> response = new ResponseEntity<String>(passJson.toString(), HttpStatus.valueOf(200));
        return response;
    }

    @PutMapping("/people/{personId}")
    public ResponseEntity<String> updatePerson(@RequestHeader Map<String, String> headers,
        @PathVariable("personId") String personId) {
        String sessionToken = "";
        int user = 0;
        String username ="";
        boolean firstNameChanged = true, lastNameChanged = true, ageChanged = true, dateOfBirthChanged = true, idChanged = true;
        JSONObject passJson = new JSONObject();
        Set<String> keys = headers.keySet();
        for(String key : keys){
            if(key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
            if(key.equalsIgnoreCase("body"))
                passJson = new JSONObject(headers.get(key));
        }

        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            user = rows.getInt("id");
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
            st = connection.prepareStatement("SELECT * FROM Users WHERE id = ?");
            st.setInt(1, user);
            rows = st.executeQuery();
            rows.first();
            username = rows.getString("username");

        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid Id", HttpStatus.valueOf(404));
        }

        int rows2;

        try{
            //Get original data
            st = connection.prepareStatement("select * from People where id = ?");
            st.setString(1, personId);
            rows = st.executeQuery();
            rows.first();

            //Add in new Data
            if(!passJson.has("id")) {
                passJson.put("id", personId);
                idChanged = false;
            }
            if(!passJson.has("firstName")) {
                passJson.put("firstName", rows.getString("firstName"));
                firstNameChanged = false;
            }
            if(!passJson.has("lastName")) {
                passJson.put("lastName", rows.getString("lastName"));
                lastNameChanged = false;
            }
            if(!passJson.has("dateOfBirth")) {
                passJson.put("dateOfBirth", rows.getString("dateOfBirth"));
                dateOfBirthChanged = false;
            }
            if(!passJson.has("age")) {
                passJson.put("age", rows.getString("age"));
                ageChanged = false;
            }

            //Put total data
            connection.setAutoCommit(false);
            st = connection.prepareStatement("UPDATE People SET firstName = ?, lastName = ?, dateOfBirth = ?, age = ? WHERE id = ?");
            st.setInt(5, Integer.parseInt((String) passJson.get("id")));
            st.setString(2, (String) passJson.get("lastName"));
            st.setString(1, (String) passJson.get("firstName"));
            LocalDate localDate = LocalDate.parse((String) passJson.get("dateOfBirth"));
            st.setDate(3, java.sql.Date.valueOf( localDate ));
            st.setInt(4, Integer.parseInt((String) passJson.get("age")));
            rows2 = st.executeUpdate();

            //Audit Trail commit for all possible changes
            if(ageChanged == true) {
                st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
                st.setInt(1, 0);
                st.setString(2, "age changed from " + rows.getString("age") + " to " + passJson.get("age"));
                st.setString(3, username);
                st.setInt(4, Integer.parseInt((String) personId));
                rows2 = st.executeUpdate();
            }
            if(lastNameChanged == true) {
                st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
                st.setInt(1, 0);
                st.setString(2, "lastName changed from " + rows.getString("lastName") + " to " + passJson.get("lastName"));
                st.setString(3, username);
                st.setInt(4, Integer.parseInt((String) personId));
                rows2 = st.executeUpdate();
            }
            if(firstNameChanged == true) {
                st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
                st.setInt(1, 0);
                st.setString(2, "firstName changed from " + rows.getString("firstName") + " to " + passJson.get("firstName"));
                st.setString(3, username);
                st.setInt(4, Integer.parseInt((String) personId));
                rows2 = st.executeUpdate();
            }
            if(dateOfBirthChanged == true) {
                st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
                st.setInt(1, 0);
                st.setString(2, "dateOfBirth changed from " + rows.getString("dateOfBirth") + " to " + passJson.get("dateOfBirth"));
                st.setString(3, username);
                st.setInt(4, Integer.parseInt((String) personId));
                rows2 = st.executeUpdate();
            }
            if(idChanged == true) {
                st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
                st.setInt(1, 0);
                st.setString(2, "id changed from " + rows.getString("id") + " to " + passJson.get("id"));
                st.setString(3, username);
                st.setInt(4, Integer.parseInt((String) personId));
                rows2 = st.executeUpdate();
            }
            connection.commit();

        } catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid Id", HttpStatus.valueOf(404));
        }
        return new ResponseEntity<String>("Update Successful", HttpStatus.valueOf(200));
    }

    @DeleteMapping("/people/{personId}")
    public ResponseEntity<String> deletePerson(@RequestHeader Map<String, String> headers, @PathVariable("personId") String personId) {
        String sessionToken = "";
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            if (key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
        }

        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Session Id Invalid or Missing", HttpStatus.valueOf(401));
        }

        int rows2;
        try {
            st = connection.prepareStatement("DELETE FROM People WHERE id = ?");
            st.setInt(1, Integer.parseInt(personId));
            rows2 = st.executeUpdate();

        } catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid Id", HttpStatus.valueOf(404));
        }
        return new ResponseEntity<String>("Person Deleted", HttpStatus.valueOf(200));
    }

    @PostMapping("/people")
    public ResponseEntity<String> insertPerson(@RequestHeader Map<String, String> headers) {
        String sessionToken = "";
        int user = 0;
        String username = "";
        JSONObject insertUser = new JSONObject();
        Set<String> keys = headers.keySet();
        for(String key : keys){
            if(key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
            if(key.equalsIgnoreCase("body"))
                insertUser = new JSONObject(headers.get(key));
        }
        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            user = rows.getInt("id");
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
            st = connection.prepareStatement("SELECT * FROM Users WHERE id = ?");
            st.setInt(1, rows.getInt("id"));
            rows = st.executeQuery();
            rows.first();
            username = rows.getString("username");

        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Session Id Invalid or Missing", HttpStatus.valueOf(401));
        }

        int rows2;
        try {
            connection.setAutoCommit(false);
            st = connection.prepareStatement("INSERT INTO People (id, lastName, firstName, dateOfBirth, age) values (?, ?, ?, ?, ?)");
            st.setInt(1, (int) insertUser.get("id"));
            st.setString(2, (String) insertUser.get("lastName"));
            st.setString(3, (String) insertUser.get("firstName"));
            LocalDate localDate = LocalDate.parse((String) insertUser.get("dateOfBirth"));
            st.setDate(4, java.sql.Date.valueOf( localDate ));
            st.setInt(5, (int) insertUser.get("age"));

            rows2 = st.executeUpdate();
            //Audit Trail commit
            st = connection.prepareStatement("INSERT INTO Audit (id, change_msg, changed_by, person_id) values(?, ?, ?, ?)");
            st.setInt(1, 0);
            st.setString(2, "added");
            st.setString(3, username);
            st.setInt(4, (int) insertUser.get("id"));
            rows2 = st.executeUpdate();
            connection.commit();

        }catch (SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid Id", HttpStatus.valueOf(404));
        }

        return new ResponseEntity<String>("Person Inserted Successfully", HttpStatus.valueOf(200));
    }

    @GetMapping("/people/{personId}/audittrail")
    public ResponseEntity<String> getAudit(@RequestHeader Map<String, String> headers, @PathVariable("personId") String personId) {
        String sessionToken = "";
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            if (key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
        }
        PreparedStatement st = null;
        ResultSet rows = null;
        try {
            //Check token
            connection.setAutoCommit(true);
            st = connection.prepareStatement("SELECT * FROM Sessions WHERE token = ?");
            st.setString(1, sessionToken);
            rows = st.executeQuery();
            rows.first();
            if(rows.getString("token").compareTo(sessionToken) != 0){
                return new ResponseEntity<String>("Invalid User Credentials", HttpStatus.valueOf(401));
            }
        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid Body", HttpStatus.valueOf(400));
        }

        JSONObject newJson = new JSONObject();
        String returnString = "";
        try {
            st = connection.prepareStatement("SELECT * FROM Audit WHERE person_id = ?");
            st.setInt(1, Integer.parseInt(personId));
            rows = st.executeQuery();
            while (rows.next()) {
                newJson.put("id", rows.getString("id"));
                newJson.put("change_msg", rows.getString("change_msg"));
                newJson.put("changed_by", rows.getString("changed_by"));
                newJson.put("person_id", rows.getString("person_id"));
                newJson.put("when_occurred", rows.getString("when_occurred"));
                returnString += newJson.toString();
                returnString += ", ";
            }
            returnString = "[" + returnString + "]";

            return new ResponseEntity<String>(returnString, HttpStatus.valueOf(200));
        }catch(SQLException e1){
            System.out.println(e1);
            return new ResponseEntity<String>("Invalid SQLRequest", HttpStatus.valueOf(404));
        }
    }

    @PreDestroy
    public void cleanup(){
        try {
            connection.close();
            logger.info("*** MySQL connection closed");
        } catch (SQLException e) {
            logger.error("*** " + e);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public String generateRandomString(int n){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for(int i=0;i<n;i++){
            int index = (int) (characters.length() * Math.random());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

}