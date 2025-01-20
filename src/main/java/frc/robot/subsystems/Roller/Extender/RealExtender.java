package frc.robot.subsystems.Roller.Extender;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class RealExtender implements ExtenderIO{
  private static final int ExtenderMotorId = 0;
  private static final boolean ExtenderMotorReversed = false;
    private final SparkMax extenderMotor;{
        extenderMotor = new SparkMax(ExtenderMotorId, MotorType.kBrushless);
      SparkMaxConfig extenderConfig = new SparkMaxConfig();
      extenderConfig
              .inverted(ExtenderMotorReversed)
            .idleMode(com.revrobotics.spark.config.SparkBaseConfig.IdleMode.kCoast);}

    public RealExtender() {
    }

    @Override
    public void setOutputPercentage(double percentage) {
        extenderMotor.set(percentage);
    }

    @Override
    public void stop(){
        extenderMotor.set(0);
    }

    @Override
    public void updateInputs(ExtenderIOInputs inputs){
        inputs.velocityRotationsPerMinute = extenderMotor.getEncoder().getVelocity();
        inputs.appliedVolts = extenderMotor.getBusVoltage();
        inputs.supplyCurrentAmps = extenderMotor.getOutputCurrent();
        inputs.tempCelcius = extenderMotor.getMotorTemperature();
    }
}