package org.usfirst.frc.team4536.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	/*-------------------------------------Motor Channels------------------------------------*/	

    public static final int LEFT_MOTOR_FRONT = 1;
    public static final int LEFT_MOTOR_BACK = 0;
    
    public static final int RIGHT_MOTOR_FRONT = 2;
    public static final int RIGHT_MOTOR_BACK = 3;
    
    public static final int INTAKE_MOTOR_CHANNEL = 4;
    
    public static final int SCISSOR_MOTOR = 5;
    
    /*-------------------------------------Joystick Ports---------------------------------*/
    public static final int MAIN_STICK = 0; // (Also the left stick for tank drive)
    public static final int SECONDARY_STICK = 1; // (Also the right stick for tank drive)
    public static final int TERTIARY_STICK = 2; //Scissor Lift

	/*-------------------------------------Relay Solenoid-----------------*/
	
	public static final int SCISSOR_RELAY = 0; //these go in the relay port, 
	public static final int INTAKE_RELAY = 1; //electricians; not PWM or DIM 
	
    /*-------------------------------------Digital Sensor Channels---------------------------------*/	
	
	public static final int LEFT_ENCODER_CHANNEL_A = 8; //1 yellow wire in signal for left encoder
	public static final int LEFT_ENCODER_CHANNEL_B = 9; //Set of 3 wires blue in signal, then orange and brown for left encoder
	public static final int RIGHT_ENCODER_CHANNEL_A = 6; //1 yellow wire in signal for right encoder
	public static final int RIGHT_ENCODER_CHANNEL_B = 7; //Set of 3 wires blue in signal, then orange and brown for right encoder

	 /*-------------------------------------Analog Sensor Channels---------------------------------*/
	public static final int FRONT_ULTRA_CHANNEL = 0;
	public static final int BACK_ULTRA_CHANNEL = 1;
	public static final int LEFT_IR_CHANNEL = 2;
	public static final int INTAKE_IR_CHANNEL = 3;
	
}
