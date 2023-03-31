# Daily Habit Tracker

A to-do list application that helps users keep track of and maintain daily tasks and habits.

![Demonstration of the application](https://i.imgur.com/4cK8LDN.png)

## Description

This program was made as a capstone project for Colgate University's Spring 2023 User Interfaces course.

The program maintains a list of tasks that the user can add to and change. The tasks are meant to be habits that the user wants to reinforce and consistently complete every day. Thus, there is no visible deadline for each task; the user should aim to complete all their tasks by the end of the day. 

The table on the left of the application contains a list of tasks that the user wants to complete at the end of the day. New tasks can be added to the table with the text field under the table. The user can use the checkbox on the table rows to mark tasks as completed. The X button on each row allows the user to delete tasks. The top progress bar denotes the percent of tasks the user has completed in a day; when all tasks are completed, the bar fills to full. 

The text field on top of the calendar denotes the current date the user is on; when the application is started, this text field is automatically set to the current date on the user's system time. When the user is finished with their day, they can press the "End the Current Day" button to move onto the next day, which would carry over all tasks the user had on their to-do list on the previous day and set them to being incomplete. 

Moving onto a new day also saves the user's tasks and their completion statuses of the previous day; the user can see this information by navigating to the previous day on the calendar. When the calendar is on a day prior to the current date denoted above the calendar, the user can only view the to-do list; they cannot add new tasks or otherwise interact with the to-do list, as seen in the image below.

![Application when navigating to a date other than the current date](https://i.imgur.com/nYfPaxh.png)

A desired feature of the application is to color-code date squares on the calendar to denote how much of their tasks the user completed on that date, but as of the March 30th version of this application, this feature is only hard-coded into the application as a demonstration/proof-of-concept.

## Getting Started

### Dependencies

* JavaFX Environment libraries - the program uses JavaFX 19.0.2.1.
* Java 19 JDK

### Installing

* [Install Java JDK](https://www.oracle.com/java/technologies/downloads/)
* [Install JavaFX](https://gluonhq.com/products/javafx/); retain the directory in which JavaFX is installed
* Optional for Windows: Set up a system environment variable to compile the applications, along with other JavaFX-based programs, more easily.
    * Options -> Systems -> Advanced System Settings -> Environment Variables -> New (System Variables)
    * Variable name can be anything, such as JAVA_FX
    * Variable value should be: 
```--module-path "(full directory of your JavaFX libraries)" --add-modules=ALL-MODULE-PATH```
    * Test whether the environment variable is properly set up with Command Prompt using the command: ```echo %(your variable name)%```
* Make sure your JavaFX and Java JDKs are properly set up by compiling [sample Java/JavaFX files](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/get_start_apps.htm); instructions for file compilation are included later.
* Unzip the application into any desired folder - the application will generate .class files as it is compiled.

### Executing program

* With no environment variables:
    * Use any command line interface (CLI), such as Powershell, to navigate to the folder holding the application.
    * Use the following command to compile all files in the application directory:
```
javac --module-path "(full directory of your JavaFX libraries)" --add-modules=ALL-MODULE-PATH DailyHabitTracker.java
``` 
If no errors are found during compilation, use the following command to run the application: 
```
java --module-path "(full directory of your JavaFX libraries)" --add-modules=ALL-MODULE-PATH DailyHabitTracker
```  
* With environment variables:
    * Use Command Prompt to navigate to the folder holding the application.
    * Use the following command to compile all files in the application directory:
```
javac %(your variable name)% *.java
```
If no errors are found during compilation, use the following command to run the application:
```
java %(your variable name)% StudentHousing
``` 

## Known Issues and Unimplemented Features

* Application window cannot be resized.
* When selecting color-coded date squares, their color does not change.
* Task data is not saved when the user closes the application.

## Author

* Minh Tao - mtao@colgate.edu

## Acknowledgments

This application uses code snippets and ideas from the official Oracle documentation for JavaFX and other JavaFX users on StackOverflow.
* [JavaFX API Documentation](https://docs.oracle.com/en/java/javase/19/)
* [TableView](https://docs.oracle.com/javafx/2/ui_controls/table-view.htm)
* [ProgressBar](https://docs.oracle.com/javafx/2/ui_controls/progress.htm)
* [DatePicker](https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/date-picker.htm)
* [How to add buttons to JavaFX TableViews](https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view)
