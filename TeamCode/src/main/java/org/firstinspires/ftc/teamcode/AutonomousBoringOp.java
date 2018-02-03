package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Autonomous (Boring) )",group="Autonomous")

public class AutonomousBoringOp extends LinearOpMode {
   TimeBasedMechanumDriveBase timeBasedMechanumDriveBase;
    @Override
    public void runOpMode(){
        timeBasedMechanumDriveBase = new  TimeBasedMechanumDriveBase(hardwareMap,telemetry);
        timeBasedMechanumDriveBase.move(0,1,0,1000);
    
    }
}
