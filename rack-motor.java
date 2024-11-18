package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Rack Test", group="2024-2025")
public class Rack_Test extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor BL_Motor = null;
    public DcMotor BR_Motor = null;
    public DcMotor FL_Motor = null;
    public DcMotor FR_Motor = null;

	public DcMotor Rack_Motor = null;
	
    @Override
    public void runOpMode() {
        BR_Motor = hardwareMap.get(DcMotor.class, "BR Wheel Motor");
        FL_Motor = hardwareMap.get(DcMotor.class, "FL Wheel Motor");
        BL_Motor = hardwareMap.get(DcMotor.class, "BL Wheel Motor");
        FR_Motor = hardwareMap.get(DcMotor.class, "FR Wheel Motor");

		Rack_Motor = hardwareMap.get(DcMotor.class, "Rack Motor");

		//Drive Test
        //FL_Motor.setDirection(DcMotor.Direction.REVERSE);
        //BL_Motor.setDirection(DcMotor.Direction.REVERSE);
        //FR_Motor.setDirection(DcMotor.Direction.FORWARD);
        //BR_Motor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;
			
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower),Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels
            FL_Motor.setPower(leftFrontPower);
            FR_Motor.setPower(rightFrontPower);
            BL_Motor.setPower(leftBackPower);
            BR_Motor.setPower(rightBackPower);

			Rack_Motor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
				


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();

		  // Reset the motor encoder so that it reads zero ticks
      motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

      // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
      motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

      waitForStart();
		        // Get the current position of the motor
            int position = motor.getCurrentPosition();
            double revolutions = position/CPR;

            double angle = revolutions * 360;
            double angleNormalized = angle % 360;

            double distance = circumference * revolutions;
        }
    }}
