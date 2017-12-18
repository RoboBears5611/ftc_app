package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by FTC on 11/21/2016.
 */

@TeleOp(name = "Robot Controller (Full) (OnBot)",group = "drive")
public class RobotControllerOp extends OpMode {
    MechanumDriveBase mechanumDriveBase;
    DcMotor Grabber;
    DcMotor Lift;
    VoltageSensor PowerGage;
    final static String PowerGageName = "PowerGage";
    final static float GrabberSpeed  = 0.25f;
    final static int GrabberFullOpenPos = 295;
    boolean VariableGrabberControl = false;
    boolean LastGamepad1b;

    DcMotor ResettingMotor;

    @Override
    public void init(){
        mechanumDriveBase = new MechanumDriveBase(hardwareMap,telemetry);
        mechanumDriveBase.init();
        Grabber = hardwareMap.dcMotor.get("Grabber");
        Grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(Grabber.isBusy());
        Grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Lift = hardwareMap.dcMotor.get("Lift");
        Lift.setDirection(DcMotorSimple.Direction.REVERSE);
        if(hardwareMap.dcMotorController.contains(PowerGageName)){
            PowerGage = hardwareMap.voltageSensor.get(PowerGageName);
        }
    }


    @Override
    public void start(){
        mechanumDriveBase.stopAllMotors();
    }

    @Override
    public void loop(){
        mechanumDriveBase.move(-gamepad1.left_stick_x,gamepad1.right_stick_y,-gamepad1.right_stick_x);
        grabberOpenAndClose();
        VariableGrabberControl();
        EmergencyResets();
        Lift.setPower((gamepad1.right_bumper?1:0)-(gamepad1.left_bumper?0.25:0));


        telemetry.addData("Lift Power",Lift.getPower());
        telemetry.addData("Pos",Grabber.getCurrentPosition());
        telemetry.addData("Pos_target",Grabber.getTargetPosition());
        telemetry.addData("Power",Grabber.getPower());
        //telemetry.addData("Power_setting",power);
        telemetry.addData("isBusy",Grabber.isBusy());
        telemetry.addData("Current Mode",Grabber.getMode());
    }

    boolean open = true;
    boolean pushedLessThanOnce = true;

    public void grabberOpenAndClose(){
        if(VariableGrabberControl){
            return;
        }
        if(gamepad1.a) {                                                  //If the A button is pushed
            if(pushedLessThanOnce) {                                                  //to be sure that the function close or open just once if we just push once
                if (open) {                                                                 //close if it's opened
                    Grabber.setTargetPosition(GrabberFullOpenPos);
                    open = false;
                }
                else{                                                                       //open if it's closed
                    Grabber.setTargetPosition(0);
                    open = true;
                }
                Grabber.setPower(GrabberSpeed);
                pushedLessThanOnce = false;
            }
        }
        else{
            pushedLessThanOnce = true;
        }

        if(open == false){
            if(Grabber.getCurrentPosition() > GrabberFullOpenPos - 25){
                Grabber.setPower(0.03);
            }
            else if(Grabber.getCurrentPosition() > GrabberFullOpenPos-3){
                Grabber.setPower(0.01);
            }
            else{
                Grabber.setPower(0.2);
            }
        }
    }

    public void VariableGrabberControl(){
        if(gamepad1.b&&!LastGamepad1b){
            VariableGrabberControl = !VariableGrabberControl;
        }
        if(VariableGrabberControl){
            Grabber.setTargetPosition((int)((gamepad1.right_stick_y+1)*GrabberFullOpenPos/2));
        }
        LastGamepad1b = gamepad1.b
    }

    public void EmergencyResets(){
        if(gamepad2.dpad_up){
            if(gamepad2.a){
                ResettingMotor = Grabber;
            }
        }
        if(ResettingMotor!=null){
            ResettingMotor.setPower((gamepad2.left_trigger/2)-(gamepad2.right_trigger/2));
        }
        if(gamepad2.dpad_down&&ResettingMotor!=null){
            DcMotor.RunMode originalMode = ResettingMotor.getMode();
            ResettingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            while(ResettingMotor.isBusy());
            ResettingMotor.setMode(originalMode);
            ResettingMotor = null;
        }
        telemetry.addData("ResettingMotor",ResettingMotor==null?"None":ResettingMotor);
        if(ResettingMotor!=null){
            telemetry.addData("ResettingMotor Power",ResettingMotor.getPower());
        }
    }
}