package com.stuypulse.frc2017.robot;

import com.stuypulse.frc2017.robot.commands.auton.ApproachHPFromBoilerGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ApproachHPFromHPGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ApproachHPFromMiddleGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.DoubleSequentialCommand;
import com.stuypulse.frc2017.robot.commands.auton.MiddleGearMobilityMinimalCommand;
import com.stuypulse.frc2017.robot.commands.auton.MobilityMinimalCommand;
import com.stuypulse.frc2017.robot.commands.auton.MobilityToHPCommand;
import com.stuypulse.frc2017.robot.commands.auton.ScoreBoilerGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ScoreHPGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ScoreMiddleGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ShootFromMiddleGearCommand;
import com.stuypulse.frc2017.robot.commands.auton.ShootingFromAllianceWallCommand;
import com.stuypulse.frc2017.robot.commands.auton.ShootingFromBoilerGearCommand;
import com.stuypulse.frc2017.robot.cv.BoilerVision;
import com.stuypulse.frc2017.robot.cv.Camera;
import com.stuypulse.frc2017.robot.cv.LiftVision;
import com.stuypulse.frc2017.robot.subsystems.BallGate;
import com.stuypulse.frc2017.robot.subsystems.Blender;
import com.stuypulse.frc2017.robot.subsystems.Drivetrain;
import com.stuypulse.frc2017.robot.subsystems.GearPusher;
import com.stuypulse.frc2017.robot.subsystems.GearTrap;
import com.stuypulse.frc2017.robot.subsystems.Shooter;
import com.stuypulse.frc2017.robot.subsystems.Winch;
import com.stuypulse.frc2017.util.BoolBox;
import com.stuypulse.frc2017.util.IRSensor;
import com.stuypulse.frc2017.util.LEDSignal;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public static Drivetrain drivetrain;
    public static GearPusher gearpusher;
    public static GearTrap geartrap;
    public static Shooter shooter;
    public static Blender blender;
    public static BallGate ballgate;
    public static Winch winch;
    public static LEDSignal ledBlenderSignal;
    public static LEDSignal ledGearSensingSignal;

    public static OI oi;

    public static SendableChooser<Command> autonChooser;

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<Command>();

    private static UsbCamera boilerCamera;
    private static UsbCamera liftCamera;

    public static LiftVision liftVision;
    public static BoilerVision boilerVision;
    public static Vector[] cvVector;

    public static BoolBox stopAutoMovement = new BoolBox(false);

    IRSensor irsensor;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        shooter = new Shooter();
        blender = new Blender();
        geartrap = new GearTrap();
        gearpusher = new GearPusher();
        ballgate = new BallGate();
        winch = new Winch();
        oi = new OI();
        irsensor = new IRSensor();
        ledBlenderSignal = new LEDSignal(RobotMap.BLENDER_LED_PORT, RobotMap.BLENDER_LED_ON_VALUE);
        ledGearSensingSignal = new LEDSignal(RobotMap.GEAR_LED_PORT, RobotMap.GEAR_LED_ON_VALUE);

        setupSmartDashboardFields();
        setupAutonChooser();

        boilerVision = new BoilerVision();
        liftVision = new LiftVision();

        boilerCamera = new UsbCamera("Boiler Camera", 0);
        liftCamera = new UsbCamera("Lift Camera", 1);
    }

    private void setupSmartDashboardFields() {
        SmartDashboard.putNumber("Shooter speed", RobotMap.SHOOTER_IDEAL_SPEED);
    }

    private void setupAutonChooser(){
    	autonChooser = new SendableChooser<Command>();
    	autonChooser.addDefault("Do Nothing", new CommandGroup());
    	autonChooser.addObject("Minimal Mobility", new MobilityMinimalCommand());
    	autonChooser.addObject("Minimal Mobility From Middle Gear Start", new MiddleGearMobilityMinimalCommand());
    	autonChooser.addObject("Only Mobility To HP Station", new MobilityToHPCommand());
    	autonChooser.addObject("Only Score HP Gear", new ScoreHPGearCommand());
    	autonChooser.addObject("Score HP Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreHPGearCommand(), new ApproachHPFromHPGearCommand()));
    	autonChooser.addObject("Only Score Middle Gear", new ScoreMiddleGearCommand());
    	autonChooser.addObject("Score Middle Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreMiddleGearCommand(), new ApproachHPFromMiddleGearCommand()));
    	autonChooser.addObject("Score Middle Gear THEN Shoot", new DoubleSequentialCommand(new ScoreMiddleGearCommand(), new ShootFromMiddleGearCommand()));
    	autonChooser.addObject("Only Score Boiler Gear", new ScoreBoilerGearCommand());
    	autonChooser.addObject("Score Boiler Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreBoilerGearCommand(), new ApproachHPFromBoilerGearCommand()));
    	autonChooser.addObject("Score Boiler Gear THEN Shoot", new DoubleSequentialCommand(new ScoreBoilerGearCommand(), new ShootingFromBoilerGearCommand()));
    	autonChooser.addObject("Only Shoot", new ShootingFromAllianceWallCommand());
    }
    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
        // TODO: why the scheduler called here (in the default code)?
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
        // schedule the autonomous command
        autonomousCommand = autonChooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }

        // TODO: Set SHOOTER_IDEAL_SPEED to the ideal speed when it is known,
        // then set shooter speed to SHOOTER_IDEAL_SPEED here.
        Robot.shooter.setSpeed(SmartDashboard.getNumber("Shooter speed", 0.0));

        // The gear-pusher piston, and the gear trap pistons, start *retracted*,
        // because when extended they reach outside the frame perimeter.
        // Thus we must immediately close the gear trap, then push the
        // gear pusher.
        // We do this in autonomousInit rather than a Command because it must
        // always happen, regardless of what comes next, and it is quick. This
        // is also why blocking the thread is appropriate:
        Robot.geartrap.trap();
        Timer.delay(RobotMap.AUTON_INIT_DELAY_BEFORE_PUSH_GEAR);
        Robot.gearpusher.push(Value.kForward);

        // Gear-shift physically starts in HIGH gear.
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        blender.checkForJam();
        irsensor.gearLEDSignalControl();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }

        Camera.configureCamera(0);
        Camera.configureCamera(1);
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        blender.checkForJam();
        irsensor.handleAutoGearPush();
        irsensor.gearLEDSignalControl();
        SmartDashboard.putNumber("IRDistance", irsensor.getDistance());
        SmartDashboard.putNumber("IRVoltage", irsensor.getVoltage());
        SmartDashboard.putNumber("Encoder drivetrain left", Robot.drivetrain.leftEncoderDistance());
        SmartDashboard.putNumber("Encoder drivetrain right", Robot.drivetrain.rightEncoderDistance());
        SmartDashboard.putNumber("Gyro angle", Robot.drivetrain.gyroAngle());
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
}
