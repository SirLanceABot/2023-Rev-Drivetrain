package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.AutonomousTabData.StartingLocation;
// import frc.robot.shuffleboard.AutonomousTabData.TeamColor;
import frc.robot.shuffleboard.AutonomousTabData.PlayPreload;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.AutonomousCommands;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ScoreSecondPiece;
import frc.robot.shuffleboard.AutonomousTabData.ContainingPreload;
import frc.robot.shuffleboard.AutonomousTabData.DriveToSecondPiece;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.ArcadeDriveDirection;

public class AutoCommandList extends SequentialCommandGroup
{
    //This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    private static enum CommandState
    {
        kInit, kExecute, kEnd, kAllDone;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private AutonomousTabData autonomousTabData;
    // private final Autonomous1 autonomous1;
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private String commandString = "\n***** AUTONOMOUS COMMAND LIST *****\n";
    // private final Command currentCommand;
    
    

    // private static final ArrayList<Command> commandList = new ArrayList<>();
 
    // private  teamColor;
    // private static startingLocation;
    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandList(RobotContainer robotContainer)
    {
        this.autonomousTabData = robotContainer.mainShuffleboard.autonomousTab.getAutonomousTabData();
        // this.autonomous1 = autonomous1;
        this.drivetrain = robotContainer.drivetrain;
        this.gyro = robotContainer.gyro;
        
        // commandList.clear();
        build();

        System.out.println(this);
    }

    // *** CLASS & INSTANCE METHODS ***
    private void build()
    {
        // commandList.clear();
        int location = 0;
        // RowPlayedPiece1 row1 = autonomousTabData.rowPlayedPiece1.getSelected();

        add(new StopDrive(drivetrain));

        location = getAllianceAndLocation();

        switch(autonomousTabData.autonomousCommands)
        {
            case kNeither:
                if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new WaitCommand(0.25));
                    //Commented out this line below
                }

                if(autonomousTabData.driveToSecondPiece == DriveToSecondPiece.kYes)
                {
                    // turnRobot180();
                    goToSecondGamePiece();
                    // add( new AutoDriveDistance(drivetrain, gyro, 0.75, 0, 0.5));
                }

                if(autonomousTabData.pickUpGamePieces == PickUpGamePieces.kYes)
                {
                
                }

                if(autonomousTabData.scoreSecondPiece == ScoreSecondPiece.kYes)
                {
                    turnRobot180();
                    driveOut(4.4);
                }

                if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
                {
                    goToChargingStation(location);
                }
                break;
            case kChargingStation:
                driveOut(4.26);
                goToChargingStation(location);
                break;
            case kTwoGamePieces:
                add( new WaitCommand(0.5));
                goToSecondGamePiece();
                turnRobot180();
                break;
        }
    
    }

    private int getAllianceAndLocation()
    {
        DriverStation.Alliance alliance = DriverStation.getAlliance();
        boolean isRedLeft = (alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kLeft);
        boolean isBlueRight = (alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kRight);
        boolean isRedRight = (alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kRight);
        boolean isBlueLeft = (alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kLeft);
        if(isRedLeft || isBlueRight)
        {
            return 1;
        }
        else if(isRedRight || isBlueLeft)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }



    private void driveOut(double distance)
    {
        add(new ArcadeAutoDriveDistance(drivetrain, gyro, 0.5, 0.0, ArcadeDriveDirection.kStraight, distance));
    }

    // private void strafeDrive(double distance)
    // {
    //     add(new AutoDriveDistance(drivetrain, gyro, 0.0, 0.5, distance)); 
    // }

    private void turnRobot180()
    {
        // add(new AutoDriveDistance(drivetrain, gyro, 0.5, -0.5, 2.0));
    }

    private void goToSecondGamePiece()
    {
        add(new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 3.75));
    }

    private void goToChargingStation(double location)
    {
        switch(autonomousTabData.startingLocation)
        {
            case kLeft:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                                new AutoDriveDistance(drivetrain, gyro, 0.0, 1.5, 1.75),
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8))));
                }
                else
                {
                    add( new SequentialCommandGroup(
                            new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                            new AutoDriveDistance(drivetrain, gyro, 0.0, 1.5, 1.75),
                            new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)));
                }

                add( new AutoBalance(drivetrain, gyro, 1));
                break;
            
            case kMiddle:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 4.3),  //4.1
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 2.1),   //1.9
                                // new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 1.75),
                                // new AutoBalance(drivetrain, gyro, -1)),
                                new AutoBalance(drivetrain, gyro, 1)))); 
                }
                else
                {
                    add( new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 4.1));
                    add( new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.9));
                    add( new AutoBalance(drivetrain, gyro, 1));

                    // add( new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 1.75));
                    // add( new AutoBalance(drivetrain, gyro, -1));
                }
                break;
            
            case kRight:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                                new AutoDriveDistance(drivetrain, gyro, 0.0, -1.5, 1.75),
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8))));
                }
                else
                {
                    add( new SequentialCommandGroup(
                            new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                            new AutoDriveDistance(drivetrain, gyro, 0.0, -1.5, 1.75),
                            new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)));
                }

                add( new AutoBalance(drivetrain, gyro, 1));
                break;
        }

		add( new LockWheels(drivetrain));

        // if(autonomousTabData.playPreload == PlayPreload.kYes)
        // {
        //     
        // }
        // else
        // {
        //     // add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.75));
        //     add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 3.75));
        // }

        // if(autonomousTabData.startingLocation == StartingLocation.kLeft)
        // {
        //     add( new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStrafe, 1.6));
        // }

        // if(autonomousTabData.startingLocation == StartingLocation.kRight)
        // {
        //     add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStrafe, 1.6));
        // }

        // add( new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.5));

	    
        
        // {
        //     add(new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, 4.4));
        //     add(new AutoDriveDistance(drivetrain, gyro, 0.0, distance, 1.5));
        //     add(new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.4));
        //     add( new AutoBalance(drivetrain, gyro));
		// 	add( new LockWheels(drivetrain));
        // }
        
    }



    // private void addCommand(Command command)
    // {
        // Command commandList[20];
        // Command startingCommand = kDelay;
        // int counter;
        // for(counter = 0; counter <= size(commandList); counter++)
        // {
        //     startingCommand.andThen(commandList[counter]);
        // }
        
    //     commandList.add(command);
    // }

    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList

    private void add(Command command)
    {

        addCommands(command);
        // commandList.add(command);
        commandString += command + "\n";
    }

    public String toString()
    {
        return commandString;
    }



}
