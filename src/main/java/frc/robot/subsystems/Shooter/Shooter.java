package frc.robot.subsystems.Shooter;

import frc.robot.GlobalVariables;
import frc.robot.Robot;
import frc.robot.Constants.ShooterConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase{
    
    private final ShooterIO io;

    private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();

    private static final double VELOCITY_TOLERANCE = 3.0;//çalışmadı amk
    private static final double STEADY_TIME_THRESHOLD = 0.5;
    private double steadyTime = 0.0;

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

            io.setVelocity(0, 0);
            break;

            case ACCELERATING:
    io.setVelocity(ShooterConstants.kSpeakerSpeedLeft, ShooterConstants.kSpeakerSpeedRight);

    boolean leftWithinTolerance = Math.abs(ShooterConstants.kSpeakerSpeedLeft - inputs.leftVelocityRps) <= VELOCITY_TOLERANCE;
    boolean rightWithinTolerance = Math.abs(ShooterConstants.kSpeakerSpeedRight - inputs.rightVelocityRps) <= VELOCITY_TOLERANCE;

    if(!GlobalVariables.getInstance().nInside) {
        state = ShooterState.IDLE;
        break;
    }

    if (leftWithinTolerance && rightWithinTolerance) {
        steadyTime += 0.02; //delphide bir ağabey 20ms de bir çalışır yeğen demiş ona güvendim
        if (steadyTime >= STEADY_TIME_THRESHOLD) {
            SmartDashboard.putBoolean("Hazir", true);
            state = ShooterState.READY;
        }
    } else {
        steadyTime = 0.0;//fena olmadı he
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
    public Command setStateCommand(ShooterState state) {
    return runOnce(() -> this.state = state);
}
public void stopIfNotBusy() {
    if (this.state == ShooterState.ACCELERATING) {
        this.state = ShooterState.ACCELERATING;
    } else {
        this.state = ShooterState.IDLE;
    }
    return;
}

}
