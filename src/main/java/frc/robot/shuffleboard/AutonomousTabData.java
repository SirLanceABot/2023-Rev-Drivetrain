package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

public class AutonomousTabData 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public static enum AutonomousCommands
    {
        kNeither, kChargingStation, kTwoGamePieces
    }

    //-------------------------------------------------------------------//

    public static enum StartingLocation
    {
        kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum ContainingPreload
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum PlayPreload
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//


    public static enum RowPlayedPiece1
    {
        kNone, kBottom, kMiddle, kTop;
    }

    //-------------------------------------------------------------------// 

    public static enum MoveOntoChargingStation
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum DriveToSecondPiece
    {
        kYes, kNo
    }

    //-------------------------------------------------------------------//

    public static enum PickUpGamePieces
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//
    
    public static enum ScoreSecondPiece
    {
        kYes, kNo;
    }

    //-------------------------------------------------------------------//

    public static enum RowPlayedPiece2
    {
        kNone, kBottom, kMiddle, kTop;
    }

    //-------------------------------------------------------------------// 
    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public PlayPreload playPreload = PlayPreload.kYes;
    public MoveOntoChargingStation moveOntoChargingStation = MoveOntoChargingStation.kYes;
    public PickUpGamePieces pickUpGamePieces = PickUpGamePieces.kNo;
    public RowPlayedPiece1 rowPlayedPiece1 = RowPlayedPiece1.kBottom;
    public RowPlayedPiece2 rowPlayedPiece2 = RowPlayedPiece2.kNone;
    public ContainingPreload containingPreload = ContainingPreload.kYes;
    public AutonomousCommands autonomousCommands = AutonomousCommands.kNeither;
    public DriveToSecondPiece driveToSecondPiece = DriveToSecondPiece.kNo;
    public ScoreSecondPiece scoreSecondPiece = ScoreSecondPiece.kNo;
    

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Autonomous Commands         :" + autonomousCommands + "\n";
        str += "Starting Location           : "  + startingLocation   + "\n";
        str += "Containing Preload          :" + containingPreload + "\n";
        str += "Play Preload                :" + playPreload  + "\n";
        str += "Row of First Game Piece     :" + rowPlayedPiece1  + "\n";
        str += "Move Onto Charging Station  : "  + moveOntoChargingStation   + "\n";
        str += "Drive to Second Piece      :" + autonomousCommands + "\n";
        str += "Pick Up Game Pieces         :" + pickUpGamePieces  + "\n";
        str += "Score Second Piece         :" + scoreSecondPiece  + "\n";
        str += "Row of Second Game Piece    :" + rowPlayedPiece2  + "\n";
        
        return str;
    }

    // public void updateData(AutonomousTabData atd)
    // {
        // autonomousCommands = atd.autonomousCommands;
        // startingLocation = atd.startingLocation;
        // containingPreload = atd.containingPreload;
        // playPreload = atd.playPreload;
        // rowPlayedPiece1 = atd.rowPlayedPiece1;
        // moveOntoChargingStation = atd.moveOntoChargingStation;
        // driveToSecondPiece = atd.driveToSecondPiece;
        // pickUpGamePieces = atd.pickUpGamePieces;
        // scoreSecondPiece = atd.scoreSecondPiece;
        // rowPlayedPiece2 = atd.rowPlayedPiece2;
    // }


}
