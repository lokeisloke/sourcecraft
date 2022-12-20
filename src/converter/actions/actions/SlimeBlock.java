package converter.actions.actions;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;

public class SlimeBlock extends Action {

	@Override
	public void add(Mapper context, Position position, Block block) {
		Position end = context.getCuboidFinder()
				.getBestY(position, block);
		int parts = 10;
		Position offset = new Position(2, 2, 2);
		Position negativeOffset = new Position(2, 2, 2);
		context.addDetail(context.createCuboid(position, end, parts, offset, negativeOffset, block));

		parts = 16;
		offset = new Position(0, 0, 0);
		negativeOffset = new Position(0, 0, 0);
		context.addDetail(context.createCuboid(position, end, parts, offset, negativeOffset, block));
		context.markAsConverted(position, end);
	}
}
