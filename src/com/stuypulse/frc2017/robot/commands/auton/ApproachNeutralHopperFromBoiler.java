package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachNeutralHopperFromBoiler extends CommandGroup {

    double TURN_AWAY_FROM_BOILER = -45;
    double MOVE_TOWARD_HOPPER = 249.3;
    double TURN_TO_HOPPER = 90;
    double MOVE_TO_HOPPER = 77;
   
    public ApproachNeutralHopperFromBoiler() {
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        
        addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_TO_HOPPER_BACKUP_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(TURN_AWAY_FROM_BOILER * direction));
        addSequential(new DriveForwardEncodersCommand(MOVE_TOWARD_HOPPER));
        addSequential(new RotateDegreesGyroCommand(TURN_TO_HOPPER * direction));
        addSequential(new DriveForwardEncodersCommand(MOVE_TO_HOPPER));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
