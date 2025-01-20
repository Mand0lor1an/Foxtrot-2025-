package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.GlobalVariables;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BeamBreak extends SubsystemBase{
  private final DigitalInput upper_beamBreak, lower_beambreak;
  public boolean b_upper = false;
  public boolean b_lower = false;

  public BeamBreak() {
    upper_beamBreak = new DigitalInput(0);
    lower_beambreak = new DigitalInput(2);
    SmartDashboard.putBoolean("alt", false);
    SmartDashboard.putBoolean("üst", false);
  }

  @Override
  public void periodic() {
      b_upper = !upper_beamBreak.get();
      GlobalVariables.getInstance().nInside = b_upper;
      Logger.recordOutput("Beam Break/Upper Beam", b_upper);
      SmartDashboard.putBoolean("nInside", b_upper);

      b_lower = !lower_beambreak.get();
      Logger.recordOutput("Beam Break/Lower Beam", b_lower);
  }
}