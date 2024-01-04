// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import javax.swing.plaf.TreeUI;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.AutoCommandList;
import frc.robot.commands.LockWheels;
import frc.robot.commands.SwerveDrive;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorController;
import frc.robot.controls.Xbox;
import frc.robot.sensors.Accelerometer4237;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.MainShuffleboard;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
	
	private boolean useFullRobot			= false;
	private boolean useBindings				= true;

	private boolean useExampleSubsystem		= false;
	private boolean useAccelerometer		= false;
	private boolean useGyro					= true;
	private boolean useDrivetrain   		= true;
	private boolean useDriverController		= true;
	private boolean useOperatorController 	= false;
	private boolean useMainShuffleboard		= false;

	private boolean useDataLog				= false;
	
	
	public final boolean fullRobot;
	public final ExampleSubsystem exampleSubsystem;
	public final Drivetrain drivetrain;
	public final DriverController driverController;
	public final OperatorController operatorController;
	public final MainShuffleboard mainShuffleboard;
	public final Accelerometer4237 accelerometer;
	public final Gyro4237 gyro;
	// public final PowerDistribution pdh;
	public final Compressor compressor;
	public DataLog log = null;
	// public static final DataLog log = DataLogManager.getLog();


	/** 
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 * Use the default modifier so that new objects can only be constructed in the same package.
	 */
	RobotContainer()
	{
		// Create the needed subsystems
		if(useFullRobot || useDataLog)
		{
			DataLogManager.start();
			log = DataLogManager.getLog();
		}
			
			
		// log					= (useDataLog)									? DataLogManager.getLog()								: null;

		fullRobot 			= (useFullRobot);
		exampleSubsystem 	= (useExampleSubsystem)							? new ExampleSubsystem() 								: null;
		accelerometer		= (useAccelerometer)							? new Accelerometer4237()								: null;
		gyro 				= (useFullRobot || useGyro || useDrivetrain)	? new Gyro4237()										: null;	
		drivetrain 			= (useFullRobot || useDrivetrain) 				? new Drivetrain(gyro, log) 							: null;
		driverController 	= (useFullRobot || useDriverController) 		? new DriverController(Constants.Controller.DRIVER) 	: null;
		operatorController 	= (useFullRobot || useOperatorController) 		? new OperatorController(Constants.Controller.OPERATOR)	: null;
		mainShuffleboard 	= (useFullRobot || useMainShuffleboard)			? new MainShuffleboard(this)							: null;
		compressor			= (true)										? new Compressor(0, PneumaticsModuleType.CTREPCM)		: null;

		// pdh = new PowerDistribution(1, ModuleType.kRev);
		// compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
		

		// Configure the trigger bindings
		if(useFullRobot || useBindings)
			configureBindings();

		// configureSchedulerLog();
	}

	/**
	 * Use this method to define your trigger->command mappings. Triggers can be created via the
	 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
	 * predicate, or via the named factories in {@link
	 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
	 * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
	 * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
	 * joysticks}.
	 */
	private void configureBindings()
	{
		configureDriverBindings();
		configureOperatorBindings();
	}

	private void configureDriverBindings()
	{
		if(driverController != null)
        {
			//Axis, driving and rotating
			DoubleSupplier leftYAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftY);
			DoubleSupplier leftXAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftX);
			DoubleSupplier rightXAxis = driverController.getAxisSupplier(Xbox.Axis.kRightX);
			DoubleSupplier leftYAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kLeftY, 0.15);
			DoubleSupplier leftXAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kLeftX, 0.15);
			DoubleSupplier rightXAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kRightX, 0.5);
			DoubleSupplier leftYAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kLeftY, 0.25);
			DoubleSupplier leftXAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kLeftX, 0.25);
			DoubleSupplier rightXAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kRightX, 0.5);
			DoubleSupplier zero = () -> 0.0;

			// Start Button
			BooleanSupplier startButton = driverController.getButtonSupplier(Xbox.Button.kStart);
			Trigger startButtonTrigger = new Trigger(startButton);
			if(gyro != null)
			{
				startButtonTrigger.toggleOnTrue(new InstantCommand( () -> { gyro.reset(); } ) );
			}

			//A Button
			BooleanSupplier aButton = driverController.getButtonSupplier(Xbox.Button.kA);
			Trigger aButtonTrigger = new Trigger(aButton);

			//B Button
			BooleanSupplier bButton = driverController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(bButton);
			if(drivetrain != null)
			{
				// bButtonTrigger.toggleOnTrue(new SwerveDriveXOnly(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
				bButtonTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, zero, zero, true));
			}

			//X Button-lockwheels
			BooleanSupplier xButton = driverController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			if(drivetrain != null)
			{
				xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
							  .until(driverController.tryingToMoveRobot()) );
			}
			
			//Y Button
			BooleanSupplier yButton = driverController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);
			// if(drivetrain != null)
			{
				// yButtonTrigger.toggleOnTrue(new SwerveDriveCrawl(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
				// yButtonTrigger.toggleOnTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true));
			}

			//Right trigger 
			BooleanSupplier rightTrigger = driverController.getButtonSupplier(Xbox.Button.kRightTrigger);
			Trigger rightTriggerTrigger = new Trigger(rightTrigger);

			//Right Bumper
			BooleanSupplier rightBumper = driverController.getButtonSupplier(Xbox.Button.kRightBumper);
			Trigger rightBumperTrigger = new Trigger(rightBumper);
			if(drivetrain != null)
			{
				rightBumperTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawlCommunity, leftXAxisCrawlCommunity, rightXAxisCrawlCommunity, true));

			}
			

			//Left trigger 
			BooleanSupplier leftTrigger = driverController.getButtonSupplier(Xbox.Button.kLeftTrigger);
			Trigger leftTriggerTrigger = new Trigger(leftTrigger);
			// if(drivetrain != null && gyro != null && vision != null)
			// {
			// 	// leftTriggerTrigger.whileTrue( new NewDriveToSubstation(drivetrain, ultrasonic, leftYAxis, leftXAxis, rightXAxis, true, 0.15));
			// }

			//Left Bumper
			BooleanSupplier leftBumper = driverController.getButtonSupplier(Xbox.Button.kLeftBumper);
			Trigger leftBumperTrigger = new Trigger(leftBumper);
			if(drivetrain != null)
			{

				leftBumperTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true));
				// leftBumperTrigger.whileTrue(
				// 	new ConditionalCommand(

				// 		new PrintCommand("Regular Swerve").andThen(
				// 		new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true)),
				// 		new PrintCommand("Crawl Swerve").andThen( 
				// 		new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true)), 
				// 		() -> ultrasonic.getDistance() > 6.0));
				// leftBumperTrigger.toggleOnFalse(new DriveToSubstationSensor(drivetrain, gyro, ultrasonic));
			}


			//Dpad down button
			BooleanSupplier dPadDown = driverController.getDpadSupplier(Xbox.Dpad.kDown);
			Trigger dPadDownTrigger = new Trigger(dPadDown);
			// if(shoulder != null)
			// {
			// 	dPadDownTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kStartingPosition));
			// }
			
			// Default Command
			if(drivetrain != null)
			{
				drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
			}
			// drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, () -> 0.5, () -> 0.0, () -> 0.0, false));
        }
	}

	private void configureOperatorBindings()
	{
		if(operatorController != null)
		{
			//Left trigger 
			BooleanSupplier leftTrigger = operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
			Trigger leftTriggerTrigger = new Trigger(leftTrigger);

			//Right trigger 
			BooleanSupplier rightTrigger = operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
			Trigger rightTriggerTrigger = new Trigger(rightTrigger);

			//Left bumper
			BooleanSupplier leftBumper = operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
			Trigger leftBumperTrigger = new Trigger(leftBumper);
			
			//Right bumper
			BooleanSupplier rightBumper = operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
			Trigger rightBumperTrigger = new Trigger(rightBumper);

			//Dpad up button
			BooleanSupplier dPadUp = operatorController.getDpadSupplier(Xbox.Dpad.kUp);
			Trigger dPadUpTrigger = new Trigger(dPadUp);
			
			//Dpad down button
			BooleanSupplier dPadDown = operatorController.getDpadSupplier(Xbox.Dpad.kDown);
			Trigger dPadDownTrigger = new Trigger(dPadDown);
			
			//Dpad left button
			BooleanSupplier dPadLeft = operatorController.getDpadSupplier(Xbox.Dpad.kLeft);
			Trigger dPadLeftTrigger = new Trigger(dPadLeft);
		
			//Dpad right button
			BooleanSupplier dPadRight = operatorController.getDpadSupplier(Xbox.Dpad.kRight);
			Trigger dPadRightTrigger = new Trigger(dPadRight);

			// Start Button
			BooleanSupplier startButton = operatorController.getButtonSupplier(Xbox.Button.kStart);
			Trigger startButtonTrigger = new Trigger(startButton);

			// Start Button and dPad Up
			Trigger startAndUpTrigger  = startButtonTrigger.and(dPadUpTrigger);

			// Start Button and dPad Left
			Trigger startAndLeftTrigger  = startButtonTrigger.and(dPadLeftTrigger);

			// Start Button and dPad Down
			Trigger startAndDownTrigger  = startButtonTrigger.and(dPadDownTrigger);

			//X button
			BooleanSupplier xButton = operatorController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);

			//Y button 
			BooleanSupplier yButton = operatorController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);

			//B button 
			BooleanSupplier bButton = operatorController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(bButton);

			//A button 
			BooleanSupplier aButton = operatorController.getButtonSupplier(Xbox.Button.kA);
			Trigger aButtonTrigger = new Trigger(aButton);
			
			// Default Command
		}
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand()
	{
		if(mainShuffleboard != null && mainShuffleboard.autoCommandList != null)
        {
            return mainShuffleboard.autoCommandList;
        }
		else
		{
			Command command = null;

			return command;
		}
	}

	public void resetRobot()
	{
		gyro.reset();
		System.out.println("Gyro Reset");
	}

	
	/////////////////////////////////////////
	// Command Event Loggers
	/////////////////////////////////////////
	void configureSchedulerLog()
	{
		boolean useShuffleBoardLog = true;
		StringLogEntry commandLogEntry = null;

		if(useShuffleBoardLog || useDataLog)
		{
		// Set the scheduler to log events for command initialize, interrupt,
		// finish, execute
		// Log to the ShuffleBoard and the WPILib data log tool.
		// If ShuffleBoard is recording these events are added to the recording. Convert
		// recording to csv and they show nicely in Excel. 
		// If using data log tool, the recording is automatic so run that tool to retrieve and convert the log.
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandInitialize(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " initialized");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command initialized", command.getName(), EventImportance.kNormal);
						System.out.println("Command initialized " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandInterrupt(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " interrupted");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command interrupted", command.getName(), EventImportance.kNormal);
						System.out.println("Command interrupted " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandFinish(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " finished");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command finished", command.getName(), EventImportance.kNormal);
						System.out.println("Command finished " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandExecute( // this can generate a lot of events
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " executed");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command executed", command.getName(), EventImportance.kNormal);
						// System.out.println("Command executed " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________
		}
	}

}


// ------------------------------------------------------------------------------------------
// COMMAND EXAMPLES
// ------------------------------------------------------------------------------------------
// 
// Here are other options ways to create "Suppliers"
// DoubleSupplier leftYAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftY) * 2.0; };
// DoubleSupplier leftXAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftX) * 2.0; };
// DoubleSupplier rightXAxis = () -> { return driverController.getRawAxis(Xbox.Axis.kRightX) * 2.0; };
// BooleanSupplier aButton =   () -> { return driverController.getRawButton(Xbox.Button.kA); };
//
// ------------------------------------------------------------------------------------------
//
// Here are 4 ways to perform the "LockWheels" command
// Press the X button to lock the wheels, unlock when the driver moves left or right axis
// 
// Option 1
// xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
//						.until(driverController.tryingToMoveRobot()) );
//
// Option 2
// xButtonTrigger.onTrue(new LockWheels(drivetrain)
// 						.until(driverController.tryingToMoveRobot()));
//
// Option 3
// xButtonTrigger.onTrue(new FunctionalCommand(
// 		() -> {}, 								// onInit
// 		() -> { drivetrain.lockWheels(); }, 	// onExec
// 		(interrupted) -> {}, 					// onEnd
// 		driverController.tryingToMoveRobot(),	// isFinished
// 		drivetrain ) );							// requirements
// 
// Option 4
// xButtonTrigger.onTrue( run( () -> drivetrain.lockWheels() )	//run(drivetrain::lockWheels)
// 						.until(driverController.tryingToMoveRobot()) );
//
