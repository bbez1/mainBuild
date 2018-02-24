/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tutorfind;

import com.tutorfind.Repositories.StudentRepository;
import com.tutorfind.Services.StudentService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.tutorfind.model.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Map;

@Controller
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

    @Qualifier("dataSource")
    @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }



//  @RequestMapping("/db")
//  String db(Map<String, Object> model) {
//    try (Connection connection = dataSource.getConnection()) {
//      Statement stmt = connection.createStatement();
//
//      ResultSet rs = stmt.executeQuery("SELECT * FROM users");
//
//      ArrayList<String> output = new ArrayList<String>();
//
//      while (rs.next()) {
//        output.add(rs.getString("email"));
//      }
//
//      model.put("records", output);
//      return "db";
//    } catch (Exception e) {
//      model.put("message", e.getMessage());
//      return "error";
//    }
//  }

    @RequestMapping("/db")
    String accessStudents(Map<String, Object> model,StudentRepository repository) {
       // StudentService studentService = new StudentService();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            // stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            //stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

           // ArrayList<String> output = new ArrayList<String>();

            //repository.save(new StudentDataModel("test"));
            while (rs.next()) {
                repository.save(new StudentDataModel(rs.getInt("userId"), rs.getString("legalFirstName"), rs.getString("legalLastName"),rs.getString("bio"), rs.getString("major"),rs.getString("minor"),rs.getString("img"),rs.getBoolean("active"),rs.getTimestamp("creationDate")));

                //output.add(rs.getString("email"));
            }
            List<StudentDataModel> output = repository.findAll();

            model.put("records", repository);
            //model.put("records", output);
            return "db";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }


  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
