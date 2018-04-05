/*
Created by Adam Hardy
 */


package com.tutorfind.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("forgotpassword")
public class ForgotPasswordController {

    @Autowired
    private DataSource dataSource;

    private String isStudentEmailActive(String email){

        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT u.email FROM users u LEFT JOIN students s ON u.userid = s.userid WHERE u.email = ? AND s.active = true;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery(query);

            return query;

/*            if(rs.next()){
                return rs.getString("email");
                //reset password
                //send email
            }

            connection.close();*/

        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }

//        return "error";
    }


    @RequestMapping(value = "/student/{email}",method = RequestMethod.GET)
    public @ResponseBody String checkIfEmailIsActive(@PathVariable("email") String email){

        return isStudentEmailActive(email);
    }
}
