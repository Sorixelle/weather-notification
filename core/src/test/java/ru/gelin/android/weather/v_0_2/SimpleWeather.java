/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.v_0_2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Simple weather realization. Just holds the values.
 */
public class SimpleWeather implements Weather {

    /** Location */
    Location location;
    /** Time */
    Date time;
    /** Unit system */
    UnitSystem unit;
    /** List of conditions */
    List<WeatherCondition> conditions;

    /**
     *  Sets the location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *  Sets the time.
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     *  Sets the unit system.
     */
    public void setUnitSystem(UnitSystem unit) {
        this.unit = unit;
    }

    /**
     *  Sets the weather conditions list.
     */
    public void setConditions(List<WeatherCondition> conditions) {
        this.conditions = conditions;
    }

    //@Override
    public Location getLocation() {
        return this.location;
    }

    //@Override
    public Date getTime() {
        return this.time;
    }

    //@Override
    public UnitSystem getUnitSystem() {
        return this.unit;
    }

    //@Override
    public List<WeatherCondition> getConditions() {
        if (this.conditions == null) {
            return new ArrayList<>();
        }
        return this.conditions;
    }

    //@Override
    public boolean isEmpty() {
        if (this.time == null || this.time.getTime() == 0) {
            return true;
        }
        if (this.conditions == null || this.conditions.size() == 0) {
            return true;
        }
        return false;
    }

}
