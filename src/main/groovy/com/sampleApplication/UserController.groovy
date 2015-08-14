package com.sampleApplication
import groovy.sql.Sql

import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.sql.DriverManager
import java.sql.SQLException

import static javax.ws.rs.core.Response.Status.ACCEPTED
import static javax.ws.rs.core.Response.Status.CONFLICT

@Path("/user")
class UserController {

    String dbConnectionString = "jdbc:h2:~/kata1-problem-database"

    static{
        DriverManager.registerDriver(new org.h2.Driver())
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Response addUser(@FormParam("name") String name, @FormParam("password") String password){
        try {
            Sql.withInstance(dbConnectionString) {
                it.execute("CREATE TABLE IF NOT EXISTS user (name VARCHAR PRIMARY KEY, password VARCHAR)");
                it.execute("INSERT INTO user (name, password) VALUES ($name, $password)");
            }
            Response.status(ACCEPTED).build()
        } catch (SQLException exception) {
            Response.status(CONFLICT).entity("Duplicate Key Exception").build()
        }
    }
}