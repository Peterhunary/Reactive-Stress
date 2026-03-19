package com.Peterhun.create_reactive_stress.mixin;

import com.Peterhun.create_reactive_stress.UtilityHelperClass;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;


import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.createmod.catnip.data.Iterate;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin {

    @Shadow protected float lastStressApplied;

    @Shadow
    public abstract KineticNetwork getOrCreateNetwork();

    /**
     * @author Peterhun
     * @reason new math
     */

    @Overwrite
    public float calculateStressApplied() {
        KineticBlockEntity self = (KineticBlockEntity)(Object)this;
        float baseImpact = (float) BlockStressValues.getImpact(self.getBlockState().getBlock());

        return (float) (baseImpact * createReactiveStress$GetConfig(self));
    }



    @Unique
    private static double createReactiveStress$GetConfig(KineticBlockEntity be) {

        if (be instanceof MechanicalPressBlockEntity press &&
                press.pressingBehaviour.running ) {
            return UtilityHelperClass.createReactiveStress$valuePress;
        }

        if (be instanceof MechanicalMixerBlockEntity mixer &&
                mixer.running) {
            return UtilityHelperClass.createReactiveStress$valueMixer;
        }

        if (be instanceof MillstoneBlockEntity mill && !mill.inputInv.getStackInSlot(0).isEmpty()) {
            return UtilityHelperClass.createReactiveStress$valueMillstone;
        }

        if (be instanceof SawBlockEntity saw && !saw.inventory.getStackInSlot(0).isEmpty() ) {
            return UtilityHelperClass.createReactiveStress$valueSaw;
        }

        if (be instanceof CrushingWheelBlockEntity wheel && createReactiveStress$containsEntityAnyController(wheel)){
            return  UtilityHelperClass.createReactiveStress$valueCrushingWheel;
        }
        return 1.0;
    }

    @Unique
    private int createReactiveStress$reactiveStressTickCounter = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        KineticBlockEntity self = (KineticBlockEntity)(Object)this;

        // Only run on server side
        assert self.getLevel() != null;
        if (self.getLevel().isClientSide()) return;
        if (overStressed) return;

        createReactiveStress$reactiveStressTickCounter++;

        if (createReactiveStress$reactiveStressTickCounter < 20) return;
        createReactiveStress$reactiveStressTickCounter = 0;


        if (getOrCreateNetwork() != null) {
            float impact = calculateStressApplied();

            if (Math.abs(impact - lastStressApplied) > 1e-6f) {
                lastStressApplied = impact;
                getOrCreateNetwork().updateStressFor(self, impact);
                self.setChanged();
            }
        }
    }



    @Shadow
    protected boolean overStressed;

    @Unique
    private static boolean createReactiveStress$containsEntityAnyController(CrushingWheelBlockEntity be) {
    var level = be.getLevel();
        if (level == null) return false;
        var pos = be.getBlockPos();
        for (var dir : Iterate.directions) {
            var relPos = pos.relative(dir);
            var state = level.getBlockState(relPos);
            if (state.hasProperty(BlockStateProperties.AXIS) && state.getValue(BlockStateProperties.AXIS) == dir.getAxis()) continue;
            if (level.getBlockEntity(relPos) instanceof CrushingWheelControllerBlockEntity ctrl && ctrl.isOccupied()) return true;
        }
    return false;
    }
}

