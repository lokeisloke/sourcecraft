package converter;

import java.util.function.Supplier;

import basic.NameSupplier;
import converter.actions.BlockMap;
import minecraft.Block;
import minecraft.Blocks;
import minecraft.Material;
import minecraft.Texture;
import periphery.TexturePack;
import vmfWriter.Skin;

public class Skins {

	private static final double DEFAULT_SCALE = 0.25;
	public static Skins INSTANCE;

	public static final void init(TexturePack texturePack, int optionScale) {
		INSTANCE = new Skins(texturePack.getName(), texturePack.getTextureSize(), optionScale);
	}

	private static final String NODRAW_TEXTURE = "tools/toolsnodraw";

	public static final Skin NODRAW = new Skin(NODRAW_TEXTURE, DEFAULT_SCALE);
	public static final Skin TRIGGER = new Skin("tools/toolstrigger", DEFAULT_SCALE);
	public static final Skin SKYBOX = new Skin("tools/toolsskybox", DEFAULT_SCALE);
	private static final Skin PLAYER_CLIP = new Skin("tools/toolsplayerclip", DEFAULT_SCALE);
	private static final Skin LADDER = new Skin("tools/toolsinvisibleladder", DEFAULT_SCALE);

	public static final String DEFAULT_TEXTURE = "dev/dev_measurecrate02";

	private double textureScale;
	private Skin[] skinLegacy;

	private String folder;

	public Skin setSourceSkin(String texture) {
		return new Skin(texture, this.textureScale);
	}

	public Skin createSkin(String main) {
		return new Skin(this.folder + main, this.textureScale);
	}

	public Skin createSkin(NameSupplier main, NameSupplier topBottom) {
		return new Skin(this.folder + main.getName(), this.folder + topBottom.getName(), this.textureScale);
	}

	private Skin createSkinTopBottom(NameSupplier side, NameSupplier top, NameSupplier bottom) {
		return new Skin(this.folder + side.getName(), this.folder + top.getName(), this.folder + side.getName(),
				this.folder + bottom.getName(), Orientation.NORTH, this.textureScale);
	}

	public Skin createSkinTopFront(NameSupplier main, NameSupplier top, NameSupplier front, Orientation orientation) {
		return new Skin(this.folder + main.getName(), this.folder + top.getName(), this.folder + front.getName(),
				orientation, this.textureScale);
	}

	public Skin createSkinTopFrontBottom(NameSupplier main, NameSupplier top, NameSupplier front, NameSupplier bottom) {
		return new Skin(this.folder + main.getName(), this.folder + top.getName(), this.folder + front.getName(),
				this.folder + bottom.getName(), Orientation.NORTH, this.textureScale);
	}

	public Skin createSkinTopFrontBottom(NameSupplier main, NameSupplier top, NameSupplier front, NameSupplier bottom,
			Orientation orientation) {
		return new Skin(this.folder + main.getName(), this.folder + top.getName(), this.folder + front.getName(),
				this.folder + bottom.getName(), orientation, this.textureScale);
	}

	public Skins(String folder, int textureSizeNew, int scale) {
		this.init(folder, textureSizeNew, scale);
	}

	private BlockMap<Skin> skins;

	/**
	 * Returns also whether given material has suffix
	 *
	 * @param suffix
	 * @return
	 */
	private boolean putPrefixMadeSuffix(Material material, Material suffix) {
		String name = material.name();
		String _suf = suffix.name();
		if (name.endsWith(_suf)) { // except dark oak
			String suf_ = _suf.substring(1) + "_";
			String textureName = suf_ + name.substring(0, name.length() - suf_.length());
			this.put(material, textureName);
			return true;
		}
		return false;
	}

	public void init(String folder, int textureSizeNew, int scale) {
		this.textureScale = ((double) scale) / ((double) textureSizeNew);
		this.folder = folder + "/";
		this.skins = new BlockMap<Skin>().setDefault(new Skin(DEFAULT_TEXTURE, this.textureScale));

		// set based on matching texture name
		for (Texture texture : Texture.values()) {
			this.put(texture);
		}

		// set based on common
		for (Material material : Material.values()) {
			String name = material.name();
			if (name.endsWith("_log")) {
				this.put(material, this.createSkin(() -> name, () -> name + "_top"));
			} else if (name.endsWith("_wood")) {
				this.put(material, name.substring(0, name.length() - "_wood".length()) + "_log");
			} else if (name.endsWith("_carpet")) {
				this.put(material, name.substring(0, name.length() - "_carpet".length()) + "_wool");
			//} else if (name.endsWith("_pressure_plate")) {
				//this.put(material, name.substring(0, name.length() - "_pressure_plate".length()) + "_wool");
			} else if (name.equals("chest")) {
				this.put(material, this.createSkinTopFront(() -> "chest_side", () -> "chest_top", () -> "chest_front",
						Orientation.NORTH));
			}
		}

		// material-prefixes
		this.put(Material.brick_, Texture.bricks);
		this.put(Material.petrified_oak_, Texture.oak_planks);
		this.put(Material.infested_stone_bricks, Texture.stone_bricks);

		this.put(Material.end_stone_brick_, Texture.end_stone_bricks);
		this.put(Material.mossy_stone_brick_, Texture.mossy_stone_bricks);
		this.put(Material.prismarine_brick_, Texture.prismarine_bricks);
		this.put(Material.nether_brick_, Texture.nether_bricks);
		this.put(Material.stone_brick_, Texture.stone_bricks);
		this.put(Material.oak_, Texture.oak_planks);
		this.put(Material.dark_oak_, Texture.dark_oak_planks);
		this.put(Material.spruce_, Texture.spruce_planks);
		this.put(Material.birch_, Texture.birch_planks);
		this.put(Material.acacia_, Texture.acacia_planks);
		this.put(Material.jungle_, Texture.jungle_planks);
		this.put(Material.purpur_, Texture.purpur_block);
		this.put(Material.prismarine_, Texture.prismarine);
		this.put(Material.red_nether_brick_, Texture.red_nether_bricks);


		/*
		this.skins.put(Material.sandstone,
				this.createSkinTopBottom(Texture.sandstone_normal, Texture.sandstone_top, Texture.sandstone_normal));
		this.put(Material.sandstone, Texture.sandstone);
		this.skins.put(Material.red_sandstone, this.createSkinTopBottom(Texture.red_sandstone_normal,
				Texture.red_sandstone_top, Texture.red_sandstone_normal));
		
		// exceptions

		*/
		// other material
		this.put(Material.grass_block, this.createSkinTopBottom(Texture.grass_block_side, Texture.grass_block_top, Texture.dirt));
		this.put(Material.podzol, this.createSkinTopBottom(Texture.podzol_side, Texture.podzol_top, Texture.dirt));
		this.put(Material.mycelium, this.createSkinTopBottom(Texture.mycelium_side, Texture.mycelium_top, Texture.dirt));
		this.put(Material.dirt_path, this.createSkinTopBottom(Texture.dirt_path_side, Texture.dirt_path_top, Texture.dirt));

		//this.put(Material.snow_block, Texture.snow);

		this.put(Material.sandstone, this.createSkinTopBottom(Texture.sandstone, Texture.sandstone_top, Texture.sandstone_bottom));
		this.put(Material.cut_sandstone, this.createSkin(Texture.cut_sandstone, Texture.sandstone_top));
		this.put(Material.chiseled_sandstone, this.createSkin(Texture.chiseled_sandstone, Texture.sandstone_top));
		this.put(Material.smooth_sandstone, Texture.sandstone_top);

		this.put(Material.red_sandstone_wall, this.createSkinTopBottom(Texture.red_sandstone, Texture.red_sandstone_top, Texture.red_sandstone_bottom));
		this.put(Material.red_sandstone, this.createSkinTopBottom(Texture.red_sandstone, Texture.red_sandstone_top, Texture.red_sandstone_bottom));
		this.put(Material.cut_red_sandstone, this.createSkin(Texture.cut_red_sandstone, Texture.red_sandstone_top));
		this.put(Material.chiseled_red_sandstone, this.createSkin(Texture.chiseled_red_sandstone, Texture.red_sandstone_top));
		this.put(Material.smooth_red_sandstone, Texture.red_sandstone_top);

		this.put(Material.heavy_weighted_pressure_plate, Texture.iron_block);
		this.put(Material.light_weighted_pressure_plate, Texture.gold_block);
		this.put(Material.purpur_pillar, this.createSkin(Texture.purpur_pillar, Texture.purpur_pillar_top));

		this.put(Material.bone_block, this.createSkin(Texture.bone_block_side, Texture.bone_block_top));

		this.put(Material.quartz_, Texture.quartz_block_side);

		this.put(Material.quartz_block, this.createSkinTopBottom(Texture.quartz_block_side, Texture.quartz_block_top, Texture.quartz_block_bottom));
		this.put(Material.chiseled_quartz_block, this.createSkin(Texture.chiseled_quartz_block, Texture.chiseled_quartz_block_top));
		this.put(Material.quartz_pillar, this.createSkin(Texture.quartz_pillar, Texture.quartz_pillar_top));
		this.put(Material.smooth_quartz, Texture.quartz_block_bottom);

		this.put(Material.tnt, this.createSkinTopBottom(Texture.tnt_side, Texture.tnt_top, Texture.tnt_bottom));

		Skin waterSkin = new Skin(NODRAW_TEXTURE, this.folder + "water_still", NODRAW_TEXTURE, NODRAW_TEXTURE,
				this.textureScale);
		for (Material m : new Material[] { Material.water, Material.seagrass, Material.tall_seagrass, Material.kelp,
				Material.kelp_plant }) {
			this.put(m, waterSkin);
		}

		this.put(Material.lava, Texture.lava_still);
		this.put(Material.magma_block, Texture.magma);

		this.put(Material.note_block, Texture.jukebox_side);
		this.put(Material.jukebox, this.createSkinTopBottom(Texture.jukebox_side, Texture.jukebox_top, Texture.jukebox_side));

		this.put(Material.melon, this.createSkin(Texture.melon_side, Texture.melon_top));
		this.put(Material.carved_pumpkin, this.createSkinTopFront(Texture.pumpkin_side, Texture.pumpkin_top, Texture.carved_pumpkin, Orientation.NORTH));
		this.put(Material.jack_o_lantern, this.createSkinTopFront(Texture.pumpkin_side, Texture.pumpkin_top, Texture.jack_o_lantern, Orientation.NORTH));
		this.put(Material.pumpkin, this.createSkin(Texture.pumpkin_side, Texture.pumpkin_top));

		this.put(Material.dispenser, this.createSkinTopFront(Texture.furnace_side, Texture.furnace_top,
				Texture.dispenser_front, Orientation.NORTH));

		this.put(Material.cactus, this.createSkin(Texture.cactus_side, Texture.cactus_top));
		this.put(Material.crafting_table, this.createSkinTopFront(Texture.crafting_table_side,
				Texture.crafting_table_top, Texture.crafting_table_front, Orientation.NORTH));
		this.put(Material.furnace, this.createSkinTopFront(Texture.furnace_side, Texture.furnace_top,
				Texture.furnace_front_on, Orientation.NORTH));

		this.put(Material.end_portal_frame,
				this.createSkinTopBottom(Texture.end_portal_frame_side, Texture.end_portal_frame_top, Texture.end_stone));

		this.put(Material.redstone_lamp, Texture.redstone_lamp_on);
		this.put(Material.wall_torch, Texture.torch);
		this.put(Material.torch, Texture.torch);

		// temporary fixes
		this.put(Material.campfire, Texture.magma);
		this.put(Material.lantern, Texture.magma);
		this.put(Material.blast_furnace, Texture.furnace_front_on);

		// TODO: Create actioned block for side placement and extension of piston head
		this.put(Material.piston, this.createSkinTopBottom(Texture.piston_side, Texture.piston_top, Texture.piston_bottom));
		this.put(Material.sticky_piston, this.createSkinTopBottom(Texture.piston_side, Texture.piston_top_sticky, Texture.piston_bottom));
		// this.put(Material.torch, Texture.magma);

		// special
		this.skins.put(Blocks.get("sourcecraft:ramp"), PLAYER_CLIP);
		this.skins.put(Blocks.get("sourcecraft:ladder"), LADDER);
	}

	private void put(Texture block) {
		this.put(block, block);
	}

	private void put(Supplier<Block> block, Texture texture) {
		this.put(block, this.createSkin(texture.name()));
	}

	private void put(Supplier<Block> block, String texture) {
		this.put(block, this.createSkin(texture));
	}

	private void put(Supplier<Block> block, Skin skin) {
		this.skins.put(block, skin);
	}

	public Skin getSkin(Block block) {
		return this.skins.getFallBackToPrefix(block);
	}
}
