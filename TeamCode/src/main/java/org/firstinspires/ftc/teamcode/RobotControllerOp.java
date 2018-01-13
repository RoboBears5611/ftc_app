package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by FTC on 11/21/2016.
 */

@TeleOp(name = "Robot Controller (Full)",group = "drive")
public class RobotControllerOp extends OpMode {
    MechanumDriveBase mechanumDriveBase;
    Servo GrabberLeft;
    Servo GrabberRight;
    boolean GrabberState = false;
    double GrabberOpenPosition = 0.2;
    double GrabberClosedPosition = 0.525;
    final static double GrabberPositionAdjustmentAmount = 0.02;
    DcMotor Lift;
    VoltageSensor PowerGage;
    final static String PowerGageName = "Aux";
    boolean VariableGrabberControl = false;
    boolean LastGamepad1b = false;
    boolean LastGamepad1a = false;

//    DcMotor ResettingMotor;


    @Override
    public void init(){
        mechanumDriveBase = new MechanumDriveBase(hardwareMap,telemetry);
        mechanumDriveBase.init();
        GrabberLeft = hardwareMap.servo.get("GrabberLeft");
        GrabberLeft.setDirection(Servo.Direction.REVERSE);
        GrabberRight = hardwareMap.servo.get("GrabberRight");
        Lift = hardwareMap.dcMotor.get("Lift");
        Lift.setDirection(DcMotorSimple.Direction.REVERSE);
        Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(hardwareMap.dcMotorController.contains(PowerGageName)){
            PowerGage = hardwareMap.voltageSensor.get(PowerGageName);
        }
        UpdateGrabbers();
    }


    @Override
    public void start(){
        mechanumDriveBase.stopAllMotors();
    }

    @Override
    public void loop(){
        mechanumDriveBase.move(-gamepad1.left_stick_x,-gamepad1.right_stick_y,gamepad1.right_stick_x,gamepad1.right_bumper,PowerGage==null?0:PowerGage.getVoltage());
        grabberOpenAndClose();
        VariableGrabberControl();
        //EmergencyResets();
        Lift.setPower(0.5);
        Lift.setTargetPosition(Lift.getTargetPosition()+(int)((gamepad1.right_trigger-gamepad1.left_trigger)*40));


        telemetry.addData("Lift Target Pos",Lift.getTargetPosition());
        telemetry.addData("RPos",GrabberRight.getPosition());
        telemetry.addData("LPos",GrabberLeft.getPosition());
    }

    public void grabberOpenAndClose(){
        if(VariableGrabberControl){
            return;
        }
        ChangeOpenClosePos(
                (gamepad1.dpad_right?GrabberPositionAdjustmentAmount:0)
                        - (gamepad1.dpad_left?GrabberPositionAdjustmentAmount:0));
        if(gamepad1.a&&!LastGamepad1a) {                                                  //If the A button is pushed
            GrabberState = !GrabberState;
            UpdateGrabbers();
        }
        LastGamepad1a = gamepad1.a;
    }

    private void ChangeOpenClosePos(double amount){
        if(GrabberState){
            GrabberClosedPosition += amount;
        }else{
            GrabberOpenPosition += amount;
        }
    }

    public void UpdateGrabbers(){
        GrabberLeft.setPosition(GrabberState?GrabberClosedPosition:GrabberOpenPosition);
        GrabberRight.setPosition(GrabberState?GrabberClosedPosition:GrabberOpenPosition);
    }
    public void VariableGrabberControl(){
        if(gamepad1.b&&!LastGamepad1b){
            VariableGrabberControl = !VariableGrabberControl;
        }
        if(VariableGrabberControl){
            GrabberLeft.setPosition((gamepad1.dpad_up?0.5:0)-(gamepad1.dpad_down?0.5:0)+0.5);
            GrabberRight.setPosition((gamepad1.dpad_up?0.5:0)-(gamepad1.dpad_down?0.5:0)+0.5);
        }
        LastGamepad1b = gamepad1.b;
    }
//Unnecessary until we get a Encoder-Enabled Motor that could need resetting
//    public void EmergencyResets(){
//        if(gamepad2.dpad_up){
//
//        }
//        if(ResettingMotor!=null){
//            ResettingMotor.setPower((gamepad2.left_trigger/2)-(gamepad2.right_trigger/2));
//        }
//        if(gamepad2.dpad_down&&ResettingMotor!=null){
//            DcMotor.RunMode originalMode = ResettingMotor.getMode();
//            ResettingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            while(ResettingMotor.isBusy());
//            ResettingMotor.setMode(originalMode);
//            ResettingMotor = null;
//        }
//        telemetry.addData("ResettingMotor",ResettingMotor==null?"None":ResettingMotor);
//        if(ResettingMotor!=null){
//            telemetry.addData("ResettingMotor Power",ResettingMotor.getPower());
//        }
//    }
}