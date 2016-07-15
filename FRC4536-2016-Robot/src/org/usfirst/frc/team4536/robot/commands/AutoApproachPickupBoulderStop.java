package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.Filter;
import org.usfirst.frc.team4536.robot.RobotMap;
import org.usfirst.frc.team4536.robot.TrapezoidProfile;
import org.usfirst.frc.team4536.robot.Utilities;
import edu.wpi.first.wpilibj.Timer;

/**
 *@author Liam
 */
public class AutoApproachPickupBoulderStop extends CommandBase {
	
	Timer timer = new Timer();
	TrapezoidProfile trapezoid;
	private double desiredAngle = -720;
	private double proportionalityConstant = Constants.DEFAULT_CROSSING_GYRO_PROPORTIONALITY;
	private double accumulatedDistanceError = 0.0;
	private double accumulatedAngleError = 0.0;
	private final Filter filter;
	private final int numDataPoints = 20;
	private double startingTime;
	
	/**
	 * @author Liam
	 * @param the distance desired to be travelled in feet
	 * Sets the Max Speed and Acceleration to the defaults defined in constants
	 */
	public AutoApproachPickupBoulderStop(double distance) {
		
		this(distance, Constants.TRAPEZOID_DEFAULT_SPEED, Constants.TRAPEZOID_DEFAULT_ACCELERATION);
	}
	
	/**
	 * @author Liam
	 * @param the distance desired to be travelled in feet
	 * @param the angle the profile should maintain
	 */
	public AutoApproachPickupBoulderStop(double distance, double angle) {
		
		this(distance, Constants.TRAPEZOID_DEFAULT_SPEED, Constants.TRAPEZOID_DEFAULT_ACCELERATION);
		desiredAngle = angle;
	}
	
	/**
	 * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Always positive.
	 * @param maxAcceleration The maximum possible acceleration in feet per second squared the speed can change by. Always positive.
	 */
    public AutoApproachPickupBoulderStop(double distance, double maxSpeed, double maxAcceleration) {

    	requires(driveTrain);
    	requires(intake);
    	trapezoid = new TrapezoidProfile(distance, maxSpeed, maxAcceleration);
    	filter = new Filter();
    }
    
	/**
	 * @author Liam
	 * @param distance The desired distance the robot should travel in feet. May be negative or positive to indicate direction.
	 * @param maxSpeed The maximum possible speed the robot could be traveling at in feet per second. Scalar so Always positive.
	 * @param maxAcceleration The maximum possible acceleration in feet per second squared the speed can change by. Always positive.
	 * @param custom gyro proportionality constant to override the default. Useful for command groups that may require more correction due to terrain.
	 */
    public AutoApproachPickupBoulderStop(double distance, double maxSpeed, double maxAcceleration, double gyroProportionality) {
    	
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
    public AutoApproachPickupBoulderStop(double distance, double maxSpeed, double maxAcceleration, double gyroProportionality, double angle) {
    	
    	this(distance, maxSpeed, maxAcceleration, gyroProportionality);
    	desiredAngle = angle;
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
    	startingTime = Utilities.getTime();
    	
    	accumulatedDistanceError = 0.0;
    	accumulatedAngleError = 0.0;
    	
    	driveTrain.resetEncoders();
    	
    	if (desiredAngle < -360) {
    		
    		desiredAngle = driveTrain.getAngle();
    	}
    	
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
    	
    	double error = Utilities.angleDifference(desiredAngle, driveTrain.getAngle());
    	
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
    
    protected void execute() {
    	
    	intake.setThrottle(1.0);
    	
    	if (Utilities.getTime() - startingTime > Constants.AUTO_INTAKE_DELAY) {
    		
    		filter.update(Utilities.getCurrent(RobotMap.PDP_INTAKE));
    	}
    	
    	driveTrain.arcadeDrive(trapezoid.throttle(timer.get()) + (Constants.TRAPEZOID_FORWARD_PROPORTIONALITY * getDistanceError()) + Constants.TRAPEZOID_INTEGRAL * getAccumulatedDistanceError(),
    							(proportionalityConstant * getAngleError() - Constants.TURNING_TRAPEZOID_INTEGRAL * getAccumulatedAngleError()));
    }
    
    protected boolean isFinished() {
    	
    	if ((driveTrain.getEncoder() >= (trapezoid.getDistance()*12 - Constants.TRAPEZOID_DISTANCE_THRESHOLD) &&
    			driveTrain.getEncoder() <= (trapezoid.getDistance()*12 + Constants.TRAPEZOID_DISTANCE_THRESHOLD)) &&
        	(driveTrain.getRate() >= -Constants.TRAPEZOID_SPEED_THRESHOLD
    			&& driveTrain.getRate() <= Constants.TRAPEZOID_SPEED_THRESHOLD) &&
    		(driveTrain.getNavXYaw() >= -Constants.TRAPEZOID_ANGLE_THRESHOLD
    				&& driveTrain.getNavXYaw() <= Constants.TRAPEZOID_ANGLE_THRESHOLD) && 
    		(driveTrain.getYawRate() >= -Constants.TRAPEZOID_ANGULAR_SPEED_THRESHOLD
    				&& driveTrain.getYawRate() <= Constants.TRAPEZOID_ANGULAR_SPEED_THRESHOLD) ||
    		(filter.getMean(numDataPoints) > Constants.AUTO_INTAKE_CURRENT)){ //conditions may cancel
    		
    		return true;
    	}
    	else { //Timeout may cancel
    		
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