package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;

public class SwerveModule {

    private final SparkMax driveMotor;
    private final SparkMax turningMotor;

    private final PIDController turningPidController;

    private final CANcoder absoluteEncoder;

    public SwerveModule(int driveMotorId, int turningMotorId, boolean driveMotorReversed, boolean turningMotorReversed,
                        int absoluteEncoderId, double absoluteEncoderOffset, boolean absoluteEncoderReversed) {

        absoluteEncoder = new CANcoder(absoluteEncoderId);
        CANcoderConfiguration config = new CANcoderConfiguration();
        config.MagnetSensor.MagnetOffset = absoluteEncoderOffset;
        config.MagnetSensor.SensorDirection = absoluteEncoderReversed
                ? SensorDirectionValue.Clockwise_Positive
                : SensorDirectionValue.CounterClockwise_Positive;
        absoluteEncoder.getConfigurator().apply(config);

        driveMotor = new SparkMax(driveMotorId, MotorType.kBrushless);
        SparkMaxConfig driveConfig = new SparkMaxConfig();
        driveConfig
                .inverted(driveMotorReversed)
                .idleMode(IdleMode.kBrake);
        driveConfig.encoder
                .positionConversionFactor(ModuleConstants.kDriveEncoderRot2Meter)
                .velocityConversionFactor(ModuleConstants.kDriveEncoderRPM2MeterPerSec);
        driveConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ModuleConstants.kPTurning, 0.0, 0.0);
        driveMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        turningMotor = new SparkMax(turningMotorId, MotorType.kBrushless);
        SparkMaxConfig turningConfig = new SparkMaxConfig();
        turningConfig
                .inverted(turningMotorReversed)
                .idleMode(IdleMode.kCoast);
        turningConfig.encoder
                .positionConversionFactor(ModuleConstants.kTurningEncoderRot2Rad)
                .velocityConversionFactor(ModuleConstants.kTurningEncoderRPM2RadPerSec);
        turningMotor.configure(turningConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Initialize the turning PID controller
        turningPidController = new PIDController(ModuleConstants.kPTurning, 0, 0);
        turningPidController.enableContinuousInput(-Math.PI, Math.PI);

        resetEncoders();
    }

    public double getDrivePosition() {
        return driveMotor.getEncoder().getPosition();
    }

    public double getTurningPosition() {
        return turningMotor.getEncoder().getPosition();
    }

    public double getDriveVelocity() {
        return driveMotor.getEncoder().getVelocity();
    }

    public double getTurningVelocity() {
        return turningMotor.getEncoder().getVelocity();
    }

    public double getAbsoluteEncoderRad() {
        return Math.toRadians(absoluteEncoder.getPosition().getValueAsDouble() * 360);
    }

    public void resetEncoders() {
        driveMotor.getEncoder().setPosition(0);
        turningMotor.getEncoder().setPosition(getAbsoluteEncoderRad());
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(getDrivePosition(), new Rotation2d(getTurningPosition()));
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getTurningPosition()));
    }

    public void setDesiredState(SwerveModuleState state) {
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }


    double currentAngle = getTurningPosition();

    state.optimize(getState().angle);
    state.cosineScale(getState().angle);
    driveMotor.set(state.speedMetersPerSecond / DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
    turningMotor.set(turningPidController.calculate(currentAngle, state.angle.getRadians()));

    }

    public void stop() {
        driveMotor.set(0);


        
        turningMotor.set(0);
    }
}
