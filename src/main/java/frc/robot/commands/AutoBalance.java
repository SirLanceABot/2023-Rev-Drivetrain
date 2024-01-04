package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro4237;
import frc.robot.subsystems.Drivetrain;


public class AutoBalance extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum BalanceState
    {
        // level means gyro is at zero, but not done balancing
        // engaged  means gyro has been at zero for 1 second, and is done balancing
        kNotLevel, kLevel, kBeenLevel;
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private int initialDirection;
    private final Timer finishBalanceTimer = new Timer();
    private BalanceState balanceState = BalanceState.kNotLevel;

    private double currentPitch;
    // private double previousPitch;
    private double maxPitch = 0.0;
    private double currentYaw;
    private double error;
    private double drivePower;
    private double direction;
    

    // private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double CS_BALANCE_DRIVE_KP = 0.025;
    private final double CS_BALANCE_TOLERANCE = 3.0;
    private final double CS_BALANCE_MIN_TIME_LEVEL = 0.75;
    private final double CS_BALANCE_MAX_SPEED = 1.5;
    private boolean approach = true;
    // private int approachCounter = 0;
    // private int balanceCounter = 0;

    /**
    * Creates a new AutoBalance.
    * Balances the robot on the charge station with no help from the driver.
    *
    * @param drivetrain Drivetrain subsytem.
    * @param gyro Gyro4237 sensor.
    * @param initialDirection Initial direction to drive before balancing +forward, -reverse
    */
    public AutoBalance(Drivetrain drivetrain, Gyro4237 gyro, int initialDirection) 
    {
        // System.out.println("Constructor");
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.initialDirection = initialDirection;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(drivetrain != null)
        {
            addRequirements(this.drivetrain);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        approach = true;
        drivePower = CS_BALANCE_MAX_SPEED;
        maxPitch = 0.0;
        // approachCounter = 0;
        // balanceCounter = 0;
        balanceState = BalanceState.kNotLevel;
        // driveForwardTimer.reset();
        // driveForwardTimer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(drivetrain != null)
        {
            // System.out.println(driveForwardTimer.get());
        
            // if(driveForwardTimer.get() < 1.0)
            // {   
            //     System.out.println("drive");
            //     drivetrain.drive(-CS_BALANCE_MAX_SPEED, 0.0, 0.0, true);
            // }

            // previousPitch = currentPitch;
            currentPitch = gyro.getPitch();
            error = 0.0 - currentPitch;   //controlling to zero

            // if(Math.abs(currentPitch) > Math.abs(maxPitch))
            // {
            //     maxPitch = currentPitch;
            // }
            // currentYaw = (int)Math.abs(gyro.getYaw()) % 360;

            if(Math.abs(currentPitch) > 10.0)
            {
                approach = false;
            }

            // SmartDashboard.putNumber("Current Pitch", currentPitch);
            // SmartDashboard.putNumber("Current Yaw", currentYaw);


            // drivePower =  Math.min(CS_BALANCE_DRIVE_KP * error, 1);
    
            // drivePower = (CS_BALANCE_DRIVE_KP * error);

            if(approach)
            {
                // approachCounter++;
                // System.out.println("Approach " + approachCounter);
                drivePower = CS_BALANCE_MAX_SPEED;
                // drivePower = Math.copySign(CS_BALANCE_MAX_SPEED, drivePower);
            }
            else
            {
                // balanceCounter++;
                // System.out.println("Balance: " + balanceCounter + " Approach: " + approachCounter);
                drivePower = CS_BALANCE_DRIVE_KP * error;
                // if(Math.abs(drivePower) > CS_BALANCE_MAX_SPEED || Math.abs(error) > 12.0)
                // {
                //     drivePower = Math.copySign(CS_BALANCE_MAX_SPEED, drivePower);
                // }
                // else if(Math.abs(error) < 12.0 && Math.abs(error) > 10.0)
                // {
                //     drivePower = Math.copySign(0.5 * CS_BALANCE_MAX_SPEED, drivePower);
                // }
                // // else if(Math.abs(error) < 11.0)
                // // else if(Math.abs(currentPitch) < Math.abs(previousPitch))
                // else if(Math.abs(maxPitch) - Math.abs(currentPitch) > 1.0)
                // {
                //     // drivePower = 0.0;
                //     drivePower = Math.copySign(0.5 * CS_BALANCE_MAX_SPEED, drivePower);
                // }
            }
            

            // if((currentYaw > 135 && currentYaw < 225))
            // {
            //     drivePower = -drivePower;
            // }

            if(drivetrain != null)
            {
                if(approach)
                {
                    drivetrain.drive(drivePower * initialDirection, 0.0, 0.0, false); 
                }
                else
                {
                    drivetrain.drive(drivePower, 0.0, 0.0, false);
                }
            }
        }     
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.resetSlewRateLimiter();
            drivetrain.drive(0.0, 0.0, 0.0, false);
            drivetrain.lockWheels();
            // new LockWheels(drivetrain).schedule();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        if(approach)
        {
            return false;
        }
        else if(Math.abs(error) > CS_BALANCE_TOLERANCE)
        {
            balanceState = BalanceState.kNotLevel;
            return false;
        }
        else if(Math.abs(error) <= CS_BALANCE_TOLERANCE && balanceState == BalanceState.kNotLevel)
        {
            finishBalanceTimer.reset();
            finishBalanceTimer.start();
            balanceState = BalanceState.kLevel;
            return false;
        }
        else if(balanceState == BalanceState.kLevel)
        {
            balanceState = BalanceState.kBeenLevel;
            return false;
        }
        else if(balanceState == BalanceState.kBeenLevel && finishBalanceTimer.get() < CS_BALANCE_MIN_TIME_LEVEL)
        {   
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public String toString()
    {
        return "AutoBalance(drivetrain, gyro)";
    }

}
