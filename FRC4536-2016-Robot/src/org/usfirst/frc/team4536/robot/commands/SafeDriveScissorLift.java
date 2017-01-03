package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import org.usfirst.frc.team4536.robot.OI;
import org.usfirst.frc.team4536.robot.Utilities;

/**
 *@author Noah
 */
public class SafeDriveScissorLift extends CommandBase {

    public SafeDriveScissorLift(){
    	
        requires(scissorLift);
    }
    
    protected void initialize() {
    	
    	scissorLift.resetValues();
    }
    
    protected void execute() {
    	
    	scissorLift.safeDrive(Utilities.deadZone(OI.tertiaryStick.getY(), Constants.SCISSOR_DEAD_ZONE));
    }
    
    protected boolean isFinished() {
    	
        return false;
    }
    
    protected void end() {
    	
    	
    }
    
    protected void interrupted() {
    	
    	
    }
}
