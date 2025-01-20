package frc.robot.subsystems.Roller;


import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Roller.Extender.Extender;
import frc.robot.subsystems.Roller.Intake.Intake;
import frc.robot.Constants.IntakextenderConstants;

public class Rollers extends SubsystemBase{
    private final Extender extender;
    private final Intake intake;
    private final BeamBreak beamBreak;
    
    public Rollers(Extender extender, Intake intake, BeamBreak beamBreak) {
        this.extender = extender;
        this.intake = intake;
        this.beamBreak = beamBreak;
    }
        public enum Rollerstate {
            IDLE,
            INTAKING,
            INDEXING,
            EJECTING,
            FEEDING
        }
        public Rollerstate state = Rollerstate.IDLE;
        @Override
        public void periodic() {
            extender.periodic();
            intake.periodic();
            switch (state) {
                case IDLE:
                    intake.setOutputPercentage(0);
                    extender.setOutputPercentage(0);
                    break;
    
                case INTAKING:
                    if (beamBreak.b_upper) {//False
                        extender.setOutputPercentage(0);
                        intake.setOutputPercentage(0);
                        state = Rollerstate.IDLE;
                    break;
                }
                if (beamBreak.b_lower) {
                    state = Rollerstate.INDEXING;
                    break;
                }

                intake.setOutputPercentage(IntakextenderConstants.kIntakeMotorSpeed);
                extender.setOutputPercentage(IntakextenderConstants.kExtenderMotorSpeed);
                break;

                case INDEXING:
                    if (beamBreak.b_upper) {//False
                        extender.setOutputPercentage(0);
                        intake.setOutputPercentage(0);
                        state = Rollerstate.IDLE;
                        break;
                    }

                    intake.setOutputPercentage(IntakextenderConstants.kIntakeMotorSpeed);
                extender.setOutputPercentage(IntakextenderConstants.kExtenderMotorSpeed);
                break;


    
                case EJECTING:
                    intake.setOutputPercentage(-0.5);
                    extender.setOutputPercentage(-0.5);
                    break;

                case FEEDING:
                    extender.setOutputPercentage(1);
                    break;
            }
   Logger.recordOutput("Rollers/State", state.toString());
        SmartDashboard.putString("Roller State", state.toString());     
}

public Command setStateCommand(Rollers.Rollerstate state) {
    return runOnce(() -> this.state = state);
}

public void stopIfNotBusy() {
    if (this.state == Rollerstate.INTAKING) {
        this.state = Rollerstate.INTAKING;
    } else {
        this.state = Rollerstate.IDLE;
    }
    return;
}
}