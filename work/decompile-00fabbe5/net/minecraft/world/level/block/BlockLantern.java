package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.material.EnumPistonReaction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidTypes;
import net.minecraft.world.level.pathfinder.PathMode;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import net.minecraft.world.phys.shapes.VoxelShapes;

public class BlockLantern extends Block implements IBlockWaterlogged {

    public static final BlockStateBoolean HANGING = BlockProperties.HANGING;
    public static final BlockStateBoolean WATERLOGGED = BlockProperties.WATERLOGGED;
    protected static final VoxelShape AABB = VoxelShapes.a(Block.a(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D), Block.a(6.0D, 7.0D, 6.0D, 10.0D, 9.0D, 10.0D));
    protected static final VoxelShape HANGING_AABB = VoxelShapes.a(Block.a(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.a(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D));

    public BlockLantern(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.k((IBlockData) ((IBlockData) ((IBlockData) this.stateDefinition.getBlockData()).set(BlockLantern.HANGING, false)).set(BlockLantern.WATERLOGGED, false));
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        Fluid fluid = blockactioncontext.getWorld().getFluid(blockactioncontext.getClickPosition());
        EnumDirection[] aenumdirection = blockactioncontext.f();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
                IBlockData iblockdata = (IBlockData) this.getBlockData().set(BlockLantern.HANGING, enumdirection == EnumDirection.UP);

                if (iblockdata.canPlace(blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) {
                    return (IBlockData) iblockdata.set(BlockLantern.WATERLOGGED, fluid.getType() == FluidTypes.WATER);
                }
            }
        }

        return null;
    }

    @Override
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (Boolean) iblockdata.get(BlockLantern.HANGING) ? BlockLantern.HANGING_AABB : BlockLantern.AABB;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockLantern.HANGING, BlockLantern.WATERLOGGED);
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        EnumDirection enumdirection = h(iblockdata).opposite();

        return Block.a(iworldreader, blockposition.shift(enumdirection), enumdirection.opposite());
    }

    protected static EnumDirection h(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockLantern.HANGING) ? EnumDirection.DOWN : EnumDirection.UP;
    }

    @Override
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return EnumPistonReaction.DESTROY;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if ((Boolean) iblockdata.get(BlockLantern.WATERLOGGED)) {
            generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) generatoraccess));
        }

        return h(iblockdata).opposite() == enumdirection && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public Fluid c_(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockLantern.WATERLOGGED) ? FluidTypes.WATER.a(false) : super.c_(iblockdata);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
