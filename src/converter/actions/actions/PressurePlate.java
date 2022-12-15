package converter.actions.actions;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;

public class PressurePlate extends Action {
	private Mapper context;

	@Override
	public void add(Mapper context, Position position, Block block) {
		this.context = context;
		Position end = context.getCuboidFinder()
				.getBestXZ(position, block);
		this.addCuboid(position, end, block);
		context.markAsConverted(position, end);
	}

	private void addCuboid(Position position, Position end, Block block) {
		Position startOffset, endOffset;
		startOffset = new Position(1, 0, 1);
		endOffset = new Position(1, 15, 1);
		int pixels = 16;
		context.addDetail(context.createCuboid(position, end, pixels, startOffset, endOffset, block));
	}
}