## Blogging Web App

---

##### Non Authenticating URLs:

- `/register` - `POST` - Takes 3 form parameters ("full name". "email", "password") and registers the user in the database. Use Data Transfer Object (DTO) to send data to the controller.
- `/login` - `POST` - Takes 2 form parameters ("email", "password") and returns the user's JWT ("data" key genericSuccess(Object data)). Use the EntitiyHawk class provided in the util package.

##### Authenticating URLs:

_To access the following URLs, JWTs are required. Pass the JWT in the header as a bearer token._

- `/api/publish` - `POST`- Checks the received blog post's DTO and saves it in the database.
- `/api/getPost` - `GET` - Returns all the posts in the blog (returns the data as a genericSuccess(Object data) object with "data" keyword as the key).
- `/api/getPostCount` - `GET` - Returns the count of the number of posts in the blog ("data" key genericSuccess(Object data)).
- `/api/getPostByUser/{userld}` - `GET` - Returns all the posts published by a particular user.
- `/api/updatePost` - `POST` - Checks the received DTO for inputs and ensures that a post is updated only by the author of the post. Users cannot update posts authored by others.
- `/api/getPost/{postID}` - `GET` - Returns a post as per the post ID specified.
- `/api/deletePost/{postlD}` - `GET` - Deletes a post as per the post ID specified. Users cannot delete posts authored by others.

#### _Notes:_

> Use the DTO provided to handle the data flow in the application and to return the response of all the controllers.
> Use the EntitiyHawk class provided in the util package.
> Use H2 database, remove any other configuration like mysql config

#### To replace MySQL with H2 Database:

---

##### **_1. Add H2 dependdency in pom.xml_**

##### **_2. Comment MySQL config in application.yml_**

##### **_3. application.properties:_**

```
server.port=8000
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.initialization-mode=always
jwt.secret=secretkey
jwt.token.validity=900000
```

##### **_4. persistence.xml:_**

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
  <persistence-unit name="com.app_blog_jar_0.0.1-SNAPSHOTPU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <class>com.app.blog.models.Posts</class>
    <class>com.app.blog.models.Users</class>
    <properties>
      <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:testdb"/>
      <!-- <property name="javax.persistence.jdbc.user" value="root"/> -->
      <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
      <!-- <property name="javax.persistence.jdbc.password" value="mysql"/> -->
      <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
    </properties>
  </persistence-unit>
</persistence>
```

##### **_5. schema.sql:_**

```
DROP TABLE IF EXISTS `posts`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);

INSERT INTO `users` VALUES
(1,'Himalaya','himalaya.saxena@gmail.com','12345'),
(2,'Test User','test@gmail.com','12345'),
(3,'qwerty','qwerty@gmail.com','12345');

CREATE TABLE `posts` (
  `post_id` int(11) NOT NULL IDENTITY,
  `post_title` varchar(455) DEFAULT NULL,
  `post_body` longtext,
  `published_by` int(11) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  CONSTRAINT `userdId` FOREIGN KEY (`published_by`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
);

INSERT INTO `posts` VALUES
(1,'Post First Updated','Post body Updated',1,'2020-04-14 21:04:06','2020-04-17 15:04:46',0),
(2,'This is be body of my second blog post','My Second Post',1,'2020-04-16 11:55:48','2020-04-16 11:55:48',1),
(3,'titlee','bodydyyy',1,'2020-04-16 11:56:11','2020-04-17 15:31:56',0),
(4,'Title 34','Thhis is my 4th Blog body',1,'2020-04-17 15:01:36','2020-04-17 15:01:36',1),
(5,'This is blog title 5','This is blog body 5',1,'2020-04-17 15:29:53','2020-04-17 15:29:53',1),
(6,'Thats it','I am updated',2,'2020-04-17 16:26:10','2020-04-17 16:26:54',1),
(7,'title demo updated','demo body updated',1,'2020-04-17 17:27:05','2020-04-17 17:28:34',0);
```
