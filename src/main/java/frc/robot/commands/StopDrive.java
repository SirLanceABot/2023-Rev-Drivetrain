package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class StopDrive extends CommandBase
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
    public StopDrive(Drivetrain drivetrain)
    {
        this.drivetrain = drivetrain;
        if (drivetrain != null)
        {
            addRequirements(drivetrain);
        }
    }

    @Override
    public void initialize()
    {
        if(drivetrain != null)
            drivetrain.stopMotor();
    }

    @Override
    public void execute()
    {} 

    @Override 
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "StopDrive()";
    }
}
