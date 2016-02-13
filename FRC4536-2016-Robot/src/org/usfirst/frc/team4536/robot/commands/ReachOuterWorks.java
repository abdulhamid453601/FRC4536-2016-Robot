package org.usfirst.frc.team4536.robot.commands;

import org.usfirst.frc.team4536.robot.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *@author Liam
 *This command group reaches the outerworks
 */
public class ReachOuterWorks extends CommandGroup {
    
    public  ReachOuterWorks(boolean forward) {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	if (forward) {
    		
    		addSequential(new DriveTrapezoidProfile(Constants.REACH_DEFENSE_DISTANCE, Constants.REACH_DEFENSE_VELOCITY, Constants.REACH_DEFENSE_ACCELERATION));
    	}
    	else {//TODO Fix negative Reach Outer Works
    		
    		addSequential(new DriveTrapezoidProfile(-Constants.REACH_DEFENSE_DISTANCE, Constants.REACH_DEFENSE_VELOCITY, Constants.REACH_DEFENSE_ACCELERATION));
    	}
    }
}
