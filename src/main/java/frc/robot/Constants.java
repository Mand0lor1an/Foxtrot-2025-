package frc.robot;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;


public final class Constants {
    public static final Mode currentMode = Mode.REAL;

    public static enum Mode {
        /** Running on a real robot. */
        REAL,
    
        /** Running a physics simulator. */
        SIM,
    
        /** Replaying from a log file. */
        REPLAY
      }
      public static class ShooterConstants{


    
    public static final double kGearRatio = 1.0 / 11.357;
    public static final double kPivotMotorRot2Rad = kGearRatio * 2 * Math.PI;
    public static final double kMinShooterAngleRad = 0;
    public static final double kMaxShooterAngleRad = Math.toRadians(39);



    public static final double kAngleP = 1;
    public static final double kAngleI = 0;
    public static final double kAngleD = 0;
    public static final double kAngleToleranceRad = Math.toRadians(0.05);

    ////////////////////////////////////////////////////////////////////////////////
    
    
    public static final int kShooterMotorLeftId = 7;
    public static final int kShooterMotorRightId = 8;    

  
    public static final TalonFXConfiguration leftMotorConfig = new TalonFXConfiguration();

    // set slot 0 gains
    private static final Slot0Configs left_slot0Configs;

    static {
      left_slot0Configs = leftMotorConfig.Slot0;
      left_slot0Configs.kS = 0.27698; // Add 0.25 V output to overcome static friction
      left_slot0Configs.kV = 0.12961; // A velocity target of 1 rps results in 0.12 V output
      left_slot0Configs.kA = 0.019564; // An acceleration of 1 rps/s requires 0.01 V output
      left_slot0Configs.kP = 0.20423; // An error of 1 rps results in 0.11 V output
      left_slot0Configs.kI = 0; // no output for integrated error
      left_slot0Configs.kD = 0; // no output for error derivative 
    }

    // set Motion Magic Velocity settings
    private static final MotionMagicConfigs left_motionMagicConfigs;

    static {
      left_motionMagicConfigs = leftMotorConfig.MotionMagic;
      left_motionMagicConfigs.MotionMagicAcceleration = 400; // Target acceleration of 400 rps/s (0.25 seconds to max)
      left_motionMagicConfigs.MotionMagicJerk = 4000; // Target jerk of 4000 rps/s/s (0.1 seconds)
    }

    public static final TalonFXConfiguration rightMotorConfig = new TalonFXConfiguration();

    // set slot 0 gains
    private static final Slot0Configs right_slot0Configs;

    static {
      right_slot0Configs = rightMotorConfig.Slot0;
      right_slot0Configs.kS = 0.32527; // Add 0.25 V output to overcome static friction
      right_slot0Configs.kV = 0.12874; // A velocity target of 1 rps results in 0.12 V output
      right_slot0Configs.kA = 0.013443; // An acceleration of 1 rps/s requires 0.01 V output
      right_slot0Configs.kP = 0.19824; // An error of 1 rps results in 0.11 V output
      right_slot0Configs.kI = 0; // no output for integrated error
      right_slot0Configs.kD = 0; // no output for error derivative 
    }

    // set Motion Magic Velocity settings
    private static final MotionMagicConfigs right_motionMagicConfigs;

    static {
      right_motionMagicConfigs = rightMotorConfig.MotionMagic;
      right_motionMagicConfigs.MotionMagicAcceleration = 400; // Target acceleration of 400 rps/s (0.25 seconds to max)
      right_motionMagicConfigs.MotionMagicJerk = 4000; // Target jerk of 4000 rps/s/s (0.1 seconds)
    }
    public static final double kSpeakerSpeedLeft = 64.3;
    public static final double kSpeakerSpeedRight = 52.3;

    public static final double kAmpSpeedLeft = 21.5;
    public static final double kAmpSpeedRight = 21.5;
    
    public static final double kVoltageCompensation = 10;
      }

      
   

    public static final class OIConstants {
        public static final int kDriverControllerPort = 0;

        public static final int kDriverYAxis = 1;
        public static final int kDriverXAxis = 0;
        public static final int kDriverRotAxis = 4;
        public static final int kDriverFieldOrientedButtonIdx = 1;

        public static final double kDeadband = 0.05;
    }

    public static class IntakextenderConstants{
        public static final int kIntakeMotorId = 10;
        public static final boolean kIntakeMotorReversed = false;
        public static final double kIntakeMotorSpeed = 0.65;
        public static final double kIntakeDeadband = 0.3;
    
    
        public static final int kExtenderMotorId = 62;
        public static final boolean kExtenderMotorReversed = false;
        public static final double kExtenderMotorSpeed = 0.15;
        public static final double kExtenderBackSpeed = 0.3;
      }
}

