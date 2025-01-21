package frc.robot.subsystems.Roller.Intake;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;


public class RealIntake implements IntakeIO{
    
    private static final int intakeMotorId = 10;
        private static final boolean intakeMotorReversed = false;
        
            public RealIntake() {
            }
            private final SparkMax intakeMotor;{
            intakeMotor = new SparkMax(intakeMotorId, MotorType.kBrushless);
        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig
                .inverted(intakeMotorReversed)
            .idleMode(com.revrobotics.spark.config.SparkBaseConfig.IdleMode.kCoast);
                intakeConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);}

    @Override
    public void setOutputPercentage(double percentage) {
        intakeMotor.set(percentage);
    }

    @Override
    public void stop(){
        intakeMotor.set(0);
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs){
  

        inputs.velocityRotationsPerMinute = intakeMotor.getEncoder().getVelocity();
        inputs.appliedVolts = intakeMotor.getBusVoltage();
        inputs.supplyCurrentAmps = intakeMotor.getOutputCurrent();
        inputs.tempCelcius = intakeMotor.getMotorTemperature();
    }


}
