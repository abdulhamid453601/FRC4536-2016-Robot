//TODO: incorporate drive encoders and navX

package org.usfirst.frc.team4536.robot.subsystems;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4536.robot.commands.DriveTrainCommand;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SPI;
import org.usfirst.frc.team4536.robot.*;
import com.kauailabs.navx.frc.AHRS;

public class DriveTrain extends Subsystem {
	
	VictorSP leftBackVictorSP;
	VictorSP leftFrontVictorSP;
	VictorSP rightBackVictorSP;
	VictorSP rightFrontVictorSP;
	Encoder leftEncoder;
	Encoder rightEncoder;
	AnalogGyro gyro;
	AHRS navX;
	
	public double oldForwardThrottle;
	double oldTurnThrottle;
	
	/**
	 * @author Max and Audrey 
	 * @param leftVictorSPChannel - The PWM channel of the left VictorSP of the drive train
	 * @param rightVictorSPChannel - THe PWM channel of the right VictorSP of the drive train
	 * @param leftEncoderChannelA - The first channel of the left encoder of the drive train
	 * @param leftEncoderChannelB - The second channel of the left encoder of the drive train
	 * @param rightEncoderChannelA - The first channel of the right encoder of the drive train
	 * @param rightEncoderChannelB - The second channel of the right encoder of the drive train
	 */
	
	public DriveTrain(int leftBackVictorSPChannel, int leftFrontVictorSPChannel,
						int rightBackVictorSPChannel, int rightFrontVictorSPChannel,
						int leftEncoderChannelA, int leftEncoderChannelB,
						int rightEncoderChannelA, int rightEncoderChannelB,
						int gyroChannel) {
		
		leftBackVictorSP = new VictorSP(leftBackVictorSPChannel);
		leftFrontVictorSP = new VictorSP(leftFrontVictorSPChannel);
		rightBackVictorSP = new VictorSP(rightBackVictorSPChannel);
		rightFrontVictorSP = new VictorSP(rightFrontVictorSPChannel);
		leftEncoder = new Encoder(leftEncoderChannelA, leftEncoderChannelB);
		rightEncoder = new Encoder(rightEncoderChannelA, rightEncoderChannelB);

		gyro = new AnalogGyro(gyroChannel);

		
		try {
	          navX = new AHRS(SPI.Port.kMXP); 
	      } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
	      }
		
		leftBackVictorSP.set(0.0);
		leftFrontVictorSP.set(0.0);
		rightBackVictorSP.set(0.0);
		rightFrontVictorSP.set(0.0);
	}
	
    public void initDefaultCommand() {
    	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand())
    	//setDefaultCommand(new Drive());
    	setDefaultCommand(new DriveTrainCommand());
    }
    
    /**
     * @ author Max and Liam
     * @ param leftThrottle - The throttle input into the left motors, positive value is left/forward
     * @ param rightThrottle - The throttle input into the right motors, positive value is right/forward
     */
    
    public void tankDrive(double leftThrottle, double rightThrottle) {
    	leftBackVictorSP.set(leftThrottle);
    	leftFrontVictorSP.set(leftThrottle);
    	rightBackVictorSP.set(-rightThrottle);
    	rightFrontVictorSP.set(-rightThrottle);
    }
    
    /**
     * @author Max and Liam
     * @param forwardThrottle - Throttle for forward motion of the drivetrain (+ forward, - backwards) 
     * @param turnThrottle - Throttle for horizontal motion of the drivetrain (+ right, - left)
     */
    
    public void arcadeDrive(double forwardThrottle, double turnThrottle) {

    	oldForwardThrottle = forwardThrottle;
    	oldTurnThrottle = turnThrottle;
    	
    	tankDrive(forwardThrottle + turnThrottle, forwardThrottle - turnThrottle);
    	
    }
    
    /**
     * @author Sheila and Audrey
     * @param forwardThrottle - see arcadeDrive
     * @param turnThrottle - see arcadeDrive
     */
    public void arcadeDriveAccelLimit(double forwardThrottle, double turnThrottle) {
    	System.out.println("Forward Throttle: "+forwardThrottle);
    	System.out.println("Old Forward Throttle: "+oldForwardThrottle);
    	System.out.println("Turn Throttle: "+turnThrottle);
    	System.out.println("Old Turn Throttle: "+oldTurnThrottle);
    	forwardThrottle = Utilities.accelLimit(forwardThrottle, oldForwardThrottle, Constants.ACCEL_LIMIT_DRIVE);
    	turnThrottle = Utilities.accelLimit(turnThrottle, oldTurnThrottle, Constants.ACCEL_LIMIT_DRIVE);

    
    	arcadeDrive(forwardThrottle, turnThrottle);
    }
    
    
    /**
     * @author Audrey
     * @return left encoder distance as inches
     */
	public double getLeftEncoder() {
		
		return leftEncoder.get()/Constants.DRIVE_ENCODER_PROPORTIONALITY_CONSTANT;
	}

	/**
	 * @author Audrey
	 * @return right encoder distance as inches
	 */
	public double getRightEncoder() {
		
		return -rightEncoder.get()/Constants.DRIVE_ENCODER_PROPORTIONALITY_CONSTANT;
	}

	/**
	 * @author Audrey
	 * @return right encoder velocity/rate in inches per second
	 */
	public double getRightRate() {
		
		return rightEncoder.getRate()/Constants.DRIVE_ENCODER_PROPORTIONALITY_CONSTANT;
	}
	
	/**
	 * @author Audrey
	 * @return left encoder velocity/rate in inches per second
	 */
	public double getLeftRate() {
		
		return leftEncoder.getRate()/Constants.DRIVE_ENCODER_PROPORTIONALITY_CONSTANT;
	}
	
	/**
	 * @author Mairead
	 * @return gyro angle in degrees
	 */
	public double getTrueGyroAngle() {
		
		return gyro.getAngle();
	}	
	
	/**
	 * @author Mairead
	 * @return gyro angle in degrees between -180 and 180
	 */
	public double getGyroAngle() {
		double angle;
		angle = this.getTrueGyroAngle()%360;
		
		if (angle < 0)
			return(angle + 360);
		else
			return (angle);		
	}
	
	/**
	 * @author Mairead
	 * @return gyro rate in degrees per seconds
	 */
	public double gyroRate() {
		
		return gyro.getRate();
	}		

	public void resetLeftEncoder() {
		
		leftEncoder.reset();
	}
	
	public void resetRightEncoder() {
		
		rightEncoder.reset();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	

	/**
	 * @author Audrey
	 * @return NavX Yaw or the angle of the robot around a vertical axis
	 * @return If output is positive, robot is turning right
	 * @return If output is negative, robot is turning left
	 */
	public double getNavXYaw(){
		
		return navX.getYaw();
	}
	
	/**
	 * @author Audrey
	 * @return NavX Pitch or the angle of the robot around an axis from left to right
	 * @return If output is positive, robot is tilting forward
	 * @return If output is negative, robot is tilting backwards
	 */
	public double getNavXPitch(){
		
		return navX.getPitch();
	}
	
	/**
	 * @author Audrey
	 * @return NavX Roll or the angle of the robot around an axis from front to back
	 * @return If output is positive, robot is tilting right
	 * @return If output is negative, robot is tilting left
	 */
	public double getNavXRoll(){
		
		return navX.getRoll();
	}

	/**
	 * @author Liam
	 * Resets the NavX
	 */
	public void resetNavX() {
		
		navX.reset();
	}
	
	/**
	 * @author Liam
	 * @return the rate of turning in degrees per second
	 */
	public double getYawRate() {
		
		return navX.getRawGyroZ();
	}
}


