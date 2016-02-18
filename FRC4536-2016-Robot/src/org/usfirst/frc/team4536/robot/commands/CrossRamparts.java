package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;
import edu.wpi.first.wpilibj.command.CommandGroup;


public class CrossRamparts extends CommandGroup {
    
	public  CrossRamparts(boolean foward) {
	    	
			DriveTrapezoidProfile crossRamparts;
			
	    	if (foward){
	        	crossRamparts = new DriveTrapezoidProfile(Constants.CROSS_RAMPARTS_DISTANCE, Constants.CROSS_RAMPARTS_VELOCITY, Constants.CROSS_RAMPARTS_ACCEL_LIMIT, Constants.CROSS_RAMPARTS_GYRO_PROPORTIONALITY);
	        	double maxTime = crossRamparts.getNeededTime() + 1;
	    		addSequential(crossRamparts, maxTime);
	    	}else{
	        	crossRamparts = new DriveTrapezoidProfile(-Constants.CROSS_RAMPARTS_DISTANCE, Constants.CROSS_RAMPARTS_VELOCITY, Constants.CROSS_RAMPARTS_ACCEL_LIMIT, Constants.CROSS_RAMPARTS_GYRO_PROPORTIONALITY);
	        	double maxTime = crossRamparts.getNeededTime() + 1;
	    		addSequential(crossRamparts, maxTime);
	    	}
	}
	public CrossRamparts() {
		this(true);
	}
}
