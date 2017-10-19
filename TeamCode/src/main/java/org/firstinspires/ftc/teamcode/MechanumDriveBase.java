package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by FTC on 12/13/2016.
 */

public class MechanumDriveBase {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private final static String LFMotorDefaultName = "LFMotor";
    private final static String RFMotorDefaultName = "RFMotor";
    private final static String LBMotorDefaultName = "LMMotor";
    private final static String RBMotorDefaultName = "RMMotor";

    private DcMotor LFMotor;
    private DcMotor RFMotor;
    private DcMotor LBMotor;
    private DcMotor RBMotor;

    public MechanumDriveBase(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }


    public void init() {
        init(LFMotorDefaultName,RFMotorDefaultName,LBMotorDefaultName,RBMotorDefaultName);
    }
    public void init(String LFMotorName,String RFMotorName,String LBMotorName,String RBMotorName) {
        RFMotor = hardwareMap.dcMotor.get(RFMotorName);
        LFMotor = hardwareMap.dcMotor.get(LFMotorName);
        RBMotor = hardwareMap.dcMotor.get(RBMotorName);
        LBMotor = hardwareMap.dcMotor.get(LBMotorName);
    }

    public void stopAllMotors(){
        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);
    }

    public void move(float x, float y, float turn){
        //good fun.
        double r = Math.hypot(x, y); //Get how far the joystick is pushed
        double robotAngle = Math.atan2(y, x) - Math.PI / 4; //Rotate everything 45 degrees, since the wheels operate on tires with an exactly 45 degree offset from forwards and backwards on the robot
        final double v1 = r * Math.cos(robotAngle) + turn; //Calculate just how much speed each wheel should get in relation to each other (as stated by some Trig!) and then multiply that by how much power we actually want (Sin and Cos won't give us that, since they are just working with angles)
        final double v2 = r * Math.sin(robotAngle) - turn; //We then subtract or add to the power, according to the wheels orientation to the rest of the bot.  This change causes the robot to turn.
        final double v3 = r * Math.sin(robotAngle) + turn;
        final double v4 = r * Math.cos(robotAngle) - turn;

        LFMotor.setPower(v1);
        RFMotor.setPower(v2);
        LBMotor.setPower(v3);
        RBMotor.setPower(v4);
    }

}
