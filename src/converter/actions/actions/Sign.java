package converter.actions.actions;

import java.util.Map;
import basic.Loggger;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;
import vmfWriter.entity.solidEntity.FuncIllusionary;
// For non-solid entities should replace FuncIllusionary with func_brush wish solidity set to never solid
// Needs texture wrapping using texture offsets somehow

public class Sign extends Action {
	@Override
	public void add(Mapper context, Position p, Block block) {
		Loggger.log(block.getName() + " Properties:");
		Map<String, String> properties = block.getProperties();
		for (String property : properties.keySet()) {
			Loggger.log("    " + property + ": " + properties.get(property).toString());
		}
		this.handleFacingAndDir(context, p, block);
		context.markAsConverted(p);
	}

	private void handleFacingAndDir(Mapper context, Position p, Block block) {
		if (getSignType(block).equals("wall")) {
			this.addWallSign(context, p, block);
		} else if (getSignType(block).equals("standing")) {
			this.addFloorSign(context, p, block);
		}
	}

	private String getSignType(Block block) {
		if (block.getProperties() == null) {
			return "standing";
		}
		
		if (block.getProperties().get("rotation") != null) {
			return "standing";
		} else if (block.getProperties().get("facing") != null) {
			return "wall";
		} else if (block.getProperties().get("attached") != null) {
			return "hanging";
		}

		return "standing";
	}

	private void addWallSign(Mapper context, Position p, Block block) {
		int parts = 16;
		Position[] out = new Position[2];
		String dir = block.getProperties().get("facing");
		if (dir.equals("north")) {
			out[0] = new Position(0, 4, 14);
			out[1] = new Position(0, 4, 0);
		} else if (dir.equals("south")) {
			out[0] = new Position(0, 4, 0);
			out[1] = new Position(0, 4, 14);
		} else if (dir.equals("east")) {
			out[0] = new Position(0, 4, 0);
			out[1] = new Position(14, 4, 0);
		} else {
			out[0] = new Position(14, 4, 0);
			out[1] = new Position(0, 4, 0);
		}
		context.addSolidEntity(new FuncIllusionary(context.createCuboid(p, p, parts, out[0], out[1], block)));
	}

	private void addFloorSign(Mapper context, Position p, Block block) {
		int parts = 16;
		Position[] out = new Position[2];
		Position[] out2 = new Position[2];
		String dir = block.getProperties().get("rotation");
		int rotation = Integer.parseInt(dir);

		// Only use N/S/E/W until rotation of brushes is figured out
		if ((rotation >= 0 && rotation <= 2) || (rotation >= 14 && rotation <= 15)) { // S SSW SW SE SSE; All South
			out[0] = new Position(0, 9, 7);
			out[1] = new Position(0, -1, 7);
		} else if (rotation >= 3 && rotation <= 5) { // WSW W WNW; All West
			out[0] = new Position(7, 9, 0);
			out[1] = new Position(7, -1, 0);
		} else if (rotation >= 6 && rotation <= 10) { // NW NNW N NNE NE; All North
			out[0] = new Position(0, 9, 7);
			out[1] = new Position(0, -1, 7);
		} else if (rotation >= 11 && rotation <= 13) { // ENE E ESE; All East
			out[0] = new Position(7, 9, 0);
			out[1] = new Position(7, -1, 0);
		}

		out2[0] = new Position(7, 0, 7);
		out2[1] = new Position(7, 7, 7);

		context.addSolidEntity(new FuncIllusionary(context.createCuboid(p, p, parts, out[0], out[1], block)));
		context.addSolidEntity(new FuncIllusionary(context.createCuboid(p, p, parts, out2[0], out2[1], block)));

	}
}
