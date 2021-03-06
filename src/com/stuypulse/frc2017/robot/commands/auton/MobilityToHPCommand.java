package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MobilityToHPCommand extends CommandGroup {
    public static final double MOBILITY_TO_NEUTRAL_ZONE_DISTANCE = 415.3;//427.3 - 12 inches deviation

    public MobilityToHPCommand() {
    	
    	addSequential(new DriveForwardEncodersCommand(MOBILITY_TO_NEUTRAL_ZONE_DISTANCE));
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
