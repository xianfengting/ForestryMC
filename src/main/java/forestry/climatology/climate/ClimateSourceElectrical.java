/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.climatology.climate;

import javax.annotation.Nullable;

import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateState;
import forestry.api.core.IErrorLogic;
import forestry.climatology.tiles.TileElectricalClimatiser;
import forestry.core.climate.ClimateStateHelper;
import forestry.core.errors.EnumErrorCode;
import forestry.energy.EnergyManager;

public class ClimateSourceElectrical extends ClimateSource<TileElectricalClimatiser> {

	private static final int ENERGY_PER_OPERATION = 75;

	public ClimateSourceElectrical(TileElectricalClimatiser proxy, ClimateSourceType type, float change, float boundModifier) {
		super(proxy, change, boundModifier, type);
		if (type.canChangeTemperature()) {
			setTemperatureMode(change > 0 ? ClimateSourceMode.POSITIVE : ClimateSourceMode.NEGATIVE);
		} else {
			setHumidityMode(change > 0 ? ClimateSourceMode.POSITIVE : ClimateSourceMode.NEGATIVE);
		}
	}

	@Override
	protected void isNotValid() {
		proxy.setActive(false);
	}

	@Override
	public boolean canWork(IClimateState currentState, @Nullable ClimateSourceType oppositeType, float resourceModifier) {
		IErrorLogic errorLogic = proxy.getErrorLogic();
		EnergyManager energyManager = proxy.getEnergyManager();

		if (energyManager.extractEnergy(Math.round(ENERGY_PER_OPERATION * getEnergyModifier(currentState, oppositeType) * resourceModifier), true) > 0) {
			proxy.setActive(true);
			errorLogic.setCondition(false, EnumErrorCode.NO_POWER);
			return true;
		}
		proxy.setActive(false);
		errorLogic.setCondition(true, EnumErrorCode.NO_POWER);
		return false;
	}

	@Override
	protected void removeResources(IClimateState currentState, @Nullable ClimateSourceType oppositeType, float resourceModifier) {
		EnergyManager energyManager = proxy.getEnergyManager();

		energyManager.extractEnergy(Math.round(ENERGY_PER_OPERATION * getEnergyModifier(currentState, oppositeType) * resourceModifier), false);
	}

	@Override
	protected IClimateState getChange(ClimateSourceType type, IClimateState target, IClimateState currentState) {
		float temperature = 0.0F;
		float humidity = 0.0F;
		if (type.canChangeHumidity()) {
			humidity += getChange(ClimateType.HUMIDITY);
		}
		if (type.canChangeTemperature()) {
			temperature += getChange(ClimateType.TEMPERATURE);
		}
		return ClimateStateHelper.mutableOf(temperature, humidity);
	}

}
