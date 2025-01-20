package frc.robot;

public class GlobalVariables {
    private static GlobalVariables instance = new GlobalVariables();
    
    public boolean nInside;

    private GlobalVariables() {
        nInside = false;
    }

    public static GlobalVariables getInstance() {
        return instance;
    }
}
