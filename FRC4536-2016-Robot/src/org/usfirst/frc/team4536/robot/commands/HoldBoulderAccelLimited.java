package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;

import edu.wpi.first.wpilibj.command.Command;

/**
 *@author Liam
 */
public class HoldBoulderAccelLimited extends CommandBase {

    public HoldBoulderAccelLimited() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	intake.setThrottleAccelLimited(Constants.HOLD_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	intake.setThrottle(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
