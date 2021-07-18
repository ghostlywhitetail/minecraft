package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.IWorldWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.levelgen.feature.configurations.WorldGenFeatureDeltaConfiguration;

public class WorldGenFeatureDelta extends WorldGenerator<WorldGenFeatureDeltaConfiguration> {

    private static final ImmutableList<Block> CANNOT_REPLACE = ImmutableList.of(Blocks.BEDROCK, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
    private static final EnumDirection[] DIRECTIONS = EnumDirection.values();
    private static final double RIM_SPAWN_CHANCE = 0.9D;

    public WorldGenFeatureDelta(Codec<WorldGenFeatureDeltaConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeaturePlaceContext<WorldGenFeatureDeltaConfiguration> featureplacecontext) {
        boolean flag = false;
        Random random = featureplacecontext.c();
        GeneratorAccessSeed generatoraccessseed = featureplacecontext.a();
        WorldGenFeatureDeltaConfiguration worldgenfeaturedeltaconfiguration = (WorldGenFeatureDeltaConfiguration) featureplacecontext.e();
        BlockPosition blockposition = featureplacecontext.d();
        boolean flag1 = random.nextDouble() < 0.9D;
        int i = flag1 ? worldgenfeaturedeltaconfiguration.e().a(random) : 0;
        int j = flag1 ? worldgenfeaturedeltaconfiguration.e().a(random) : 0;
        boolean flag2 = flag1 && i != 0 && j != 0;
        int k = worldgenfeaturedeltaconfiguration.d().a(random);
        int l = worldgenfeaturedeltaconfiguration.d().a(random);
        int i1 = Math.max(k, l);
        Iterator iterator = BlockPosition.a(blockposition, k, 0, l).iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition1 = (BlockPosition) iterator.next();

            if (blockposition1.k(blockposition) > i1) {
                break;
            }

            if (a((GeneratorAccess) generatoraccessseed, blockposition1, worldgenfeaturedeltaconfiguration)) {
                if (flag2) {
                    flag = true;
                    this.a((IWorldWriter) generatoraccessseed, blockposition1, worldgenfeaturedeltaconfiguration.c());
                }

                BlockPosition blockposition2 = blockposition1.c(i, 0, j);

                if (a((GeneratorAccess) generatoraccessseed, blockposition2, worldgenfeaturedeltaconfiguration)) {
                    flag = true;
                    this.a((IWorldWriter) generatoraccessseed, blockposition2, worldgenfeaturedeltaconfiguration.b());
                }
            }
        }

        return flag;
    }

    private static boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition, WorldGenFeatureDeltaConfiguration worldgenfeaturedeltaconfiguration) {
        IBlockData iblockdata = generatoraccess.getType(blockposition);

        if (iblockdata.a(worldgenfeaturedeltaconfiguration.b().getBlock())) {
            return false;
        } else if (WorldGenFeatureDelta.CANNOT_REPLACE.contains(iblockdata.getBlock())) {
            return false;
        } else {
            EnumDirection[] aenumdirection = WorldGenFeatureDelta.DIRECTIONS;
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];
                boolean flag = generatoraccess.getType(blockposition.shift(enumdirection)).isAir();

                if (flag && enumdirection != EnumDirection.UP || !flag && enumdirection == EnumDirection.UP) {
                    return false;
                }
            }

            return true;
        }
    }
}
