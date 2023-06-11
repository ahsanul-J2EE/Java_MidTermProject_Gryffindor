# Table of Contents

1. [Project Description](#Project-Description)
2. [Installation](#Project-Installation-Guide)
3. [All Path](#All-Path)
4. [Contributors Details](#Contributors-Details)

## Project Description
The Online Book Shop micro-service project consists of six micro-services: API Gateway for authentication, Discovery Server for service identification, Cloud Config Server for configuration properties, Authentication Service for user authentication, Book Service for book management, and Book Inventory for inventory operations. The API Gateway ensures secure access to other micro-services. The Book Service allows the ADMIN to create, update, and delete books, while both ADMIN and USER can retrieve book information. The Book Inventory Service handles price and quantity updates, fetches, and deletions. The project follows design principles and promotes modularity, scalability, and fault tolerance in a distributed micro-service architecture.

# Project Installation Guide
This guide will walk you through the installation process of the Spring microservice project. Make sure you have the following prerequisites installed:

### Step 1: Clone the Repository
Clone the project repository from GitHub using the following command:

`git clone <repository_url>`

### Step 2: Database Configuration

    1. Make sure you have MySQL installed and running on your system. If not, you can download and install it from the [MySQL website](https://www.mysql.com/downloads/).

    2. Open the MySQL command-line client or a MySQL management tool of your choice.

    3. Log in to MySQL using the username and password provided. In this case, the username is `root` and the password is `root`.

    4. Create the required databases by executing the following SQL commands:

    ```sql
    CREATE DATABASE book_service;
    CREATE DATABASE inventory_service;
    CREATE DATABASE user-db;

    In the role table you have to manually entry the user role (ADMIN/USER).
    

    ### Step 2: Build the Project
    Navigate to the project directory and build the project using gradel:

        - Start Discovery Server.
        - Start Cloud Config Server.
        - Start API Gateway.
        - Start Authentication Service.
        - Start Book Service.
        - Start Book Inventory Service.




# All-Path

#### User:

To register a user
`http://localhost:9090/auth-server/register`

To login a user
`http://localhost:9090/auth-server/login`




#### Book Service:

To create a book
`http://localhost:9090/book-service/create`

To update a book by bookId
`http://localhost:9090/book-service/update/{bookId}`

To delete a book by bookId
`http://localhost:9090/book-service/delete/{bookId}`

To get all book
`http://localhost:9090/book-service/book/all`

To get book by bookId
`http://localhost:9090/book-service/book/{bookId}`

To buy a book
`http://localhost:9090/book-service/book/buy`




#### Inventory Service:

To update a book inventory info by bookId
`http://localhost:8082/book-inventory/update/{bookId}`

To get a book inventory info by bookId
`http://localhost:8082/book-inventory/{bookId}`

To get some book inventories info by providing a list of booKId
`http://localhost:8082/book-inventory`

To delete a book inventory info by bookId
`http://localhost:8082/book-inventory/{bookId}`



## 4. Contributors Details

Thank you to the following contributors who have helped to improve this project:

| Name          | Email           | Id           |
| ------------- | --------------- | --------------- |
| Arif Akmal| arif.akmal@bjitacademy.com|  30057  |
| Mohammad Ahsanul Islam |  ahsanul.islam@bjitacademy.com|  30073   |
| Khalid Shifullah|  khalid.shifullah@bjitacademy.com | 30062   |
| Mohammad Naimur Rahman    | naimur.rahman@bjitacademy.com | 30063   |

