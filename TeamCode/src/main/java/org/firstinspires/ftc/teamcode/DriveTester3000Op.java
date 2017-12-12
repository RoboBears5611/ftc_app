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

public class MechanumDriveBaseBkup {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private final static String LFMotorDefaultName = "LFMotor";
    private final static String RFMotorDefaultName = "RFMotor";
    private final static String LBMotorDefaultName = "LBMotor";
    private final static String RBMotorDefaultName = "RBMotor";

    private final static boolean LFMotorDefaultReversed = false;
    private final static boolean RFMotorDefaultReversed = true;
    private final static boolean LBMotorDefaultReversed = false;
    private final static boolean RBMotorDefaultReversed = true;

    private DcMotor LFMotor;
    private DcMotor RFMotor;
    private DcMotor LBMotor;
    private DcMotor RBMotor;
    public MechanumDriveBaseBkup(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }


    public void init() {
        init(   LFMotorDefaultName, LFMotorDefaultReversed,
                RFMotorDefaultName, RFMotorDefaultReversed,
                LBMotorDefaultName, LBMotorDefaultReversed,
                RBMotorDefaultName, RBMotorDefaultReversed);
    }
    public void init(String LFMotorName, boolean LFMotorReversed,
                     String RFMotorName, boolean RFMotorReversed,
                     String LBMotorName, boolean LBMotorReversed,
                     String RBMotorName, boolean RBMotorReversed) {
        RFMotor = hardwareMap.dcMotor.get(RFMotorName);
        RFMotor.setDirection(RFMotorReversed? DcMotorSimple.Direction.REVERSE: DcMotorSimple.Direction.FORWARD);
        LFMotor = hardwareMap.dcMotor.get(LFMotorName);
        LFMotor.setDirection(LFMotorReversed? DcMotorSimple.Direction.REVERSE: DcMotorSimple.Direction.FORWARD);
        RBMotor = hardwareMap.dcMotor.get(RBMotorName);
        RBMotor.setDirection(RBMotorReversed? DcMotorSimple.Direction.REVERSE: DcMotorSimple.Direction.FORWARD);
        LBMotor = hardwareMap.dcMotor.get(LBMotorName);
        LBMotor.setDirection(LBMotorReversed? DcMotorSimple.Direction.REVERSE: DcMotorSimple.Direction.FORWARD);
    }


    public void stopAllMotors(){
        LFMotor.setPower(0);
        RFMotor.setPower(0);
        LBMotor.setPower(0);
        RBMotor.setPower(0);
    }

    public void move(float x, float y, float turn){
        telemetry.addData("Drive X",x);
        telemetry.addData("Drive Y",y);
        telemetry.addData("Drive Turn",turn);

        //good fun.
        double r = Math.hypot(x, y); //Get how far the joystick is pushed
        double robotAngle = Math.atan2(y, x) - Math.PI / 4; //Rotate everything 45 degrees, since the wheels operate on tires with an exactly 45 degree offset from forwards and backwards on the robot
        final double LF = r * Math.cos(robotAngle) + turn; //Calculate just how much speed each wheel should get in relation to each other (as stated by some Trig!) and then multiply that by how much power we actually want (Sin and Cos won't give us that, since they are just working with angles)
        final double RF = r * Math.sin(robotAngle) - turn; //We then subtract or add to the power, according to the wheels orientation to the rest of the bot.  This change causes the robot to turn.
        final double LB = r * Math.sin(robotAngle) + turn;
        final double RB = r * Math.cos(robotAngle) - turn;

        LFMotor.setPower(LF);
        telemetry.addData("LFMotor",LF);
        RFMotor.setPower(RF);
        telemetry.addData("RFMotor",RF);
        LBMotor.setPower(LB);
        telemetry.addData("LBMotor",LB);
        RBMotor.setPower(RB);
        telemetry.addData("RBMotor",RB);
    }

}
