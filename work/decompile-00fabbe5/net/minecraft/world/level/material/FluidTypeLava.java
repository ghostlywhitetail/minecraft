package net.minecraft.world.level.material;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.core.particles.Particles;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockFireAbstract;
import net.minecraft.world.level.block.BlockFluids;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;

public abstract class FluidTypeLava extends FluidTypeFlowing {

    public static final float MIN_LEVEL_CUTOFF = 0.44444445F;

    public FluidTypeLava() {}

    @Override
    public FluidType d() {
        return FluidTypes.FLOWING_LAVA;
    }

    @Override
    public FluidType e() {
        return FluidTypes.LAVA;
    }

    @Override
    public Item a() {
        return Items.LAVA_BUCKET;
    }

    @Override
    public void a(World world, BlockPosition blockposition, Fluid fluid, Random random) {
        BlockPosition blockposition1 = blockposition.up();

        if (world.getType(blockposition1).isAir() && !world.getType(blockposition1).i(world, blockposition1)) {
            if (random.nextInt(100) == 0) {
                double d0 = (double) blockposition.getX() + random.nextDouble();
                double d1 = (double) blockposition.getY() + 1.0D;
                double d2 = (double) blockposition.getZ() + random.nextDouble();

                world.addParticle(Particles.LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                world.a(d0, d1, d2, SoundEffects.LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                world.a((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), SoundEffects.LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

    }

    @Override
    public void b(World world, BlockPosition blockposition, Fluid fluid, Random random) {
        if (world.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            int i = random.nextInt(3);

            if (i > 0) {
                BlockPosition blockposition1 = blockposition;

                for (int j = 0; j < i; ++j) {
                    blockposition1 = blockposition1.c(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    if (!world.o(blockposition1)) {
                        return;
                    }

                    IBlockData iblockdata = world.getType(blockposition1);

                    if (iblockdata.isAir()) {
                        if (this.a((IWorldReader) world, blockposition1)) {
                            world.setTypeUpdate(blockposition1, BlockFireAbstract.a((IBlockAccess) world, blockposition1));
                            return;
                        }
                    } else if (iblockdata.getMaterial().isSolid()) {
                        return;
                    }
                }
            } else {
                for (int k = 0; k < 3; ++k) {
                    BlockPosition blockposition2 = blockposition.c(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);

                    if (!world.o(blockposition2)) {
                        return;
                    }

                    if (world.isEmpty(blockposition2.up()) && this.b(world, blockposition2)) {
                        world.setTypeUpdate(blockposition2.up(), BlockFireAbstract.a((IBlockAccess) world, blockposition2));
                    }
                }
            }

        }
    }

    private boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (this.b(iworldreader, blockposition.shift(enumdirection))) {
                return true;
            }
        }

        return false;
    }

    private boolean b(IWorldReader iworldreader, BlockPosition blockposition) {
        return blockposition.getY() >= iworldreader.getMinBuildHeight() && blockposition.getY() < iworldreader.getMaxBuildHeight() && !iworldreader.isLoaded(blockposition) ? false : iworldreader.getType(blockposition).getMaterial().isBurnable();
    }

    @Nullable
    @Override
    public ParticleParam i() {
        return Particles.DRIPPING_LAVA;
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        this.a(generatoraccess, blockposition);
    }

    @Override
    public int b(IWorldReader iworldreader) {
        return iworldreader.getDimensionManager().isNether() ? 4 : 2;
    }

    @Override
    public IBlockData b(Fluid fluid) {
        return (IBlockData) Blocks.LAVA.getBlockData().set(BlockFluids.LEVEL, e(fluid));
    }

    @Override
    public boolean a(FluidType fluidtype) {
        return fluidtype == FluidTypes.LAVA || fluidtype == FluidTypes.FLOWING_LAVA;
    }

    @Override
    public int c(IWorldReader iworldreader) {
        return iworldreader.getDimensionManager().isNether() ? 1 : 2;
    }

    @Override
    public boolean a(Fluid fluid, IBlockAccess iblockaccess, BlockPosition blockposition, FluidType fluidtype, EnumDirection enumdirection) {
        return fluid.getHeight(iblockaccess, blockposition) >= 0.44444445F && fluidtype.a((Tag) TagsFluid.WATER);
    }

    @Override
    public int a(IWorldReader iworldreader) {
        return iworldreader.getDimensionManager().isNether() ? 10 : 30;
    }

    @Override
    public int a(World world, BlockPosition blockposition, Fluid fluid, Fluid fluid1) {
        int i = this.a((IWorldReader) world);

        if (!fluid.isEmpty() && !fluid1.isEmpty() && !(Boolean) fluid.get(FluidTypeLava.FALLING) && !(Boolean) fluid1.get(FluidTypeLava.FALLING) && fluid1.getHeight(world, blockposition) > fluid.getHeight(world, blockposition) && world.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    private void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.triggerEffect(1501, blockposition, 0);
    }

    @Override
    protected boolean f() {
        return false;
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection, Fluid fluid) {
        if (enumdirection == EnumDirection.DOWN) {
            Fluid fluid1 = generatoraccess.getFluid(blockposition);

            if (this.a((Tag) TagsFluid.LAVA) && fluid1.a((Tag) TagsFluid.WATER)) {
                if (iblockdata.getBlock() instanceof BlockFluids) {
                    generatoraccess.setTypeAndData(blockposition, Blocks.STONE.getBlockData(), 3);
                }

                this.a(generatoraccess, blockposition);
                return;
            }
        }

        super.a(generatoraccess, blockposition, iblockdata, enumdirection, fluid);
    }

    @Override
    protected boolean j() {
        return true;
    }

    @Override
    protected float c() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEffect> k() {
        return Optional.of(SoundEffects.BUCKET_FILL_LAVA);
    }

    public static class a extends FluidTypeLava {

        public a() {}

        @Override
        protected void a(BlockStateList.a<FluidType, Fluid> blockstatelist_a) {
            super.a(blockstatelist_a);
            blockstatelist_a.a(FluidTypeLava.a.LEVEL);
        }

        @Override
        public int d(Fluid fluid) {
            return (Integer) fluid.get(FluidTypeLava.a.LEVEL);
        }

        @Override
        public boolean c(Fluid fluid) {
            return false;
        }
    }

    public static class b extends FluidTypeLava {

        public b() {}

        @Override
        public int d(Fluid fluid) {
            return 8;
        }

        @Override
        public boolean c(Fluid fluid) {
            return true;
        }
    }
}
