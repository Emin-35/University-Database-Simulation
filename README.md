
# University-Database-Simulation

In the development of our Android application using Android Studio, Java, and SQLite; we successfully created a management of university-related database and this database is divided into three main tabs: Administration, Registration, and Registered Students.

The Administration Tab: is the backbone of the system, where data related to Faculties, Departments, and Lecturers is inputted and managed. It provides a snapshot of the university's general information. The tab is equipped with functional buttons such as "add", "delete", "update", and "search" to ensure efficient data management.

The Registration Tab is dedicated to the registration of students. It requires various details such as a 10-digit Student ID (generated randomly), gender (options available via radio buttons), name, last name, faculty, department, and advisor. The last three fields are pre-defined in the Administration tab, ensuring consistency across the system. To complete the registration process, there are buttons such as "register", "cancel", "update", and "search" available.

Finally, the Registered Students Tab displays a list of all registered students. Clicking on a student's name triggers a pop-up frame (or a message dialog box) that presents all the student's information.





## How to use the database

#### Adding Faculty

```
  Faculty Table
```

| Faculty_ID | Faculty_Name 
| :-------- | :------- | 
| `P.K, Auto Increment` | `string` |

#### Adding Department

```
  Department Table
```

| Department_ID | Department_Name | Faculty_ID |
| :-------- | :------- | :------- | 
| `P.K, Auto Increment` | `string` | `F.K, connected to Faculty_ID` |

#### Adding Lecturer

```
  Lecturer Table
```

| Lecturer_ID | Lecturer_Name | Department_ID |
| :-------- | :------- | :------- | 
| `P.K, Auto Increment` | `string` | `F.K, connected to Department_ID` |

By using these 3 tables we can add Faculty, Department that is related to the Faculty and Lecturer that is going to work in the created Department.

We can either add Faculty, Department and Lecturer at the same time or we can first create Faculty, Then add Department and Lecturer in it.

There can be multiple Departments inside a single Faculty and multiple Lecturers inside a single department.

Same lecturer can work in different departments but same departments can not be in different faculties

## Tools and Tech.

**Platform:** Android Studio

**Language:** Java

**Database:** SQLite
  
