package org.usfirst.frc.team4536.robot.commands;

/**
 * @author Liam
 * This Class is used to handle all printouts from the roboRIO. To be able to graph information in excel we enter printouts in CSV format. We then select all and copy and paste that data into a text document and save it as a CSV file. You can then open it in excel and make a graph.
 */
public class RioLoggingCommand extends CommandBase {

	/**
	 * @author Liam
	 */
    public RioLoggingCommand() {
    }
    
	/**
	 * @author Liam
	 */
    protected void initialize() {
    	
    	// Add titles for data here
    	System.out.println("Data Title Goes Here,");
    	
    }
    
	/**
	 * @author Liam
	 */
    protected void execute() {
    	
    	//Add printouts in CSV format for data here and remember to add a title above
    	System.out.println(",");
    }
    
	/**
	 * @author Liam
	 */
    protected boolean isFinished() {
        return false;
    }
    
	/**
	 * @author Liam
	 */
    protected void end() {
    }
    
	/**
	 * @author Liam
	 */
    protected void interrupted() {
    }
}
