package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.Shooter.ShooterState;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;

public class ShooterCommand extends Command{
    private final Supplier<Double> rightTrigger;
    private final Shooter shooter;
    private final Rollers rollers;
    private boolean ending;
    private final XboxController operator;

    public ShooterCommand(Supplier<Double> rightTrigger ,Shooter shooter, Rollers rollers,XboxController operator){
        this.shooter = shooter;
        this.rollers = rollers;
        this.rightTrigger = rightTrigger;
        this.operator = operator;
        ending = false;
        addRequirements(shooter); 
    }

    private enum State {
        START,
        PREPARE,
        SHOOT,
        END
    }

    private State state = State.START;
    private double startTime;

    @Override
    public void initialize(){
        ending = false;
        state = State.START;
        shooter.state = ShooterState.ACCELERATING;
    }

    @Override
    public void execute() {
        double timeElapsed = Timer.getFPGATimestamp() - startTime;

        switch (state) {
            case START:
                if (shooter.state == ShooterState.READY) {
                    if (rightTrigger.get() > 0.3) {
                        operator.setRumble(RumbleType.kBothRumble, 0.5);
                        startTime = Timer.getFPGATimestamp();//feedi ayarlayabilmek için
                        state = State.PREPARE;
                    } else {
                        operator.setRumble(RumbleType.kBothRumble, 0);
                    }
                } else {
                    if (rightTrigger.get() > 0.3) {
                        operator.setRumble(RumbleType.kRightRumble, 1);//napıyon amk ayısı uyarısı
                    } else { 
                        operator.setRumble(RumbleType.kBothRumble, 0);
                    }
                }
                break;
            case PREPARE:
                if (timeElapsed < 0.1) {
                    rollers.state = Rollerstate.INDEXING;
                } else {
                    state = State.SHOOT;
                }
                break;
            case SHOOT:
                if (timeElapsed < 1.5) {
                    rollers.state = Rollerstate.FEEDING;
                } else {
                    state = State.END;
                }
                break;
            case END:
                ending = true;
                break;
        }
    }
    
    @Override
    public void end(boolean interrupted){
        state = State.START;
        shooter.state = ShooterState.IDLE;
        rollers.state = Rollerstate.IDLE;
    }

    @Override
    public boolean isFinished(){
        return ending;
    }

}