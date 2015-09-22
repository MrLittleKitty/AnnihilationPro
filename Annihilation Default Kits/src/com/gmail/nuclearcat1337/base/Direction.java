package com.gmail.nuclearcat1337.base;

import org.bukkit.util.Vector;

public enum Direction
{
	North,
	South,
	East,
	West,
	NorthWest,
	NorthEast,
	SouthWest,
	SouthEast;
	
	public Vector getVector()
	{
		switch(this)
		{
			case North:
			default:
				return new Vector(0,0,-1);
			case South:
				return new Vector(0,0,1);
			case West:
				return new Vector(-1,0,0);
			case East:
				return new Vector(1,0,0);
			case NorthWest:
				return new Vector(-1,0,-1);
			case NorthEast:
				return new Vector(1,0,-1);
			case SouthWest:
				return new Vector(-1,0,1);
			case SouthEast:
				return new Vector(1,0,1);
		}
	}
	
	public static Direction getDirection(Vector vec)
	{
		Vector k = vec.normalize();
		int x = k.getBlockX();
		int z = k.getBlockZ();
		if(z < 1)
		{
			if(x<0)
				return Direction.NorthWest;
			else if(x>0)
				return Direction.NorthEast;
			else 
				return Direction.North;
		}
		else if(z > 1)
		{
			if(x<0)
				return Direction.SouthWest;
			else if(x>0)
				return Direction.SouthEast;
			else 
				return Direction.South;
		}
		else if(x < 0)
			return Direction.West;
		else return Direction.East;
	}
	
	public static Direction getOpposite(Direction direc)
	{
		switch(direc)
		{
			default:
				return null;
			case North:
				return Direction.South;
			case South:
				return Direction.North;
			case East:
				return Direction.West;
			case West:
				return Direction.East;
			case NorthWest:
				return Direction.SouthEast;
			case NorthEast:
				return Direction.SouthWest;
			case SouthWest:
				return Direction.NorthEast;
			case SouthEast:
				return Direction.NorthWest;
		}
	}
	
	
	public static Direction getOpposite(Vector vec)
	{
		return Direction.getOpposite(Direction.getDirection(vec));
	}
	
//	public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
//    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
//	   
//    /**
//    * Gets the horizontal Block Face from a given yaw angle<br>
//    * This includes the NORTH_WEST faces
//    *
//    * @param yaw angle
//    * @return The Block Face of the angle
//    */
//    public static BlockFace yawToFace(float yaw) {
//        return yawToFace(yaw, true);
//    }
// 
//    /**
//    * Gets the horizontal Block Face from a given yaw angle
//    *
//    * @param yaw angle
//    * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be returned
//    * @return The Block Face of the angle
//    */
//    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
//        if (useSubCardinalDirections) {
//            return radial[Math.round(yaw / 45f) & 0x7];
//        } else {
//            return axis[Math.round(yaw / 90f) & 0x3];
//        }
//    }
}
