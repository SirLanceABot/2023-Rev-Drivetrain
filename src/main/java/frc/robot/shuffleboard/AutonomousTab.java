package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.shuffleboard.AutonomousTabData.AutonomousCommands;
import frc.robot.shuffleboard.AutonomousTabData.DriveToSecondPiece;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import edu.wpi.first.util.sendable.SendableRegistry;


public class AutonomousTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private enum ButtonState
    {
        kPressed, kStillPressed, kReleased, kStillReleased
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");

    private final AutonomousTabData autonomousTabData = new AutonomousTabData();
  
    // Create the Box objects    
    private SendableChooser<AutonomousTabData.AutonomousCommands> autonomousCommandsBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.StartingLocation> startingLocationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ContainingPreload> containingPreloadBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PlayPreload> playPreloadBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece1> rowPlayedPiece1Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveOntoChargingStation> moveOntoChargingStationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.DriveToSecondPiece> driveToSecondPieceBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PickUpGamePieces> pickUpGamePiecesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ScoreSecondPiece> scoreSecondPieceBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece2> rowPlayedPiece2Box = new SendableChooser<>();
    private GenericEntry successfulDownload;
    private GenericEntry errorMessageBox;

    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private ButtonState previousButtonState = ButtonState.kStillReleased;
    private boolean isDataValid = true;
    private String errorMessage = "No Errors";

    // *** CLASS CONSTRUCTOR ***
    AutonomousTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        createAutonomousCommandsBox();
        createStartingLocationBox();
        createPlayPreloadBox();
        createContainingPreloadBox();
        createRowPlayedPiece1Box();
        createMoveOntoChargingStationBox();
        createDriveToSecondPieceBox();
        createPickUpGamePiecesBox();
        createScoreSecondPieceBox();
        createRowPlayedPiece2Box();
        
        createSendDataButton();
        successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        errorMessageBox = createErrorMessageBox();

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***

    /**
    * <b>Autonomous Commands </b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createAutonomousCommandsBox()
    {
        //create and name the Box
        SendableRegistry.add(autonomousCommandsBox, "Auto Commands");
        SendableRegistry.setName(autonomousCommandsBox, "Auto Commands");

        //add options to Box
        autonomousCommandsBox.setDefaultOption("None", AutonomousTabData.AutonomousCommands.kNeither);
        // autonomousCommandsBox.addOption("Charge Station", AutonomousTabData.AutonomousCommands.kChargingStation);
        // autonomousCommandsBox.addOption("2 Pieces", AutonomousTabData.AutonomousCommands.kTwoGamePieces);

        //put the widget on the shuffleboard
        autonomousTab.add(autonomousCommandsBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(0, 0)
            .withSize(7, 2);
    }

    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createStartingLocationBox()
    {
        //create and name the Box
        SendableRegistry.add(startingLocationBox, "Starting Location");
        SendableRegistry.setName(startingLocationBox, "Starting Location");
        
        //add options to  Box
        startingLocationBox.setDefaultOption("Left", AutonomousTabData.StartingLocation.kLeft);
        startingLocationBox.addOption("Middle", AutonomousTabData.StartingLocation.kMiddle);
        startingLocationBox.addOption("Right", AutonomousTabData.StartingLocation.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(startingLocationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 3)
            .withSize(5, 2);
    }

    /**
    * <b>Containing Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createContainingPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(containingPreloadBox, "Containing Preload?");
        SendableRegistry.setName(containingPreloadBox, "Containing Preload?");

        //add options to Box
        containingPreloadBox.addOption("No", AutonomousTabData.ContainingPreload.kNo);
        containingPreloadBox.setDefaultOption("Yes", AutonomousTabData.ContainingPreload.kYes);
        
        //put the widget on the shuffleboard
        autonomousTab.add(containingPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(10, 0)
            .withSize(4, 3);
    }

    /**
    * <b>Play Preload</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPlayPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(playPreloadBox, "Play Preload?");
        SendableRegistry.setName(playPreloadBox, "Play Preload?");
        
        //add options to  Box
        playPreloadBox.addOption("No", AutonomousTabData.PlayPreload.kNo);
        playPreloadBox.setDefaultOption("Yes", AutonomousTabData.PlayPreload.kYes);
        

        //put the widget on the shuffleboard
        autonomousTab.add(playPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(14, 0)
            .withSize(4, 3);
    }

    /**
    * <b>Row for Piece 1</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRowPlayedPiece1Box()
    {
        //create and name the Box
        SendableRegistry.add(rowPlayedPiece1Box, "Row To Play Preload");
        SendableRegistry.setName(rowPlayedPiece1Box, "Row To Play Preload");

        //add options to Box
        rowPlayedPiece1Box.addOption("None", AutonomousTabData.RowPlayedPiece1.kNone);
        rowPlayedPiece1Box.addOption("Bottom", AutonomousTabData.RowPlayedPiece1.kBottom);
        rowPlayedPiece1Box.addOption("Middle", AutonomousTabData.RowPlayedPiece1.kMiddle);
        rowPlayedPiece1Box.setDefaultOption("Top", AutonomousTabData.RowPlayedPiece1.kTop);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece1Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(11, 3)
            .withSize(6, 2);
    }

    /**
    * <b>Move Onto Charging Station</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMoveOntoChargingStationBox()
    {
        //create and name the Box
        SendableRegistry.add(moveOntoChargingStationBox, "Charge Station");
        SendableRegistry.setName(moveOntoChargingStationBox, "Charge Station");

        //add options to Box
        moveOntoChargingStationBox.setDefaultOption("No", AutonomousTabData.MoveOntoChargingStation.kNo);
        moveOntoChargingStationBox.addOption("Yes", AutonomousTabData.MoveOntoChargingStation.kYes);

        //put the widget on the shuffleboard
        autonomousTab.add(moveOntoChargingStationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 6)
            .withSize(4, 3);
    }

    /**
    * <b>Drive to Second Piece</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createDriveToSecondPieceBox()
    {
        //create and name the Box
        SendableRegistry.add(driveToSecondPieceBox, "Drive To Piece 2");
        SendableRegistry.setName(driveToSecondPieceBox, "Drive To Piece 2");

        //add options to Box
        driveToSecondPieceBox.setDefaultOption("No", AutonomousTabData.DriveToSecondPiece.kNo);
        driveToSecondPieceBox.addOption("Yes", AutonomousTabData.DriveToSecondPiece.kYes);
        //put the widget on the shuffleboard
        autonomousTab.add(driveToSecondPieceBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(5, 6)
            .withSize(4, 3);
    }

    /**
    * <b>Pick Up Game Pieces</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPickUpGamePiecesBox()
    {
        //create and name the Box
        SendableRegistry.add(pickUpGamePiecesBox, "Pick Up Game Piece 2");
        SendableRegistry.setName(pickUpGamePiecesBox, "Pick Up Game Piece 2");

        //add options to Box
        pickUpGamePiecesBox.setDefaultOption("No", AutonomousTabData.PickUpGamePieces.kNo);
        pickUpGamePiecesBox.addOption("Yes", AutonomousTabData.PickUpGamePieces.kYes);

        //put the widget on the shuffleboard
        autonomousTab.add(pickUpGamePiecesBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(20, 0)
            .withSize(4, 3);
    }

    /**
    * <b>Score Second Piece</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createScoreSecondPieceBox()
    {
        //create and name the Box
        SendableRegistry.add(scoreSecondPieceBox, "Score Piece 2");
        SendableRegistry.setName(scoreSecondPieceBox, "Score Piece 2");

        //add options to Box
        scoreSecondPieceBox.setDefaultOption("No", AutonomousTabData.ScoreSecondPiece.kNo);
        scoreSecondPieceBox.addOption("Yes", AutonomousTabData.ScoreSecondPiece.kYes);

        //put the widget on the shuffleboard
        autonomousTab.add(scoreSecondPieceBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(24, 0)
            .withSize(4, 3);
    }

    /**
    * <b>Row for Piece 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRowPlayedPiece2Box()
    {
        //create and name the Box
        SendableRegistry.add(rowPlayedPiece2Box, "Row To Play Piece 2");
        SendableRegistry.setName(rowPlayedPiece2Box, "Row To Play Piece 2");

        //add options to Box
        rowPlayedPiece2Box.setDefaultOption("None", AutonomousTabData.RowPlayedPiece2.kNone);
        rowPlayedPiece2Box.addOption("Bottom", AutonomousTabData.RowPlayedPiece2.kBottom);
        rowPlayedPiece2Box.addOption("Middle", AutonomousTabData.RowPlayedPiece2.kMiddle);
        rowPlayedPiece2Box.addOption("Top", AutonomousTabData.RowPlayedPiece2.kTop);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(21, 3)
            .withSize(6, 2);
    }

    // TODO create boolean box to display color (yellow as undecided)

    /**
     * <b>Send Data</b> Button
     * <p>
     * Create an entry in the Network Table and add the Button to the Shuffleboard
     * Tab
     * 
     * @return
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);

        autonomousTab.add(sendDataButton)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(20, 8)
            .withSize(4, 4);
    }

    private GenericEntry createSuccessfulDownloadBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
    
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return autonomousTab.add("Successful Download?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(24, 8)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private GenericEntry createErrorMessageBox()
    {
         return autonomousTab.add("Error Messages", "No Errors")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(0, 10)
             .withSize(20, 2)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.autonomousCommands = autonomousCommandsBox.getSelected();

        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        autonomousTabData.containingPreload = containingPreloadBox.getSelected();
        autonomousTabData.playPreload = playPreloadBox.getSelected();
        autonomousTabData.rowPlayedPiece1 = rowPlayedPiece1Box.getSelected();
        autonomousTabData.moveOntoChargingStation = moveOntoChargingStationBox.getSelected();
        autonomousTabData.driveToSecondPiece = driveToSecondPieceBox.getSelected();
        autonomousTabData.pickUpGamePieces = pickUpGamePiecesBox.getSelected();
        autonomousTabData.scoreSecondPiece = scoreSecondPieceBox.getSelected();
        autonomousTabData.rowPlayedPiece2 = rowPlayedPiece2Box.getSelected();
        
        if(autonomousTabData.autonomousCommands == AutonomousCommands.kChargingStation)
        {
            autonomousTabData.pickUpGamePieces = PickUpGamePieces.kNo;
            autonomousTabData.driveToSecondPiece = DriveToSecondPiece.kNo;
            autonomousTabData.moveOntoChargingStation = MoveOntoChargingStation.kYes;
            autonomousTabData.rowPlayedPiece2 = RowPlayedPiece2.kNone;
        }

        else if(autonomousTabData.autonomousCommands == AutonomousCommands.kTwoGamePieces)
        {
            // autonomousTabData.pickUpGamePieces = PickUpGamePieces.kYes;
            autonomousTabData.driveToSecondPiece = DriveToSecondPiece.kYes;
            autonomousTabData.moveOntoChargingStation = MoveOntoChargingStation.kNo;
            // autonomousTabData.rowPlayedPiece2 = RowPlayedPiece2.kNone;
        }
    }

    public boolean isNewData()
    {
        boolean isNewData = false;
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        switch(previousButtonState)
        {
            case kStillReleased:
                if(isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kPressed;
                }
                break;

            case kPressed:
                updateIsDataValidAndErrorMessage();
                if(isDataValid)
                {
                    successfulDownload.setBoolean(true);
                    updateAutonomousTabData();
                    isNewData = true;
                    errorMessageBox.setString(errorMessage);
                }
                else
                {
                    successfulDownload.setBoolean(false);
                    DriverStation.reportWarning(errorMessage, false);
                    errorMessageBox.setString(errorMessage);
                }
                previousButtonState = ButtonState.kStillPressed;
                break;

            case kStillPressed:
                if(!isSendDataButtonPressed)
                {
                    previousButtonState = ButtonState.kReleased;
                }
                break;

            case kReleased:
                previousButtonState = ButtonState.kStillReleased;
                break;
        }

        return isNewData;
    }

    public AutonomousTabData getAutonomousTabData()
    {
        return autonomousTabData;
    }

    public void updateIsDataValidAndErrorMessage()
    {
        errorMessage = "No Errors";
        String msg = "";
        boolean isValid = true;

        boolean isNoCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kNeither);
        boolean isChargingStationCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kChargingStation);
        boolean is2GamePieceCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kTwoGamePieces);
        boolean isMiddle = (startingLocationBox.getSelected() == AutonomousTabData.StartingLocation.kMiddle);
        boolean isPiecesPlayed = (playPreloadBox.getSelected() == AutonomousTabData.PlayPreload.kYes);
        boolean isPiecesPlayedFalse = (playPreloadBox.getSelected() == AutonomousTabData.PlayPreload.kNo);
        boolean isRow1 = (rowPlayedPiece1Box.getSelected() == AutonomousTabData.RowPlayedPiece1.kNone);
        boolean isMoveOntoStation = (moveOntoChargingStationBox.getSelected() == AutonomousTabData.MoveOntoChargingStation.kYes);
        boolean isPickUpPiece = (pickUpGamePiecesBox.getSelected() == AutonomousTabData.PickUpGamePieces.kYes);
        boolean isRow2False = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.kNone);
        boolean isRow2True = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.kBottom);
        boolean isPiecesContained = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
        boolean isDrivetoSecondPiece = (driveToSecondPieceBox.getSelected() == AutonomousTabData.DriveToSecondPiece.kYes);
        
        
        if(!isMiddle && isMoveOntoStation)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }
        
        if(isRow2True && isMoveOntoStation || isPickUpPiece && isMoveOntoStation || isDrivetoSecondPiece && isMoveOntoStation)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPiecesContained && isRow1 || isPiecesPlayed && isRow1)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isDrivetoSecondPiece && isPickUpPiece)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPickUpPiece && isRow2True || !isDrivetoSecondPiece && isRow2True)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        // if(!isPiecesContained && isRow1 || !isPiecesContained && isColumn1)
        // {
        //     isValid = false;
            
        //     msg += "[ Not Possible ]  \n";
        // }
        

        
        
    }   
}
