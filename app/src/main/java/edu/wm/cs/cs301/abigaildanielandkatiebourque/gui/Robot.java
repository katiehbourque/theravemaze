package edu.wm.cs.cs301.abigaildanielandkatiebourque.gui;

import  edu.wm.cs.cs301.abigaildanielandkatiebourque.generation.CardinalDirection;

public interface Robot {
    /**
     * Describes all possible turns that a robot can do when it rotates on the spot.
     * Left is 90 degrees left, right is 90 degrees right, turn around is 180 degrees.
     */
    public enum Turn { LEFT, RIGHT, AROUND }
    /**
     * Describes all possible directions from the point of view of the robot,
     * i.e., relative to its current forward position.
     * Mind the difference between the robot's point of view
     * and cardinal directions in terms of north, south, east, west.
     */
    public enum Direction { LEFT, RIGHT, FORWARD, BACKWARD }
    /**
     * Provides the current position as (x,y) coordinates for the maze cell as an array of length 2 with [x,y].
     * @postcondition 0 <= x < width, 0 <= y < height of the maze.
     * @return array of length 2, x = array[0], y=array[1]
     * @throws Exception if position is outside of the maze
     */
    int[] getCurrentPosition() throws Exception;
    /**
     * Provides the current cardinal direction.
     * @return cardinal direction is robot's current direction in absolute terms
     */
    CardinalDirection getCurrentDirection();
    /**
     * Provides the robot with a reference to the controller to cooperate with.
     * The robot memorizes the controller such that this method is most likely called only once
     * and for initialization purposes. The controller serves as the main source of information
     * for the robot about the current position, the presence of walls, the reaching of an exit.
     * The controller is assumed to be in the playing state.
     * @param controller is the communication partner for robot
     * @precondition controller != null, controller is in playing state and has a maze
     */
    void setMaze(StatePlaying controller) ;
    /**
     * Returns the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for distance2Obstacle may use less energy than a move forward operation.
     * If battery level <= 0 then robot stops to function and hasStopped() is true.
     * @return current battery level, level is > 0 if operational.
     */
    float getBatteryLevel() ;
    /**
     * Sets the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for distance2Obstacle may use less energy than a move forward operation.
     * If battery level <= 0 then robot stops to function and hasStopped() is true.
     * @param level is the current battery level
     * @precondition level >= 0
     */
    void setBatteryLevel(float level) ;
    /**
     * Gets the distance traveled by the robot.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     * The counter can be reset to 0 with resetOdomoter().
     * @return the distance traveled measured in single-cell steps forward
     */
    int getOdometerReading();
    /**
     * Resets the odomoter counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
    void resetOdometer();
    /**
     * Gives the energy consumption for a full 360 degree rotation.
     * Scaling by other degrees approximates the corresponding consumption.
     * @return energy for a full rotation
     */
    float getEnergyForFullRotation() ;
    /**
     * Gives the energy consumption for moving forward for a distance of 1 step.
     * For simplicity, we assume that this equals the energy necessary
     * to move 1 step backwards and that scaling by a larger number of moves is
     * approximately the corresponding multiple.
     * @return energy for a single step forward
     */
    float getEnergyForStepForward() ;
    ///////////////////////////////////////////////////////////////////
    /////////////////// Sensors   /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /**
     * Tells if current position (x,y) is right at the exit but still inside the maze.
     * Used to recognize termination of a search.
     * @return true if robot is at the exit, false otherwise
     */
    boolean isAtExit() ;
    /**
     * Tells if a sensor can identify the exit in the given direction relative to
     * the robot's current forward direction from the current position.
     * @return true if the exit of the maze is visible in a straight line of sight
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     */
    boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException ;
    /**
     * Tells if current position is inside a room.
     * @return true if robot is inside a room, false otherwise
     * @throws UnsupportedOperationException if not supported by robot
     */
    boolean isInsideRoom() throws UnsupportedOperationException ;
    /**
     * Tells if the robot has a room sensor.
     */
    boolean hasRoomSensor() ;
    /**
     * Tells if the robot has stopped for reasons like lack of energy, hitting an obstacle, etc.
     * @return true if the robot has stopped, false otherwise
     */
    boolean hasStopped() ;
    /**
     * Tells the distance to an obstacle (a wall)
     * in the given direction.
     * The direction is relative to the robot's current forward direction.
     * Distance is measured in the number of cells towards that obstacle,
     * e.g. 0 if the current cell has a wallboard in this direction,
     * 1 if it is one step forward before directly facing a wallboard,
     * Integer.MaxValue if one looks through the exit into eternity.
     * @param direction specifies the direction of the sensor
     * @return number of steps towards obstacle if obstacle is visible
     * in a straight line of sight, Integer.MAX_VALUE otherwise
     * @throws UnsupportedOperationException if the robot does not have
     * an operational sensor for this direction
     */
    int distanceToObstacle(Direction direction) throws UnsupportedOperationException ;
    /**
     * Tells if the robot has an operational distance sensor for the given direction.
     * The interface is generic and may be implemented with robots
     * that are more or less equipped with sensor or have sensors that
     * are subject to failures and repairs.
     * The purpose is to allow for a flexible robot driver to adapt
     * its driving strategy according the features it
     * finds supported by a robot.
     * @param direction specifies the direction of the sensor
     * @return true if robot has operational sensor, false otherwise
     */
    boolean hasOperationalSensor(Direction direction) ;
    /**
     * Makes the robot's distance sensor for the given direction fail.
     * Subsequent calls to measure the distance to an obstacle in
     * this direction will return with an exception.
     * If the robot does not have a sensor in this direction,
     * the method does not have any effect.
     * Only distance sensors can fail, the room sensor and exit
     * sensor if installed are always operational.
     * @param direction specifies the direction of the sensor
     */
    void triggerSensorFailure(Direction direction) ;
    /**
     * Makes the robot's distance sensor for the given direction
     * operational again.
     * A method call for an already operational sensor has no effect
     * but returns true as the robot has an operational sensor
     * for this direction.
     * A method call for a sensor that the robot does not have
     * has not effect and the method returns false.
     * @param direction specifies the direction of the sensor
     * @return true if robot has operational sensor, false otherwise
     */
    boolean repairFailedSensor(Direction direction) ;
    ///////////////////////////////////////////////////////////////////
    /////////////////// Actuators /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////
    /**
     * Turn robot on the spot for amount of degrees.
     * If robot runs out of energy, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * @param turn direction and relative to current forward direction.
     */
    void rotate(Turn turn);
    /**
     * Moves robot forward a given number of steps. A step matches a single cell.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot hits an obstacle like a wall, it depends on the mode of operation
     * what happens. If an algorithm drives the robot, it remains at the position in front
     * of the obstacle and also hasStopped() == true as this is not supposed to happen.
     * This is also helpful to recognize if the robot implementation and the actual maze
     * do not share a consistent view on where walls are and where not.
     * If a user manually operates the robot, this behavior is inconvenient for a user,
     * such that in case of a manual operation the robot remains at the position in front
     * of the obstacle but hasStopped() == false and the game can continue.
     * @param distance is the number of cells to move in the robot's current forward direction
     * @param manual is true if robot is operated manually by user, false otherwise
     * @precondition distance >= 0
     */
    void move(int distance, boolean manual);
    /**
     * Makes robot move in a forward direction even if there is a wall
     * in front of it. In this sense, the robot jumps over the wall
     * if necessary. The distance is always 1 step and the direction
     * is always forward.
     * @throws Exception is thrown if the chosen wall is an exterior wall
     * and the robot would land outside of the maze that way.
     * The current location remains set at the last position,
     * same for direction but the game is supposed
     * to end with a failure.
     */
    void jump() throws Exception;
}
