package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.TileRunner;


/**
 * Basic iterative Teleop OpMode for a tile runner based robot.
 */

@TeleOp( name = "Competition Teleop", group = "Competition" )
public class CompetitionTeleop extends OpMode
{
	private final double ARM_POWER_LIMIT = 0.5;
	private final double REVERSE_DRIVE_SPEED = 0.5;
	// Declare OpMode members.
	TileRunner robot = new TileRunner();
	//constants for lift limits
	private final double LIFT_MIN = 0;
	private final double LIFT_MAX = 5000;
	private final double LIFT_CORRECT_POWER = 0.2;

	//constants for arm limits
	private final double ARM_MIN = 0;
	private final double ARM_MAX = 5000;
	private final double ARM_CORRECT_POWER = 0.2;

	private final double DUMPER_OPEN = 90;
	private final double DUMPER_CLOSED = 0;

	private int armBrake;

	// Code to run ONCE when the driver hits INIT
	@Override
	public void init()
	{
		/* Initialize the hardware variables.
		 * The init() method of the hardware class does all the work here
		 */
		robot.init( hardwareMap );

		// Send telemetry message to signify robot waiting;
		telemetry.addLine("Initialized");
	}


	// Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
	@Override
	public void init_loop()
	{
	}


	// Code to run ONCE when the driver hits PLAY
	@Override
	public void start()
	{
		//this makes the robot arm brake at the current position.
		armBrake = robot.arm.getCurrentPosition();
	}


	// Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
	@Override
	public void loop()
	{
		// Read the joystick values for the arm, lift, left drive, and right drive power
		double armPower = gamepad2.left_stick_y * ARM_POWER_LIMIT;
		double liftPower = gamepad2.right_stick_y;
		double leftDrivePower = gamepad1.left_stick_y;
		double rightDrivePower = gamepad1.right_stick_y;

		//read trigger values for mineral infeed and outfeed.
		double infeed = gamepad1.left_trigger;
		double outfeed = gamepad1.right_trigger;
		//this calculates the acutal value passed into the motor
		double intakePower = infeed - outfeed;

		// Set power to the motors

		//This makes the arm motor brake when the joystick is not moved
		if(armPower == 0)
		{
			robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			robot.arm.setTargetPosition(armBrake);
			robot.arm.setPower(ARM_POWER_LIMIT);
			telemetry.addData("Braking", "True");
		}
		else
		{
			telemetry.addData("Braking", "False");
			robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			robot.arm.setPower(ARM_POWER_LIMIT * armPower);
			armBrake = robot.arm.getCurrentPosition();
		}
		robot.intake.setPower(intakePower);
		robot.lift.setPower(liftPower);

		//this opens the mineral dumper if the left bumper is pressed
		if(gamepad2.left_bumper)
		{
			robot.dumper.setPosition(DUMPER_OPEN);
		}
		else
		{
			robot.dumper.setPosition(DUMPER_CLOSED);
		}

		//this reverses driving if the right bumper on controller one is pressed
		if(gamepad1.right_bumper)
		{
			robot.leftDrive.setPower(-rightDrivePower * REVERSE_DRIVE_SPEED);
			robot.rightDrive.setPower(-leftDrivePower * REVERSE_DRIVE_SPEED);
		}
		else
		{
			robot.leftDrive.setPower(leftDrivePower);
			robot.rightDrive.setPower(rightDrivePower);
		}

		// Send telemetry data to driver station.
		double liftPosition = robot.lift.getCurrentPosition();
		telemetry.addData("Lift Position", "" + liftPosition);
		telemetry.update();
	}

	 // Code to run ONCE after the driver hits STOP
	@Override
	public void stop()
	{
	}
}
