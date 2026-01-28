package com.upb.agripos.dao;
import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.User;
import java.sql.*;
public class UserDAO {
    public User login(String u,String p){
        try(PreparedStatement ps=DatabaseConnection.getConnection()
        .prepareStatement("SELECT * FROM users WHERE username=? AND password=?")){
            ps.setString(1,u); ps.setString(2,p);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) return new User(rs.getString("username"),rs.getString("role"));
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
}
