package frc.robot.subsystems.Shooter;

import frc.robot.GlobalVariables;
import frc.robot.Robot;
import frc.robot.Constants.ShooterConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase{
    
    private final ShooterIO io;

    private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();

    public static Shooter create() {
        return new Shooter(Robot.isReal() ? new RealShooter() : new NoShooter());    
    }
    
    public Shooter(ShooterIO io) {
        this.io = io;
    }
    
    public enum ShooterState{
        IDLE,
        ACCELERATING,
        READY
    }

    public ShooterState state = ShooterState.IDLE;

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);

        switch(state) {
            case IDLE:
            if(GlobalVariables.getInstance().nInside) {
                state = ShooterState.ACCELERATING;
                break;
            }
            io.setVelocity(0, 0);
            break;

            case ACCELERATING:
                io.setVelocity(ShooterConstants.kSpeakerSpeedLeft, ShooterConstants.kSpeakerSpeedRight);
                if (ShooterConstants.kSpeakerSpeedLeft - inputs.leftVelocityRps < 3 && ShooterConstants.kSpeakerSpeedRight + inputs.rightVelocityRps < 3) {
                    SmartDashboard.putBoolean("shooterReady", true);
                    state = ShooterState.READY;
                }
                break;

            case READY:
                if (GlobalVariables.getInstance().nInside){
                    state = ShooterState.IDLE;
                    break;
                }
                break;
        }


    }


}
