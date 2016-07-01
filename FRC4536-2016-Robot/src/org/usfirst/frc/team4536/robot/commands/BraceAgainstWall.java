package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.TrapezoidProfile;
import org.usfirst.frc.team4536.robot.Utilities;
import edu.wpi.first.wpilibj.Timer;

/**
 *@author Liam
 */
public class BraceAgainstWall extends CommandBase {
	
	Timer timer = new Timer();
	TrapezoidProfile trapezoid;
	private double startingAngle;
	private double proportionalityConstant = Constants.DEFAULT_CROSSING_GYRO_PROPORTIONALITY;
	private double accumulatedDistanceError = 0.0;
	private double accumulatedAngleError = 0.0;
	private boolean collision = false;
	private double jerkThreshold = Constants.JERK_COLLISION_THRESHOLD;
	
	/**
	 * @author Liam
	 * @param the distance desired to be travelled in feet
	 * Sets the Max Speed and Acceleration to the defaults defined in constants
	 */
	public BraceAgainstWall(double distance) {
		
		this(distance, Constants.TRAPEZOID_DEFAULT_SPEED, Constants.TRAPEZOID_DEFAULT_ACCELERATION);
	}
	
	/**
	 * @author Liam
	 * @param the distance desired to be travelled in feet
	 * @param the angle the profile should maintain
	 */
	public BraceAgainstWall(double distance, double angle) {
		
		this(distance, Constants.TRAPEZOID_DEFAULT_SPEED, Constants.TRAPEZOID_DEFAULT_ACCELERATION);
		startingAngle = angle;
	}
	
	/**
	 * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Always positive.
	 * @param maxAcceleration The maximum possible acceleration the speed can change by in feet per second squared. Always positive.
	 */
    public BraceAgainstWall(double distance, double maxSpeed, double maxAcceleration) {

    	requires(driveTrain);
    	trapezoid = new TrapezoidProfile(distance, maxSpeed, maxAcceleration);
    }
    
	/**
	 * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
<<<<<<< HEAD
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Always positive.
	 * @param maxAcceleration The maximum possible acceleration the speed can change by in feet per second squared. Always positive.
=======
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Scalar so Always positive.
	 * @param maxAcceleration The maximum possible acceleration in feet per second squared the speed can change by. Always positive.
>>>>>>> refs/remotes/origin/DriveTrapezoidIntegral
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 */
    public BraceAgainstWall(double distance, double maxSpeed, double maxAcceleration, double gyroProportionality) {
    	
    	this(distance, maxSpeed, maxAcceleration);
    	proportionalityConstant = gyroProportionality;
    }
    
    /**
     * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Always positive.
	 * @param maxAcceleration The maximum possible acceleration the speed can change by in feet per second squared. Always positive.
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 * @param the angle the profile should maintain
     */
    public BraceAgainstWall(double distance, double maxSpeed, double maxAcceleration, double gyroProportionality, double angle) {
    	
    	this(distance, maxSpeed, maxAcceleration, gyroProportionality);
    	startingAngle = angle;
    }
    
    /**
     * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Always positive.
	 * @param maxAcceleration The maximum possible acceleration the speed can change by in feet per second squared. Always positive.
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 * @param the angle the profile should maintain
	 * @param the jerk threshold for determining a collision
     */
    public BraceAgainstWall(double distance, double maxSpeed, double maxAcceleration, double gyroProportionality, double angle, double jerkThreshold) {
    	
    	this(distance, maxSpeed, maxAcceleration, gyroProportionality, angle);
    	this.jerkThreshold = jerkThreshold;
    }
    
    /**
     * @author Liam
     * @return time in seconds since the command was started
     */
    public double getTime() {
    	
    	return timer.get();
    }
    
    /**
     * @author Audrey
     * @return time needed from the trapezoid profile method in seconds
     */
    public double getNeededTime(){
    	
    	return trapezoid.getTimeNeeded();
    }
    
    protected void initialize() {
    	timer.reset();
    	timer.start();
    	
    	accumulatedDistanceError = 0.0;
    	accumulatedAngleError = 0.0;
    	
    	driveTrain.resetEncoders();
    	startingAngle = driveTrain.getAngle();
    	setTimeout(trapezoid.getTimeNeeded() + Constants.TRAPEZOID_PROFILE_TIMEOUT_OFFSET);
    }
    
    /**
     * @author Liam
     * @return the error in the distance between where the robot is and where it should be in inches.
     */
    public double getDistanceError() {
    	
    	double error = trapezoid.idealDistance(timer.get())*12 - driveTrain.getRightEncoder();
    	
    	return error;
    }
    
    /**
     * @author Liam
     * @return the accumulated error over time in the distance between where the robot is and where it should be in inches.
     */
    public double getAccumulatedDistanceError() {
    	
    	accumulatedDistanceError += getDistanceError() * Utilities.getCycleTime();
    	
    	return accumulatedDistanceError;
    }
    
    /**
     * @author Liam
     * @return the error in the angle between what angle the robot is at and the angle it should be at in degrees.
     */
    public double getAngleError() {
    	
    	double error = Utilities.angleDifference(startingAngle,driveTrain.getAngle());
    	
    	return error;
    }
    
    /**
     * @author Liam
     * @return the accumulated error over time in the angle between what angle the robot is at and the angle it should be at in degrees.
     */
    public double getAccumulatedAngleError() {
    	
    	accumulatedAngleError += getAngleError() * Utilities.getCycleTime();
    	
    	return accumulatedAngleError;
    }
    
    public boolean isCollision() {
    	
    	if (driveTrain.getOrthoganalJerk() > jerkThreshold) {
    		
    		collision = true;
    	}
    	
    	return collision;
    }
    
    protected void execute() {
    	
    	driveTrain.arcadeDrive(trapezoid.throttle(timer.get()) + (Constants.TRAPEZOID_FORWARD_PROPORTIONALITY * getDistanceError()) + Constants.TRAPEZOID_INTEGRAL * getAccumulatedDistanceError(),
    							(proportionalityConstant * getAngleError() - Constants.TURNING_TRAPEZOID_INTEGRAL * getAccumulatedAngleError()));
    }
    
    protected boolean isFinished() {
    	
    	if (isCollision()) {
    		
    		System.out.println("Brace Against Wall finished from collision.");
    		
    		return true;
    	}
    	else if ((driveTrain.getEncoder() >= (trapezoid.getDistance()*12 - Constants.TRAPEZOID_DISTANCE_THRESHOLD) &&
    			driveTrain.getEncoder() <= (trapezoid.getDistance()*12 + Constants.TRAPEZOID_DISTANCE_THRESHOLD)) &&
        	(driveTrain.getRate() >= -Constants.TRAPEZOID_SPEED_THRESHOLD
    			&& driveTrain.getRate() <= Constants.TRAPEZOID_SPEED_THRESHOLD) &&
    		(driveTrain.getNavXYaw() >= -Constants.TRAPEZOID_ANGLE_THRESHOLD
    				&& driveTrain.getNavXYaw() <= Constants.TRAPEZOID_ANGLE_THRESHOLD) && 
    		(driveTrain.getYawRate() >= -Constants.TRAPEZOID_ANGULAR_SPEED_THRESHOLD
    				&& driveTrain.getYawRate() <= Constants.TRAPEZOID_ANGULAR_SPEED_THRESHOLD)){ //conditions may cancel
    		
    		System.out.println("Brace Against Wall finished from ending criteria.");
    		
    		return true;
    	}
    	else { //Timeout may cancel
    		
    		System.out.println("Brace Against Wall finished from timeout.");
    		
    		return isTimedOut();
    	}
    }
    
    protected void end() {
    	driveTrain.arcadeDrive(0, 0);
    }
    
    protected void interrupted() {
    	
    	end();
    }
}