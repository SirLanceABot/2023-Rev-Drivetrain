package frc.robot.commands;

import java.lang.invoke.MethodHandles;
// import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
 //hello

public class LockWheels extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final Drivetrain drivetrain;
    // private DoubleSupplier xSpeed;
    // private DoubleSupplier ySpeed;
    // private DoubleSupplier turn;

    
    public LockWheels(Drivetrain drivetrain)//, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn)
    {
        this.drivetrain = drivetrain;
        // this.xSpeed = xSpeed;
        // this.ySpeed = ySpeed;
        // this.turn = turn;

        if(this.drivetrain != null)
            addRequirements(drivetrain);
    }

    @Override 
    public void initialize()
    {}

    @Override
    public void execute()
    {
        if(drivetrain != null)
            drivetrain.lockWheels();
    }

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {   
        // if(Math.abs(xSpeed.getAsDouble()) > 0.01 || Math.abs(ySpeed.getAsDouble()) > 0.01 || Math.abs(turn.getAsDouble()) > 0.01)
        //     return true;
        // else
        //     return false;

        if(drivetrain != null)
            return false;

        return true;
    }

    @Override
    public String toString()
    {
        return "LockWheels()";
    }
   
}