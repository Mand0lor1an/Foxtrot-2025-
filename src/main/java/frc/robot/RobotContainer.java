package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.robot.Constants.IntakextenderConstants;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Roller.Intake.Intake;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.Shooter.ShooterState;
import frc.robot.subsystems.Roller.Extender.Extender;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import static edu.wpi.first.wpilibj2.command.Commands.runOnce;




public class RobotContainer {
    private final CommandXboxController driverJoystick = new CommandXboxController(0);
    
    private Intake intake;
    private BeamBreak beamBreak;
    private Extender extender;
    private Rollers rollers;
    private Shooter shooter;

    public RobotContainer() {

                    
                                    extender = frc.robot.subsystems.Roller.Extender.Extender.create();
                                intake = frc.robot.subsystems.Roller.Intake.Intake.create();
                            beamBreak = new BeamBreak();
                        rollers = new Rollers(extender, intake, beamBreak);
                        shooter = Shooter.create();

        configureButtonBindings();
    }
    

    private void configureButtonBindings() {
    driverJoystick.rightBumper().whileTrue(new RepeatCommand(shooter.setStateCommand(ShooterState.ACCELERATING)));
    driverJoystick.rightBumper().onFalse(runOnce(() -> shooter.stopIfNotBusy()));
    driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).whileTrue(new RepeatCommand(rollers.setStateCommand(Rollerstate.INTAKING)));
    driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).onFalse(runOnce(() -> rollers.stopIfNotBusy()));
    driverJoystick.a().whileTrue(rollers.setStateCommand(Rollerstate.EJECTING));
    driverJoystick.a().onFalse(runOnce(() -> rollers.stopIfNotBusy()));
    driverJoystick.y().whileTrue(new RepeatCommand(rollers.setStateCommand(Rollerstate.FEEDING)));
    driverJoystick.y().onFalse(runOnce(() -> rollers.stopIfNotBusy()));

    }

public Command getAutonomousCommand() {
        return null;

}

}