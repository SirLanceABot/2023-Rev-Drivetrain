package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro4237;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Drivetrain.ArcadeDriveDirection;

public class AutoDriveDistance extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private Drivetrain drivetrain;
    private Gyro4237 gyro;
    private double xSpeed;
    private double ySpeed;
    private double initialYaw;
    private double currentYaw;
    private double correctionRotate;
    private double distanceToDriveMeters;
    private Translation2d startingPosition;


    public AutoDriveDistance(Drivetrain drivetrain, Gyro4237 gyro, double xSpeed, double ySpeed, double distanceToDriveMeters)
    {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.distanceToDriveMeters = distanceToDriveMeters;

        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
    }   

    @Override
    public void initialize()
    {
        initialYaw = gyro.getYaw();
        // isFinished = false;
        if(drivetrain != null)
        {
            startingPosition = drivetrain.getCurrentTranslation();
            //drivetrain.cofigOpenLoopRamp(0.75);
        }
    }

    @Override
    public void execute()
    {
        currentYaw = gyro.getYaw();
        correctionRotate = (initialYaw - currentYaw) * 0.2;
        if(drivetrain != null)
        {
            drivetrain.drive(xSpeed, ySpeed, 0.0, false);
            // drivetrain.drive(xSpeed, ySpeed, correctionRotate, false);
            // drivetrain.arcadeDrive(xSpeed, correctionRotate, arcadeDriveDirection.value);

            // double distanceDrivenMeters = drivetrain.getDistanceDrivenMeters(startingPosition);
            // if(Math.abs(distanceDrivenMeters) < Math.abs(distanceToDriveMeters))
            // {
            //     drivetrain.drive(xSpeed, ySpeed, 0.0, false);
            // }
            // else
            // {
            //     drivetrain.stopMotor();
            // }
        }
    }

    @Override 
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.stopMotor();
            //drivetrain.configOpenLoopRamp(1.0);
        }
    }

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
        {
            double distanceDrivenMeters = drivetrain.getDistanceDrivenMeters(startingPosition);
            if(Math.abs(distanceDrivenMeters) < Math.abs(distanceToDriveMeters))
            {
                return false;
            } 
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "AutoDriveDistance(" + xSpeed + ", " + ySpeed + ", " + distanceToDriveMeters + ")";
    }
}


