package converter.actions.actions;

import java.util.Map;
import basic.Loggger;

import converter.Orientation;
import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;
import minecraft.Property;

public class Wall extends Action {

	private static int BEAM_SIDE = 5;
	private static int BEAM_TOP_OFF = 2;
	private static int BEAM_TOP_ON = 0;
	private Mapper context;

	@Override
	public boolean hasWall(Orientation orientation) {
		return true;
	}

	@Override
	public void add(Mapper context, Position p, Block block) {
		this.context = context;
		Position end = context.getCuboidFinder()
				.getBestY(p, block);
		Position startOffset = new Position(2, 0, 2);
		Position endOffset = new Position(2, 0, 2);
		this.addPole(p, end, block, startOffset, endOffset);
		this.addBeams(p, end, block, startOffset, endOffset);
		this.context.markAsConverted(p, end);
	}

	private void addPole(Position p, Position end, Block block, Position startOffset, Position endOffset) {
		boolean north = block.getProperty(Property.north)
				.equals("low");
		boolean south = block.getProperty(Property.south)
				.equals("low");
		boolean east = block.getProperty(Property.east)
				.equals("low");
		boolean west = block.getProperty(Property.west)
				.equals("low");

		int parts = 8;
		if (east && west && (north && !south || south && !north)) {
			this.context.addDetail(context.createCuboid(p, end, parts, startOffset, endOffset, block));
		} else if (north && south && (east && !west || west && !east)) {
			this.context.addDetail(context.createCuboid(p, end, parts, startOffset, endOffset, block));
		} else if (!(east && west) && !(north && south)) {
			this.context.addDetail(context.createCuboid(p, end, parts, startOffset, endOffset, block));
		}
	}

	private void addBeams(Position p, Position end, Block block, Position startOffset, Position endOffset) {
		//Loggger.log("Adding beam for " + block.getName());
		//Loggger.log("  Properties:");
		//Map<String, String> properties = block.getProperties();
		//for (String property : properties.keySet()) {
		//	Loggger.log("    " + property + ": " + properties.get(property).toString());
		//}

		boolean north = block.getProperty(Property.north)
				.equals("low");
		boolean south = block.getProperty(Property.south)
				.equals("low");
		boolean east = block.getProperty(Property.east)
				.equals("low");
		boolean west = block.getProperty(Property.west)
				.equals("low");

		int parts = 16;
		while (p.getY() <= end.getY()) {
			if (east && west) {
				this.addEWBeams(p, startOffset, endOffset, parts, block);
			} else if (east) {
				this.addEastBeams(p, startOffset, endOffset, parts, block);
			} else if (west) {
				this.addWestBeams(p, startOffset, endOffset, parts, block);
			}

			if (north && south) {
				this.addNSBeams(p, startOffset, endOffset, parts, block);
			} else if (south) {
				this.addSouthBeams(p, startOffset, endOffset, parts, block);
			} else if (north) {
				this.addNorthBeams(p, startOffset, endOffset, parts, block);
			}
			p.move(0, 1, 0);
		}
	}

	private void addEWBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(0, Wall.BEAM_TOP_ON, Wall.BEAM_SIDE);
		endOffset = new Position(parts, Wall.BEAM_TOP_OFF, Wall.BEAM_SIDE);
		context.addDetail(context.createCuboid(p, p.getOffset(1, 0, 0), parts, startOffset, endOffset, block));
	}

	private void addNSBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_ON, parts);
		endOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_OFF, 0);
		context.addDetail(context.createCuboid(p.getOffset(0, 0, -1), p, parts, startOffset, endOffset, block));
	}

	private void addEastBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(10, Wall.BEAM_TOP_ON, Wall.BEAM_SIDE);
		endOffset = new Position(parts, Wall.BEAM_TOP_OFF, Wall.BEAM_SIDE);
		context.addDetail(context.createCuboid(p, p.getOffset(1, 0, 0), parts, startOffset, endOffset, block));
	}

	private void addWestBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(parts, Wall.BEAM_TOP_ON, Wall.BEAM_SIDE);
		endOffset = new Position(10, Wall.BEAM_TOP_OFF, Wall.BEAM_SIDE);
		context.addDetail(context.createCuboid(p.getOffset(-1, 0, 0), p, parts, startOffset, endOffset, block));
	}

	private void addSouthBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_ON, 10);
		endOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_OFF, parts);
		context.addDetail(context.createCuboid(p, p.getOffset(0, 0, 1), parts, startOffset, endOffset, block));
	}

	private void addNorthBeams(Position p, Position startOffset, Position endOffset, int parts, Block block) {
		startOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_ON, parts);
		endOffset = new Position(Wall.BEAM_SIDE, Wall.BEAM_TOP_OFF, 10);
		context.addDetail(context.createCuboid(p.getOffset(0, 0, -1), p, parts, startOffset, endOffset, block));
	}
}
