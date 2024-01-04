package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class Accelerometer4237 extends Sensor4237
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicIO
    {
        private double x;
        private double y;
        private double z;
    }

    private final Accelerometer accelerometer = new BuiltInAccelerometer(Accelerometer.Range.k2G);
    private final PeriodicIO periodicIO = new PeriodicIO();

    public Accelerometer4237()
    {}

    public double getX()
    {
        return periodicIO.x;
    }

    public double getY()
    {
        return periodicIO.y;
    }

    public double getZ()
    {
        return periodicIO.z;
    }
    



    
    @Override
    public void readPeriodicInputs()
    {
        periodicIO.x = accelerometer.getX();
        periodicIO.y = accelerometer.getY();
        periodicIO.z = accelerometer.getZ();
    }

    @Override
    public void writePeriodicOutputs()
    {}

    @Override
    public String toString()
    {
        return String.format("Acc %f, %f, %f \n", periodicIO.x, periodicIO.y, periodicIO.z);
    }

}
