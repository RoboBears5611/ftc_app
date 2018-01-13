package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeBasedMechanumDriveBase{
    private MechanumDriveBase mechanumDriveBase;
    private boolean actionIsExecuting;
    private Timer timer;


    public TimeBasedMechanumDriveBase(HardwareMap hardwareMap,Telemetry telemetry){
        mechanumDriveBase = new MechanumDriveBase(hardwareMap,telemetry);

    }
    public boolean isBusy(){
        return actionIsExecuting;
    }

    public boolean move(float x,float y,float turn, long timeMillis){
        if(isBusy()){
            return false;
        }
        mechanumDriveBase.move(x,y,turn);
        actionIsExecuting = true;
        timer.schedule(new EndAction(),timeMillis);
        return true;
    }
    private class EndAction extends TimerTask{

        @Override
        public void run() {
            mechanumDriveBase.stopAllMotors();
            actionIsExecuting = false;
        }
    }
}
