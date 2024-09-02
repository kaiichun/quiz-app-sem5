# Quiz App
#### Purpose
The purpose of this project is to create a comprehensive quiz application where users can create accounts as teachers, manage student groups, and create quizzes with timed multiple-choice questions. The app aims to provide an intuitive and user-friendly interface for both teachers and students, facilitating easy quiz creation, management, and participation. Teachers can import quiz data from pre-made CSV files and set or update quiz timings. Students need a specific ID provided by the teacher to access and answer the quizzes, and each student can only attempt each quiz once.

#### Key Features
##### 1. User Registration and Roles: 
Users can register as either teachers or students. Teachers can access a dashboard to manage quizzes, while students can join quizzes using a unique quiz ID.

##### 2. Teacher Main Screen:
Teachers can see which quizzes have student participation and which do not. 

##### 3. Teacher Dashboard Screen:
Teachers can create quizzes, set start and end times, and import quiz data from CSV files.

##### 3. Student Main Screen:
Students can log in, join quizzes using a quiz ID, and see the top 10 rankings on the leaderboard.

##### 4. Quiz Participation:
Students answer timed multiple-choice questions. If the time runs out without an answer, the quiz will automatically move to the next question.


#### Framework and Tools
##### Android Studio: 
The primary development environment for building the Android application. It supports Java/Kotlin and provides robust tools for UI design and testing.
##### Firebase: 
Used for backend services including user authentication, real-time database, and cloud storage. Firebase simplifies the implementation of user management and data synchronization across devices.
Architecture
##### The app follows the Model-View-ViewModel (MVVM) architectural pattern. This pattern helps in separating concerns, making the app modular, scalable, and maintainable.


#### Figma:
No Login Screens: <br/>
<img width="150" alt="figma_login" src="https://github.com/user-attachments/assets/b03e7d1d-b25d-41cf-8b64-7473c6b6a042">
<img width="150" alt="figma_register" src="https://github.com/user-attachments/assets/f1bd462c-a309-4933-a7f4-e84c26182f82">

Login Screens: <br/>
<img width="150" alt="figma_profile" src="https://github.com/user-attachments/assets/5d767a7c-7322-4727-8820-1a4470029044">
<img width="150" alt="figma_logout_alert" src="https://github.com/user-attachments/assets/a914c63a-ff62-4f82-a73c-921cb8e8dd92">

Teacher Role Screens: <br/>
<img width="150" alt="figma_teacher_home" src="https://github.com/user-attachments/assets/6c85f466-a611-462c-bd84-84f2a3d91a0a">
<img width="150" alt="figma_teacher_dashboard" src="https://github.com/user-attachments/assets/f8082aa5-9993-4f9c-8a33-bcd40e6266b2">
<img width="150" alt="figma_dashboard_delete" src="https://github.com/user-attachments/assets/71d0988d-4cb7-4935-bc49-178b3f7e46c0">
<img width="150" alt="figma_quiz_check" src="https://github.com/user-attachments/assets/3e4eb559-7e78-44a2-8bd5-da31e3c2f9ac">
<img width="150" alt="figma_nav_view_teacher" src="https://github.com/user-attachments/assets/d749cb1f-fa3d-4685-9f27-ce86902cd333">

Student Role Screens: <br/>
<img width="150" alt="figma_student_ranking" src="https://github.com/user-attachments/assets/f639154e-9222-4886-9a96-740368a762d1">
<img width="150" alt="figma_student_home" src="https://github.com/user-attachments/assets/fed11703-ee2e-4633-b828-cc44c6bf1a3f">
<img width="150" alt="quiz_answer_quiz" src="https://github.com/user-attachments/assets/7f525f72-ec75-499b-8dfc-0b2b011bbdfb">
<img width="150" alt="figma_quiz_view" src="https://github.com/user-attachments/assets/a5f4995e-1185-4a82-aa0f-fad4855f2620">
<img width="150" alt="figma_nav_view_student" src="https://github.com/user-attachments/assets/ca7e24f7-525e-4217-a14a-bc54f4c3d366">
