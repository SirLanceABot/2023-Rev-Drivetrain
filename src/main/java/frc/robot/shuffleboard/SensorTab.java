package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.sensors.Gyro4237;
// import frc.robot.RobotContainer;
// import frc.robot.subsystems.SwerveModule;
// import frc.robot.RobotContainer;
// import edu.wpi.first.wpilibj.interfaces.Gyro;

public class SensorTab
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor");
    private Gyro4237 gyro;
    private Drivetrain drivetrain;
    // private SwerveModule swerveModule;
    private Translation2d startingPosition;
    private Double encoderValue = 0.0;
    private GenericEntry shoulderEncoderBox;
    private GenericEntry grabberEncoderBottomBox;
    private GenericEntry grabberEncoderTopBox;
    private GenericEntry grabberMotorBottomCurrentBox;
    private GenericEntry grabberMotorTopCurrentBox;
    private GenericEntry gyroBox;
    private GenericEntry armEncoderBox;
    private GenericEntry flsEncoderBox;
    private GenericEntry frsEncoderBox;
    private GenericEntry blsEncoderBox;
    private GenericEntry brsEncoderBox;
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    SensorTab(Drivetrain drivetrain, Gyro4237 gyro)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.drivetrain = drivetrain;
        this.gyro = gyro;
        if(drivetrain != null)
        {
            flsEncoderBox = createFrontLeftTurnEncoderBox();
            frsEncoderBox = createFrontRightTurnEncoderBox();
            blsEncoderBox = createBackLeftTurnEncoderBox();
            brsEncoderBox = createBackRightTurnEncoderBox();
        }
        if(gyro != null)
        {
            gyroBox = createGyroBox();
        }
        

        

        System.out.println(fullClassName + ": Constructor Finished");
    }

    private GenericEntry createFrontLeftTurnEncoderBox()
    {
        return sensorTab.add("Front Left Turn Encoder", drivetrain.fls())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(5, 0)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createFrontRightTurnEncoderBox()
    {
        return sensorTab.add("Front Right Turn Encoder", drivetrain.frs())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(5, 3)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackLeftTurnEncoderBox()
    {
        return sensorTab.add("Back Left Turn Encoder", drivetrain.bls())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(5, 6)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createBackRightTurnEncoderBox()
    {
        return sensorTab.add("Back Right Turn Encoder", drivetrain.brs())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(5, 9)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    private GenericEntry createGyroBox()
    {
        return sensorTab.add("Gyro", gyro.getPitch())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(1, 9)  // sets position of widget
        .withSize(4, 2)    // sets size of widget
        .getEntry();
    }

    public void updateEncoderData()
    {
        if(drivetrain != null)
        {
            flsEncoderBox.setDouble(drivetrain.fls());
            frsEncoderBox.setDouble(drivetrain.frs());
            blsEncoderBox.setDouble(drivetrain.bls());
            brsEncoderBox.setDouble(drivetrain.brs());
        }
        if(gyro != null)
        {
            gyroBox.setDouble(gyro.getPitch());
        }
        
    }
}