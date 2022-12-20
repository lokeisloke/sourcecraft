package converter.actions.actions;

import basic.Loggger;
import converter.Orientation;
import converter.Skins;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;
import minecraft.Property;
import vmfWriter.Cuboid;
import vmfWriter.Skin;

public class Door extends Action {
	private Mapper context;

	/*
		 * For half - Need to find how to set skin for upper/lower
		 * For facing - Shouldnt need any action because all sides use the same texture
		 * For hinge - Need to set skin with orientation by negating texture scale on x axis
		 * 
		 * minecraft:oak_door Properties:
		 *     hinge: left/right
		 *     half: upper/lower
		 *     powered: true/false
		 *     facing: north/south/east/west
		 *     open: true/false
	*/
	
	@Override
	public void add(Mapper context, Position position, Block block) {

		this.context = context;
		Position end = context.getCuboidFinder()
				.getBestY(position, block);
		String open = block.getProperty(Property.open);
		if (open.equals("true")) {
			this.handleOpen(position, end, block);
		} else {
			this.handleClosed(position, end, block);
		}
		context.markAsConverted(position, end);
	}

	// north
	// startOffset = new Position(0, 0, 13);
	// endOffset = new Position(0, 0, 0);

	// south
	// startOffset = new Position(0, 0, 0);
	// endOffset = new Position(0, 0, 13);

	// east
	// startOffset = new Position(0, 0, 0);
	// endOffset = new Position(13, 0, 0);

	// west
	//startOffset = new Position(13, 0, 0);
	//endOffset = new Position(0, 0, 0);

	private void handleOpen(Position position, Position end, Block block) {
		Position startOffset, endOffset;
		String dir = block.getProperty(Property.facing);
		if (dir.equals("north")) {
			startOffset = new Position(13, 0, 0);
			endOffset = new Position(0, 0, 0);
		} else if (dir.equals("south")) {
			startOffset = new Position(0, 0, 0);
			endOffset = new Position(13, 0, 0);
		} else if (dir.equals("east")) {
			startOffset = new Position(0, 0, 13);
			endOffset = new Position(0, 0, 0);
		} else {
			startOffset = new Position(0, 0, 0);
			endOffset = new Position(0, 0, 13);
		}
		int pixels = 16;
		Cuboid door = context.createCuboid(position, end, pixels, startOffset, endOffset, block);
		door.setSkin(this.constructSkin(block));
		context.addDetail(door);
	}

	private void handleClosed(Position position, Position end, Block block) {
		Position startOffset, endOffset;
		String dir = block.getProperty(Property.facing);
		if (dir.equals("north")) {
			startOffset = new Position(0, 0, 13);
			endOffset = new Position(0, 0, 0);
		} else if (dir.equals("south")) {
			startOffset = new Position(0, 0, 0);
			endOffset = new Position(0, 0, 13);
		} else if (dir.equals("east")) {
			startOffset = new Position(0, 0, 0);
			endOffset = new Position(13, 0, 0);
		} else {
			startOffset = new Position(13, 0, 0);
			endOffset = new Position(0, 0, 0);
		}
		int pixels = 16;
		Cuboid door = context.createCuboid(position, end, pixels, startOffset, endOffset, block);

		/*
		String hinge = block.getProperty(Property.hinge);
		if (hinge.equals("right")) {
			double[] textureScale = new double[3];
			textureScale[0] = -1; // x
			textureScale[1] = 1; // y
			textureScale[2] = 1; // z
			door.setTextureScale(textureScale);
		}
		*/

		door.setSkin(this.constructSkin(block));
		context.addDetail(door);
	}

	private Skin constructSkin(Block block) {
		Skin oldSkin = Skins.INSTANCE.getSkin(block);
		String main = oldSkin.materialFront;

		String doorBase = "_bottom";
		if (main.endsWith("_top")) {
			doorBase = main.substring(0, main.length() - "_top".length());
		} else if (main.endsWith("_bottom")) {
			doorBase = main.substring(0, main.length() - "_bottom".length());
		}

		String half = block.getProperty(Property.half);
		if (half.equals("lower")) {
			main = doorBase + "_bottom";
		} else if (half.equals("upper")) {
			main = doorBase + "_top";
		}
		return new Skin(main, oldSkin.scale);
		// TODO: Modify scale to allow changing of individual axis (use negative value to flip texture on specific axis)
		//return new Skin(main, oldSkin.scale * -1); // Flips texture in all axis sad
	}
}