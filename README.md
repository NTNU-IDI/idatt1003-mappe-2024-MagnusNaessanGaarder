[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=16241657&assignment_repo_type=AssignmentRepo)
[![Review Assignment Due Date]([x]https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/INcAwgxk)
# Portfolio project IDATT1003
This file uses Mark Down syntax. For more information see [here]([x]https://www.markdownguide.org/basic-syntax/).
[!E-portofolio](https://sites.google.com/view/magnusnaessangaarder/om-meg?authuser=0)
[!Assignments 1-11 IDATT1003](https://github.com/MagnusNaessanGaarder/IDATT1003_ProgrammeringOvinger)

[//]: # Name and student ID

STUDENT NAME = "Magnus Næssan Gaarder"  
STUDENT ID = ""

## Project description

This project is a simple Text-based User Interface(TUI) that can be used to manage and keep track of a collection of 
Groceries in a foodstorage. With this application you can add, remove and update groceries and add or remove them from 
a foodstorage. This project focuses on sustainable development and the goal is to reduce food waste by keeping track 
of the groceries in the foodstorage. On that regard, the application have a feature that sorts stored groceries in three 
categories: "Expired", "About to expire" and "Not expired". 
This is to notify the user when a grocery is about to expire.

The application also includes features to add, remove or make a recipes for a cook book. When the user makes a dish,
from a given recipe, the groceries used in the dish will be removed from the foodstorage.

Another focus of this project is to make the application user-friendly and easy to use. The application therefore 
focuses on structuring content in a way that is easy to understand and use. The application also includes a commands, 
explained in texts in the application, that the user can use to navigate the application. This is ment to helps to 
condence the menu and make it simpler and easier for the user to use.

## Project structure

The main Java sourcefiles for the application is located in the src-folder (src/main/java/edu/ntnu/idi/idatt).
In this folder there are three packages: "manager", "modules" and "utils" as well as the main sourcefile for the 
application, StorageApplication.java. 
The "manager"-package contains the classes that manages the groceries and recipes in the
application. The "modules"-package contains the classes that represents the groceries and recipes in the application.
The "utils"-package contains the classes that are used to help the application run smoothly.

The JUnit-test classes for the application is located in the test-folder (src/test/java/edu/ntnu/idi/idatt).
In this folder there are two packages: "manager" and "modules". The "manager"-package contains the JUnit-test classes
for the classes in the "manager"-package in the src-folder. The "modules"-package contains the JUnit-test classes for
the classes in the "modules"-package in the src-folder. JUnit-tests for the manager-package was integrated to ensure
testing of the other important features for the modules-package classes.

## https://github.com/NTNU-IDI/idatt1003-mappe-2024-MagnusNaessanGaarder

## How to run the project

The main class of the application is StorageApplication.java in src/main/java/edu/ntnu/idi/idatt.
The main method of the application is the main-method in the StorageApplication.java class initiating 
the UserInterface class which stars the application.

To run the application, there are two options:

1. Run the main sourcefile through a commandline terminal. For this method you need to download the project from
the GitHub repository and navigate to the project folder in the terminal. Then you can run the application by typing
the following command in the terminal:
```cd [path to the project folder]```
```cd src/main/java/edu/ntnu/idi/idatt```
```Java StorageApplication.java```.
Simply press enter after each command. This will run the application in the terminal.
2. Run the main sourcefile StorageApplication.java in src/main/java/edu/ntnu/idi/idatt/StorageApplication.java directly 
in the IDE of choice. If you are using IntelliJ IDEA, you can run the application by right-clicking on the main 
sourcefile and select "Run StorageApplication.main()", click on the run icon on the top right navigation-bar or 
simply pressing the hot-keys "Shift + F10".

The first method is recommended as it promotes unique features such as clearing screen for each time 
menus is displayed. While the first method is recommended, the second method is also a good option if you are
familiar with the IDE and want to run the application in the IDE.

The input and output of the program is text-based, as the project is an TUI. The expected behaviour of the program is that the application starts 
and the user is presented with a menu with commands that the user can use to navigate the application. With this, 
the user can choose to add, remove or update groceries and add or remove groceries from a foodstorage. The user also 
gets to add, remove or make a recipe, make a dish from a recipe and view the groceries in the foodstorage.


## How to run the tests

To run the JUnit-tests for the application, simply run the test classes in the test-folder 
(src/test/java/edu/ntnu/idi/idatt). The JUnit-tests are integrated in the IDE and can be run by right-clicking on the
test classes and select "Run 'TestClassName'". To run all tests in the test-folder, right-click on the test-folder and
select "Run 'All Tests'". The tests should run and the results should be displayed in the IDE.


## References

[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)