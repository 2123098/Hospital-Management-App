This is Hospital Management App which allows users like patients and doctors login into their account and new users can create account by clicking on register with us button.
For new users when creating account one must ensure that the password match and it should be at least 6 characters long. The app will not allow an existing user to create an account again.
When login successfully, the app will open the dashboard where features like Manage Doctor, Manage Patient, Schedule Appointments and View Appointments.
Manage Doctor: This page allow you to add, update and delete a doctor. Notice, all fields must be completed before a doctor can be successfully be added.
Manage Patient: This activity also allows you to add, update and delete a patient with similar logic all fields must be completed be a patient can be added successfully.
Schedule Appointments is where you can book an appointment with a doctor. Firstly you must enter the doctors name followed by patient name but make sure that both are added on the app else 
there will be a message that invalid doctor or patient name since they are not recorded in the database.
Also doctors and patients who have been booked for appointment can not be deleted directly from their activities or pages unless appointment is deleted first.
Also you delete appointment by selecting it and clicking on the delete button. The app will not allow users to book the same appointment at the same time and date in oder to prevent schedule conflict.
And lastly, the View Appointments. This is where all booked appointment appears. 

The app comes with a database named (hospital_database.db) with tables such as:
User table which stores users logins and Hash their passwords.
Doctor Table which stores doctors names, specialisation and phone.
Patient Table which stores patients names, diagnose and number.

How to access the database using SQLite: 
Open device explorer
Navigate to data and then click on data again
Look for com.example.hospitalapp and click on it
Click on databases
You will see the name of the database which hospital_database.db 
Save it with all the files in the folder
Then open the SQLite app
Click on open database
Navigate to where you save the files and open it

Unit testing was done for test cases such as:
Test Empty Fields to ensure all registration fields are not empty.
Test Short Password to check passwords validation. Meaning passwords less than 6 characters are rejected.
Test Passwords Match to make sure that password and confirm password are the same.
Test Username Existence to verify if the username already exists in the database.

JUnit testing was done to know how activities, classes and methods works as they are supposed to do. Some of the test cases are:
Add Doctor Button Test checking if a doctor is successfully added to the list.
Update Doctor Button Test checking if doctor's details can be updated successfully.
Delete Doctor Button Test checking if a doctor can be deleted from the list.
Boundary Phone Number Length Test checks if phone number length is less than 10 digits.
Invalid Phone Number Format Test checks for invalid input for phone number format like letters in the number.
