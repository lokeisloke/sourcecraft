package converter.actions.actions;

import converter.actions.Action;
import converter.mapper.Mapper;
import minecraft.Block;
import minecraft.Position;
import vmfWriter.entity.solidEntity.FuncIllusionary;

public class Snow extends Action {
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
		int layers = Integer.parseInt(block.getProperties().get("layers"));
		int layerOffset = 16 - (layers * 2);

		Position startOffset, endOffset;
		startOffset = new Position(0, 0, 0);
		endOffset = new Position(0, layerOffset, 0);
		int pixels = 16;
		if (layers > 1) {
			context.addDetail(context.createCuboid(position, end, pixels, startOffset, endOffset, block));
		} else {
			context.addSolidEntity(new FuncIllusionary(context.createCuboid(position, end, pixels, startOffset, endOffset, block)));
		}
	}
}