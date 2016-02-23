package org.usfirst.frc.team4536.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author Liam
 * Right goal enter true for right, left goal enter false for left
 */
public class FinishScoringRoutine extends CommandGroup {
    
    public  FinishScoringRoutine(boolean right) {
      
    	if (right) {
    		
	    	addSequential(new TurnTrapezoidProfile(120, 240, 180));
	    	addSequential(new EjectBoulderAccelLimited());
    	}
    	else {
    		
    		addSequential(new TurnTrapezoidProfile(-120, 240, 180));
	    	addSequential(new EjectBoulderAccelLimited());
    	}
    }
}
