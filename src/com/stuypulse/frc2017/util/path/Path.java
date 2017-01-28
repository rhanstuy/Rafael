package com.stuypulse.frc2017.util.path;

import java.util.ArrayList;

import com.stuypulse.frc2017.util.Vector;

/**
 * Holds a list of Vectors to form a path (a poly-line), 
 * tracks a bots position in order to follow the path,
 * 
 *
 * Assume that the bot starts at (0,0) and plot the destinations accordingly
 *
 */

public class Path {
	private ArrayList<Vector> destinations;
	private double botX, botY;

	// The current destination we are "aiming" for.
	private int destinationIndex;

	// How close we get to the destination before we are "close enough" to continue
	private static final int CLOSE_ENOUGH_THRESHHOLD = 1;

	public Path(ArrayList<Vector> destinations) {
		this.destinations = destinations;
		botX = 0.0;
		botY = 0.0;
		destinationIndex = 0;
	}

	// Update bot position
	public void setRobotPosition(double botX, double botY) {
		this.botX = botX;
		this.botY = botY;
	}

	// Returns an array telling us what to set the robot motor speeds (using tankDrive)
	// returns: new double[] { leftMotorSpeed, rightMotorSpeed };
	public double[] getDesiredMotorSpeeds() {
		return new double[] {0, 0};
	}

	
}