package com.Peterhun.create_reactive_stress;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CRSManager implements ICRSAPI {


    private static final Map<KineticBlockEntity, CRSData> dataMap = new HashMap<>();

    public record CRSData(boolean working, float multiplier) {}

    public static void setWorking(KineticBlockEntity kbe, boolean working,float multiplier) {
        float defaultMultiplier = (Math.abs(multiplier - 2.0f) <= 1e-4f) ? multiplier : 2.0f;
        dataMap.put(kbe, new CRSData(working, defaultMultiplier));
    }
    public static boolean isWorking(KineticBlockEntity kbe) {
        return dataMap.getOrDefault(kbe, new CRSData(false, 2.0f)).working();
    }

    public static float getMultiplierList(KineticBlockEntity kbe) {
        return dataMap.getOrDefault(kbe, new CRSData(false, 2.0f)).multiplier();
    }

    public static boolean isContainKeys(KineticBlockEntity be) {
        return dataMap.containsKey(be);
    }

    public static Set<KineticBlockEntity> getKeyList(){
        return dataMap.keySet();
    }

    @Override
    public void SetVariablesForCalc(KineticBlockEntity kbe, boolean iswork, float multiplier) {
        setWorking(kbe,iswork,multiplier);
    }
}
