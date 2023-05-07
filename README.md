### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Modifications

- Java version : 8
- Use Spring Security to provide protection on all APIs
  - Username: `admin` / Password: `admin`
  - You should add Basic Auth during each API calls
- Use Caffeine cache to store employee info on `Get info by ID` API
  - Max size: `200` / expire time: `1 min`
  - `Update/Add/Delete` employee info api will update employ info in cache
- Add validation on `Employee entity` and `employId`
- Add unit several test cases
