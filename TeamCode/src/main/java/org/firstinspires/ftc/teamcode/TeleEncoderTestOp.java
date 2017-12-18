package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.MechanumDriveBase;

/**
 * Created by admin on 11/26/2017.
 */

@TeleOp(group="Test",name="Encoder Test (Tele) (OnBot)")
public class TeleEncoderTestOp extends OpMode {
    MechanumDriveBase mechanumDriveBase;
    DcMotor Grabber;
    double power;
    @Override
    public void init() {
        mechanumDriveBase = new MechanumDriveBase(hardwareMap,telemetry);
        mechanumDriveBase.init();
        Grabber = hardwareMap.dcMotor.get("Grabber");
        Grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while(Grabber.isBusy());
        Grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    boolean open = true;
    boolean pushedLessThanOnce = true;

    public void grabberOpenAndClose(){

        if(gamepad1.a) {                                                  //If the A button is pushed
            if(pushedLessThanOnce) {                                                  //to be sure that the function close or open just once if we just push once
                if (open) {                                                                 //close if it's opened
                    Grabber.setTargetPosition(320);
                    Grabber.setPower(0.2);
                    open = false;
                }
                else{                                                                       //open if it's closed
                    Grabber.setTargetPosition(0);
                    Grabber.setPower(0.2);
                    open = true;
                }
                pushedLessThanOnce = false;
            }
        }
        else{
            pushedLessThanOnce = true;
        }
    }



    @Override
    public void loop() {

        grabberOpenAndClose();

        /*mechanumDriveBase.move(gamepad1.right_stick_x,gamepad1.right_stick_y,gamepad1.left_stick_x);

        Grabber.setTargetPosition(Grabber.getTargetPosition()-(gamepad1.a?1:0)+(gamepad1.b?1:0));
        power =power-(gamepad1.x?0.02:0)+(gamepad1.y?0.02:0);
        Grabber.setPower(power);*/
        telemetry.addData("Pos",Grabber.getCurrentPosition());
        telemetry.addData("Pos_target",Grabber.getTargetPosition());
        telemetry.addData("Power",Grabber.getPower());
        telemetry.addData("Power_setting",power);
        telemetry.addData("isBusy",Grabber.isBusy());
        telemetry.addData("Current Mode",Grabber.getMode());
    }
}

