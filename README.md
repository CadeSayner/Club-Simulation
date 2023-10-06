# Club-Simulation
This Java application simulates the behavior of patrons in a nightclub, providing a visual representation of their movements and interactions within the club environment.
Overview

The simulation is built on a grid-based system where each block represents a position within the club. Patrons enter through the entrance door, move around the club, visit the bar to get a drink, and eventually leave through the exit doors. The simulation enforces several rules to mimic a realistic nightclub experience:

Was developed as part of a project to show the benefits and dangers of concurrent programming in Java.

    Patrons enter and exit one at a time through the entrance and exit doors.
    The maximum number of patrons inside the club is limited.
    Patrons maintain a realistic distance from each other (one per grid block).
    Patrons move block by block simultaneously to ensure liveness.

Features

    Start, Pause, Quit: Control the simulation with the Start, Pause, and Quit buttons.
    Realistic Patron Behavior: Patrons follow a set of rules to navigate the club, get drinks, and eventually leave.
    Enforced Rules: The simulation enforces rules to ensure realistic behavior and prevent overcrowding.
    Visual Representation: The graphical interface provides a visual representation of the nightclub, including patrons, bar, entrance, and exit.
    Synchronization: The simulation uses synchronization mechanisms to manage access to critical sections and prevent data races.

Usage

    Clone the repository to your local machine.
    Open the project in your preferred Java IDE.
    Run the Main class to start the simulation.
    Use the GUI buttons to control the simulation (Start, Pause, Quit).

Classes

    ClubGrid: Represents the layout of the club, managing access to different blocks and enforcing rules.
    Clubgoer: Each instance represents a patron in the nightclub, simulating their behavior and movement.
    PeopleLocation: Provides information about a patron's location and manages access to it.
    GridBlock: Represents a block in the club grid, handling occupancy and access control.
    Barman: Represents the barman who serves drinks to patrons, managing their requests.
