package frc.robot;

import java.util.List;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.IntakextenderConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.SwerveJoystickCmd;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Roller.Intake.RealIntake;
import frc.robot.subsystems.Roller.Intake.Intake;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import static edu.wpi.first.wpilibj2.command.Commands.runOnce;
import frc.robot.commands.ShooterCommand;




public class RobotContainer {



    private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
    private final CommandXboxController driverJoystick = new CommandXboxController(0);
    
    private final Joystick driverJoytick = new Joystick(OIConstants.kDriverControllerPort);

    public RobotContainer() {
        swerveSubsystem.setDefaultCommand(new SwerveJoystickCmd(
                swerveSubsystem,
                () -> -driverJoytick.getRawAxis(OIConstants.kDriverYAxis),
                () -> -driverJoytick.getRawAxis(OIConstants.kDriverXAxis),
                () -> -driverJoytick.getRawAxis(OIConstants.kDriverRotAxis),
                () -> false));

                

        configureButtonBindings();
    }
    

    private void configureButtonBindings() {
        new JoystickButton(driverJoytick, 2).onTrue(new InstantCommand(swerveSubsystem::zeroHeading));
        driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).whileTrue(new RepeatCommand(Rollers.Rollerstate.setStateCommand(Rollerstate.INTAKING)));
    driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).onFalse(runOnce(() -> Rollers.Rollerstate.stopIfNotBusy()));
    driverJoystick.a().whileTrue(Rollers.Rollerstate.setStateCommand(Rollers.Rollerstate.EJECTING));
    driverJoystick.a().onFalse(runOnce(() -> Rollers.stopIfNotBusy()));

    }

public Command getAutonomousCommand() {
    TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
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
            new InstantCommand(() -> swerveSubsystem.stopModules()));
}

}