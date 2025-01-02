package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase{
    public SparkMax intakeMotor;
    public DigitalInput breakBeam;


    public IntakeSubsystem() {

        intakeMotor = new SparkMax(0, MotorType.kBrushless);
        breakBeam = new DigitalInput(0);
    }
    

public void periodic(){
    SmartDashboard.putBoolean("Intake/BreakBeam", breakBeam.get());
}

public void runForward(){
    intakeMotor.setVoltage(8);
}

public void runBackward(){
    intakeMotor.setVoltage(-3);
}

public void stop(){
    intakeMotor.stopMotor();
}

public boolean hasDisc(){
    return !breakBeam.get();}

public Command load(){
    return Commands.startEnd(this::runForward, this::runBackward, this).until(this::hasDisc);
}

public Command purge(){
    return Commands.startEnd(this::runBackward, this::stop, this);
}

}
