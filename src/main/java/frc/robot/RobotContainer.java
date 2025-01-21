package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.robot.Constants.IntakextenderConstants;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Roller.Intake.Intake;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;
import frc.robot.subsystems.Roller.Extender.Extender;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import static edu.wpi.first.wpilibj2.command.Commands.runOnce;




public class RobotContainer {





    private final CommandXboxController driverJoystick = new CommandXboxController(0);
    
    private Intake Intake;
    private BeamBreak BeamBreak;
    private Extender Extender;
    private Rollers Rollers;

                    
                       /* */ public RobotContainer() {

                    
                                    Extender = frc.robot.subsystems.Roller.Extender.Extender.create();
                                Intake = frc.robot.subsystems.Roller.Intake.Intake.create();
                            BeamBreak = new BeamBreak();
                        Rollers = new Rollers(Extender, Intake, BeamBreak);

        configureButtonBindings();
    }
    

    private void configureButtonBindings() {
        driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).whileTrue(new RepeatCommand(Rollers.setStateCommand(Rollerstate.INTAKING)));
    driverJoystick.leftTrigger(IntakextenderConstants.kIntakeDeadband).onFalse(runOnce(() -> Rollers.stopIfNotBusy()));
    driverJoystick.a().whileTrue(Rollers.setStateCommand(Rollerstate.EJECTING));
    driverJoystick.a().onFalse(runOnce(() -> Rollers.stopIfNotBusy()));
    driverJoystick.y().whileTrue(new RepeatCommand(Rollers.setStateCommand(Rollerstate.FEEDING)));
    driverJoystick.y().onFalse(runOnce(() -> Rollers.stopIfNotBusy()));

    }

public Command getAutonomousCommand() {
        return null;

}

}