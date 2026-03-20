package com.Peterhun.create_reactive_stress;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;


public interface ICRSAPI {
    //it is used to define KineticBlockEntity and send a value whenever the said KineticBlockEntity is working/crafting/busy
    //set the default value used for the multiplication e.g.: baseStress * multiplier = max used stress
    //if no value set it will default to 2.0f
    void SetVariablesForCalc(KineticBlockEntity kbe, boolean iswork, float multiplier);
}
