package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SwerveDrive extends CommandBase
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
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier turn;
    private boolean fieldRelative;
    
    public SwerveDrive(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, boolean fieldRelative)
    {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.turn = turn;
        this.fieldRelative = fieldRelative;

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
            // change robot speed
            drivetrain.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), turn.getAsDouble(), fieldRelative);
    }

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
            return false;
        else
            return true;
    }

}