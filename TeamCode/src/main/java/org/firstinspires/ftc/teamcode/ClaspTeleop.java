package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by dcrenshaw on 3/4/18.
 * Clasp is an intelligent teleop designed to interface with Anvil and provide an overhaul to
 * classic teleop. Clasp is built for the Tank drive train, but should work with any drive train.
 */
/* CHANGELOG:
 * Anvil-controlled speed is no longer used anywhere in Clasp. A new, more flexible speed control
   system has been added in its place: Computer-Assisted Zones
 * Added changelog
 */

public class ClaspTeleop extends OpMode {
    public Anvil anvil;

    public boolean speedMode = true;

    public double[] zoneList = new double[]{0.35, 0.5, 0.75};

    public void init() {
        anvil = new Anvil();
        anvil.init(hardwareMap, "TANK", telemetry);
    }
    public void loop() {
        //Handle buttons first
        if (gamepad1.a) { //Emergency brake. Stops all motors immediately.
            anvil.rest();
            while (gamepad1.a) {
                if (!(gamepad1.a)) break;
            }
        }
        else if (gamepad1.b) {
            //Unused button
        }
        else if (gamepad1.x) {
            //Unused button
        }
        else if (gamepad1.y) {
            //Unused button
        }
        else if (gamepad1.dpad_left) speedMode ^= true;
        else if (gamepad1.dpad_down) swapControllers();
        //Handle controls
        if (gamepad1.atRest()) {
            anvil.rest();
        }
        else {
            if (Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
                if (gamepad1.left_stick_x > 0) {
                    if (speedMode) {
                        anvil.turnLeft(computerAssistedZones());
                    } else {
                        anvil.turnLeft(gamepad1.left_stick_x);
                    }
                }
                else {
                    if (speedMode) {
                        anvil.turnRight(computerAssistedZones());
                    } else {
                        anvil.turnRight(-gamepad1.left_stick_x);
                    }
                }
            }
            else {
                if (gamepad1.left_stick_y > 0) {
                    if (speedMode) {
                        anvil.moveBackward(computerAssistedZones());
                    } else {
                        anvil.moveBackward(gamepad1.left_stick_y);
                    }
                }
                else {
                    if (speedMode) {
                        anvil.moveForward(computerAssistedZones());
                    } else {
                        anvil.moveForward(-gamepad1.left_stick_y);
                    }
                }
            }
        }
    }
    public void swapControllers() {
        Gamepad r = gamepad1; //Runner
        gamepad1 = gamepad2;
        gamepad2 = r;
    }
    public double computerAssistedZones() {
        double a = Math.sqrt(Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2));
        return zoneList[(int) Math.ceil(a*zoneList.length) - 1];
    }
}
