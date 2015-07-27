package com.sampleApplication

import spock.lang.Specification
import groovy.sql.Sql
import static javax.ws.rs.core.Response.Status.ACCEPTED
import static javax.ws.rs.core.Response.Status.CONFLICT

class UserControllerTests extends Specification{

    String inMemoryDbConnectionString = "jdbc:h2:mem:inMemoryDb;DB_CLOSE_DELAY=-1"
    Sql sql
    UserController userController

    def "Ensure user is being persisted and user table is created initially when it doesn't exist"(){
        when:
            userController.addUser("SampleName","SamplePassword")

        then:
            def readUser = sql.firstRow("SELECT name, password FROM user")

            assert readUser.name == "SampleName"
            assert readUser.password == "SamplePassword"
    }

    def "Succeed with HTTP success code when adding new user"(){
        when:
            def response = userController.addUser("DuplicateName","DuplicateAttemptPassword")

        then:
            assert response.status == ACCEPTED.statusCode
    }

    def "Fail with HTTP conflict code when attempting to add duplicate users"(){
        given:
            userController.addUser("DuplicateName","SamplePassword")

        when:
            def response = userController.addUser("DuplicateName","DuplicateAttemptPassword")

        then:
            assert response.status == CONFLICT.statusCode
    }

    def "Allow addition of multiple users, ensure that user table is only created initially"(){
        when:
            userController.addUser("User1","password")
            userController.addUser("User2","password")
            userController.addUser("User3","password")

        then:
            assert sql.firstRow("SELECT COUNT(*) AS count FROM user").count == 3
    }

    def setup() {
        sql = Sql.newInstance(inMemoryDbConnectionString)
        sql.execute("DROP TABLE IF EXISTS user")
        userController = new UserController(dbConnectionString: inMemoryDbConnectionString)
    }

    def cleanup(){
        sql.close()
    }

}
