package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by samuel on 8/11/17
 */

@Autonomous( name="SquareAuto", group="Iterative OpMode")
public class SquareAuto extends LinearOpMode implements BusyWaitHandler{

    private Tilerunner tileRunner = new Tilerunner();

    private String mode = "Stopped";

    @Override
    public void runOpMode() throws InterruptedException {
        tileRunner.init(hardwareMap, telemetry);

        // Set up our telemetry dashboard
        composeTelemetry();

        // Wait until we're told to go
        waitForStart();

        // Start the logging of measured acceleration
        tileRunner.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        mode = "Calibrating";
        tileRunner.turn(this, 0.5, 180);
        tileRunner.turn(this, -0.5, 180);

        while (opModeIsActive()) {
            moveEdge();
        }

    }

    private void moveEdge() {
        mode = "forward";
        tileRunner.move(this, 36, 1);
        mode = "turning";
        tileRunner.turn(this, 1, 90);
    }

    private void composeTelemetry() {
        telemetry.addLine()
                .addData("current position (left)", new Func<Integer>() {
                    @Override
                    public Integer value() {
                        return tileRunner.leftMotor.getCurrentPosition();
                    }
                })
                .addData("current position (right)", new Func<Integer>() {
                    @Override
                    public Integer value() {
                        return tileRunner.rightMotor.getCurrentPosition();
                    }
                });

        telemetry.addData("target position (left)", new Func<Integer>() {
            @Override
            public Integer value() {
                return tileRunner.leftMotor.getTargetPosition();
            }
        });

        telemetry.addData("heading", new Func<Double>() {
            @Override
            public Double value() {
                return tileRunner.getHeading();
            }
        });

        telemetry.addData("mode", new Func<String>() {
            @Override
            public String value() {
                return mode;
            }
        });
    }

    @Override
    public boolean isActive() {
        telemetry.update();

        return opModeIsActive();
    }
}
