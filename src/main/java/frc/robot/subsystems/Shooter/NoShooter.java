package frc.robot.subsystems.Shooter;

import edu.wpi.first.units.Measure;
import edu.wpi.first.units.VoltageUnit;

public class NoShooter implements ShooterIO{

    @Override
    public void setVelocity(double leftRPS, double rightRPS) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVelocity'");
    }

    @Override
    public void neutralMotors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'neutralMotors'");
    }

    @Override
    public void setSysIdVoltage(Measure<VoltageUnit> volts) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSysIdVoltage'");
    }
    
}
