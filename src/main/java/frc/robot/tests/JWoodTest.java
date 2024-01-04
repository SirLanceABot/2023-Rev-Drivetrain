package frc.robot.tests;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import frc.robot.RobotContainer;

public class JWoodTest implements Test
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Put all class and instance variables here.
    private final RobotContainer robotContainer;
    PowerDistribution pdp = new PowerDistribution();
    private final CANSparkMax motor = new CANSparkMax(3,  MotorType.kBrushless); 
    private final Joystick joystick = new Joystick(0);


    // *** CLASS CONSTRUCTOR ***
    public JWoodTest(RobotContainer robotContainer)
    {
        this.robotContainer = robotContainer;

        motor.restoreFactoryDefaults();

    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {}

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        motor.set(0.5);
        System.out.println(pdp.getTotalEnergy() + " " + pdp.getCurrent(14) + " " + motor.getOutputCurrent());
    }
    
    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        motor.set(0.0);
    }

    // *** METHODS ***
    // Put any additional methods here.

    
}
