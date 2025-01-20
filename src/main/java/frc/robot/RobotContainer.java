package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.IntakextenderConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.SwerveJoystickCmd;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Roller.Intake.Intake;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;
import frc.robot.subsystems.Roller.Extender.Extender;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import static edu.wpi.first.wpilibj2.command.Commands.runOnce;




public class RobotContainer {




    private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
    private final CommandXboxController driverJoystick = new CommandXboxController(0);
    
    private final Joystick driverJoytick = new Joystick(OIConstants.kDriverControllerPort);
    private Intake Intake;
    private BeamBreak BeamBreak;
    private Extender Extender;
    private Rollers Rollers;
                
                    public RobotContainer() {
                        swerveSubsystem.setDefaultCommand(new SwerveJoystickCmd(
                                swerveSubsystem,
                                () -> -driverJoytick.getRawAxis(OIConstants.kDriverYAxis),
                                () -> -driverJoytick.getRawAxis(OIConstants.kDriverXAxis),
                                () -> -driverJoytick.getRawAxis(OIConstants.kDriverRotAxis),
                                () -> false));
                
                                Extender = frc.robot.subsystems.Roller.Extender.Extender.create();
                            Intake = frc.robot.subsystems.Roller.Intake.Intake.create();
                        BeamBreak = new BeamBreak();
                    Rollers = new Rollers(Extender, Intake, BeamBreak);

        configureButtonBindings();
    }
    

    private void configureButtonBindings() {
        new JoystickButton(driverJoytick, 2).onTrue(new InstantCommand(swerveSubsystem::zeroHeading));
        driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).whileTrue(new RepeatCommand(Rollers.setStateCommand(Rollerstate.INTAKING)));
    driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).onFalse(runOnce(() -> Rollers.stopIfNotBusy()));
    driverJoystick.a().whileTrue(Rollers.setStateCommand(Rollerstate.EJECTING));
    driverJoystick.a().onFalse(runOnce(() -> Rollers.stopIfNotBusy()));

    }

public Command getAutonomousCommand() {
        return null;
    /*TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(DriveConstants.kDriveKinematics);

    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(
                    new Translation2d(1, 0),
                    new Translation2d(1, -1)),
            new Pose2d(2, -1, Rotation2d.fromDegrees(180)),
            trajectoryConfig);

    PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
    PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
    ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    HolonomicDriveController holonomicController = new HolonomicDriveController(
            xController, yController, thetaController);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
            trajectory,
            swerveSubsystem::getPose,
            DriveConstants.kDriveKinematics,
            holonomicController,
            swerveSubsystem::setModuleStates,
            swerveSubsystem);

    return new SequentialCommandGroup(
            new InstantCommand(() -> swerveSubsystem.resetOdometry(trajectory.getInitialPose())),
            swerveControllerCommand,
            new InstantCommand(() -> swerveSubsystem.stopModules()));*/
}

}