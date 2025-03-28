# TrainApp-Backend

This project is a command-line interface (CLI) based train ticket reservation system inspired by the Indian Railways Catering and Tourism Corporation (IRCTC) system. The backend is built using Java 8 and follows Object-Oriented Programming (OOP) principles. The system simulates key features of a train booking system with a focus on simplicity and functionality.

Features:
Train Search: Users can search for trains based on source and destination stations.

Ticket Reservation: Allows users to book tickets for available trains.

Seat Availability: Displays real-time seat availability on trains.

Booking History: Users can view their previous bookings and reservations.

JSON File-based Backend: All train data, user bookings, and related information are stored in JSON files, providing a simple and lightweight solution for data management.

OOP Design: The system is designed using object-oriented principles, ensuring clean, maintainable, and scalable code. Key classes represent different aspects of the system, such as trains, users, and bookings.

Technologies Used:
Java 8: The project is developed using Java 8 features such as Streams, Lambda expressions, and functional programming principles.

JSON: Used for data storage and easy manipulation of train and booking information.

OOP: The design follows object-oriented principles for modularity and code reuse.

How to Use:
Clone the repository to your local machine.

Ensure you have Java 8 installed.

Navigate to the project directory and run the Java application via the command line.

Follow the on-screen instructions to search for trains, book tickets, and view booking history.

File Structure:
data/: Contains the JSON files for train data, user bookings, and availability.

src/: Contains the Java source code, implementing the main functionalities of the system.

TrainSystem.java: Main entry point for the application.

Example Commands:
Search Trains: Enter source and destination to view available trains.

Book Ticket: Select a train and book a ticket based on available seats.

View History: Check previous bookings made by the user.
