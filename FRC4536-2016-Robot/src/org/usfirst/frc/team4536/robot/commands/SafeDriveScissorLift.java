package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.OI;
import org.usfirst.frc.team4536.robot.Utilities;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SafeDriveScissorLift extends CommandBase {

    public SafeDriveScissorLift(){
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(scissorLift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	scissorLift.safeDrive(Utilities.deadZone(OI.tertiaryStick.getY(), Constants.SCISSOR_DEAD_ZONE));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
