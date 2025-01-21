package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.Shooter.Shooter.ShooterState;
import frc.robot.subsystems.Roller.Rollers;
import frc.robot.subsystems.Roller.Rollers.Rollerstate;

public class ShooterCommand extends Command {
    private final Shooter shooter;
    private final Rollers rollers;
    private final XboxController operator;

    private boolean isFinished = false;
    private double stateStartTime;

    private static final double PREPARE_DURATION = 0.1;
    private static final double SHOOT_DURATION = 1.5;

    private State currentState = State.IDLE;

    // State enumeration
    private enum State {
        IDLE,
        ACCELERATING,
        PREPARING,
        SHOOTING,
        DONE
    }

    public ShooterCommand(Shooter shooter, Rollers rollers, XboxController operator) {
        this.shooter = shooter;
        this.rollers = rollers;
        this.operator = operator;

        addRequirements(shooter, rollers);
    }

    @Override
    public void initialize() {
        transitionToState(State.ACCELERATING);
    }

    @Override
    public void execute() {
        switch (currentState) {
            case ACCELERATING:
                handleAccelerating();
                break;

            case PREPARING:
                handlePreparing();
                break;

            case SHOOTING:
                handleShooting();
                break;

            case DONE:
                isFinished = true;
                break;

            default:
                break;
        }

        if (operator.getBButton()) {
            // Cancel command if B button is pressed
            transitionToState(State.DONE);
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.state = ShooterState.IDLE;
        rollers.state = Rollerstate.IDLE;
        operator.setRumble(RumbleType.kBothRumble, 0);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    // Handle the accelerating state
    private void handleAccelerating() {
        shooter.state = ShooterState.ACCELERATING;

        if (shooter.state == ShooterState.READY) {
            if (operator.getRightTriggerAxis() > 0.3) {
                operator.setRumble(RumbleType.kBothRumble, 0.5);
                transitionToState(State.PREPARING);
            } else {
                operator.setRumble(RumbleType.kBothRumble, 0);
            }
        } else if (operator.getRightTriggerAxis() > 0.3) {
            operator.setRumble(RumbleType.kRightRumble, 1);
        } else {
            operator.setRumble(RumbleType.kBothRumble, 0);
        }
    }

    // Handle the preparing state
    private void handlePreparing() {
        rollers.state = Rollerstate.INTAKING;

        if (timeSinceStateStart() >= PREPARE_DURATION) {
            transitionToState(State.SHOOTING);
        }
    }

    // Handle the shooting state
    private void handleShooting() {
        rollers.state = Rollerstate.FEEDING;

        if (timeSinceStateStart() >= SHOOT_DURATION) {
            transitionToState(State.DONE);
        }
    }

    // Transition to a new state
    private void transitionToState(State newState) {
        currentState = newState;
        stateStartTime = Timer.getFPGATimestamp();
    }

    // Get time since the current state started
    private double timeSinceStateStart() {
        return Timer.getFPGATimestamp() - stateStartTime;
    }
}
