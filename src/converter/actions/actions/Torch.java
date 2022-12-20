package converter.actions.actions;

import basic.Loggger;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;
import minecraft.Property;
import vmfWriter.Color;
import vmfWriter.entity.pointEntity.PointEntity;
import vmfWriter.entity.pointEntity.pointEntity.EnvFire;
import vmfWriter.entity.pointEntity.pointEntity.InfoParticleSystem;
import vmfWriter.entity.pointEntity.pointEntity.Light;
import vmfWriter.entity.solidEntity.FuncIllusionary;

public class Torch extends Action {

	public final static int red = 255;
	public final static int blue = 50;
	public final static int green = 243;
	public final static int brigthness = 40;
	public final static int distance50 = 96;
	public final static int distance100 = 256;
	public final static Color LIGHT_COLOR = new Color(Torch.red, Torch.green, Torch.blue, Torch.brigthness);
	public final static Light LIGHT = new Light(Torch.LIGHT_COLOR, Torch.distance50, Torch.distance100);
	private final static String EFFECT_NAME = "flaming_arrow";
	public final static InfoParticleSystem PARTICLE_SYSTEM = new InfoParticleSystem(Torch.EFFECT_NAME, 270, 0, 0);
	protected static final PointEntity FLAME = new EnvFire().setFireSize(3);
	public static final Torch INSTANCE = new Torch();

	@Override
	public void add(Mapper context, Position p, Block material) {
		/*Loggger.log(material.getName() + "  Properties:");
		if (material.getProperties() != null) {
			Loggger.log(material.getProperties().get("facing"));
		}*/

		boolean tryWallTorches = false;

		String facing;
		if (material.getProperties() == null) {
			facing = "none";
		} else {
			facing = material.getProperty(Property.facing);
		}

		if (tryWallTorches && facing.equals("east")) {
			addFacingEast(context, p, material);
		} else if (tryWallTorches && facing.equals("north")) {
			addFacingNorth(context, p, material);
		} else if (tryWallTorches && facing.equals("south")) {
			addFacingSouth(context, p, material);
		} else if (tryWallTorches && facing.equals("west")) {
			addFacingWest(context, p, material);
		} else {
			// Adjust manually in hammer
			// Move X/Y +-15.15
			// Rotate X/Y +-22.5
			// Move Z 10
			int parts = 16;
			Position offset = new Position(7, 0, 7);
			Position negativeOffset = new Position(7, 6, 7);
			context.addSolidEntity(
					new FuncIllusionary(context.createCuboid(p, p, parts, offset, negativeOffset, material)));
			context.setPointToGrid(p);
			context.movePointInGridDimension(0.5, ((double) (parts - negativeOffset.getY())) / ((parts)), 0.5);
			this.addFlame(context);
			context.markAsConverted(p);
		}
	}

	public void addFacingEast(Mapper context, Position p, Block material) {
		int parts = 32;
		Position[] pointOffset = new Position[8];
		pointOffset[0] = new Position(0, 6, 18); // a
		pointOffset[1] = new Position(9, 24, 18); // b
		pointOffset[2] = new Position(13, 22, 18); // c
		pointOffset[3] = new Position(4, 4, 18); // d
		pointOffset[4] = new Position(0, 6, 14); // e
		pointOffset[5] = new Position(9, 24, 14); // f
		pointOffset[6] = new Position(13, 22, 14); // g
		pointOffset[7] = new Position(4, 4, 14); // h

		Position point = Position.create(p);
		context.addSolidEntity(
				new FuncIllusionary(context.createFree8Point(point, point, parts, pointOffset, true, material)));
		context.setPointToGrid(p);
		context.movePointInGridDimension((7.0 / 20.0), 0.7, 0.5);
		this.addFlame(context);
		context.markAsConverted(p);

		//		textureshift: -92
		//		textureshift -24
		//		rotation 28
	}

	public void addFacingNorth(Mapper context, Position p, Block material) {
		int parts = 32;
		Position[] pointOffset = new Position[8];

		pointOffset[0] = new Position(18, 6, 32); // a
		pointOffset[1] = new Position(18, 24, 23); // b
		pointOffset[2] = new Position(18, 22, 19); // c
		pointOffset[3] = new Position(18, 4, 28); // d
		pointOffset[4] = new Position(14, 6, 32); // e
		pointOffset[5] = new Position(14, 24, 23); // f
		pointOffset[6] = new Position(14, 22, 19); // g
		pointOffset[7] = new Position(14, 4, 28); // h

		Position point = Position.create(p);
		context.addSolidEntity(
				new FuncIllusionary(context.createFree8Point(point, point, parts, pointOffset, false, material)));
		context.setPointToGrid(p);
		context.movePointInGridDimension(0.5, 0.7, (13.0 / 20.0));
		this.addFlame(context);
		context.markAsConverted(p);
	}
	public void addFacingSouth(Mapper context, Position p, Block material) {
		int parts = 32;
		Position[] pointOffset = new Position[8];

		pointOffset[0] = new Position(14, 6, 0); // a
		pointOffset[1] = new Position(14, 24, 9); // b
		pointOffset[2] = new Position(14, 22, 13); // c
		pointOffset[3] = new Position(14, 4, 4); // d
		pointOffset[4] = new Position(18, 6, 0); // e
		pointOffset[5] = new Position(18, 24, 9); // f
		pointOffset[6] = new Position(18, 22, 13); // g
		pointOffset[7] = new Position(18, 4, 4); // h

		Position point = Position.create(p);
		context.addSolidEntity(
				new FuncIllusionary(context.createFree8Point(point, point, parts, pointOffset, false, material)));
		context.setPointToGrid(p);
		context.movePointInGridDimension(0.5, 0.7, (7.0 / 20.0));
		this.addFlame(context);
		context.markAsConverted(p);
	}
	public void addFacingWest(Mapper context, Position p, Block material) {
		int parts = 32;
		Position[] pointOffset = new Position[8];

		pointOffset[0] = new Position(32, 6, 14); // a
		pointOffset[1] = new Position(23, 24, 14); // b
		pointOffset[2] = new Position(19, 22, 14); // c
		pointOffset[3] = new Position(28, 4, 14); // d
		pointOffset[4] = new Position(32, 6, 18); // e
		pointOffset[5] = new Position(23, 24, 18); // f
		pointOffset[6] = new Position(19, 22, 18); // g
		pointOffset[7] = new Position(28, 4, 18); // h

		Position point = Position.create(p);
		context.addSolidEntity(
				new FuncIllusionary(context.createFree8Point(point, point, parts, pointOffset, true, material)));
		context.setPointToGrid(p);
		context.movePointInGridDimension((13.0 / 20.0), 0.7, 0.5);
		this.addFlame(context);
		context.markAsConverted(p);
	}

	public void addFlame(Mapper context) {
		context.addPointEntity(Torch.PARTICLE_SYSTEM);
		context.movePointInGridDimension(0, 1.0 / ((16)), 0);
		context.addPointEntity(Torch.FLAME);
		context.addPointEntity(Torch.LIGHT);
	}
}
