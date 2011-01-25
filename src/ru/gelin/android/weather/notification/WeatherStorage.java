package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM_DEFAULT;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.CONDITION_TEXT;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.CURRENT_TEMP;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.HIGH_TEMP;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.HUMIDITY_TEXT;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.LOCATION;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.LOW_TEMP;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.TIME;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.UNIT_SYSTEM;
import static ru.gelin.android.weather.notification.WeatherStorageKeys.WIND_TEXT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.SimpleWeather;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 *  Stores and retrieves the weather objects to SharedPreferences.
 *  The exact classes of the retrieved Weather can differ from the classes
 *  of the saved weather.
 */
public class WeatherStorage {
    
    /** Preference name for weather (this fake preference is updated 
     *  to call preference change listeners) */
    static final String WEATHER = "weather";
    
    /** SharedPreferences to store weather. */
    SharedPreferences preferences;
    
    /**
     *  Creates the storage for context.
     */
    WeatherStorage(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    /**
     *  Saves the weather.
     */
    public void save(Weather weather) {
        Editor editor = preferences.edit();
        editor.putLong(WEATHER, System.currentTimeMillis());   //just current time
        editor.putString(LOCATION, weather.getLocation().getText());
        editor.putLong(TIME, weather.getTime().getTime());
        editor.putString(UNIT_SYSTEM, weather.getUnitSystem().toString());
        int i = 0;
        for (WeatherCondition condition : weather.getConditions()) {
            putOrRemove(editor, String.format(CONDITION_TEXT, i), 
                    condition.getConditionText());
            Temperature temp = condition.getTemperature();
            putOrRemove(editor, String.format(CURRENT_TEMP, i), 
                    temp.getCurrent());
            putOrRemove(editor, String.format(LOW_TEMP, i), 
                    temp.getLow());
            putOrRemove(editor, String.format(HIGH_TEMP, i), 
                    temp.getHigh());
            putOrRemove(editor, String.format(HUMIDITY_TEXT, i), 
                    condition.getHumidityText());
            putOrRemove(editor, String.format(WIND_TEXT, i), 
                    condition.getWindText());
            i++;
        }
        editor.commit();
    }
    
    /**
     *  Loads the weather.
     *  The values of the saved weather are restored, not exact classes.
     */
    public Weather load() {
        SimpleWeather weather = new ParcelableWeather();
        Location location = new SimpleLocation(
                preferences.getString(LOCATION, ""));
        weather.setLocation(location);
        weather.setTime(new Date(preferences.getLong(TIME, 0)));
        weather.setUnitSystem(UnitSystem.valueOf(
                preferences.getString(UNIT_SYSTEM, UNIT_SYSTEM_DEFAULT)));
        int i = 0;
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
        while (preferences.contains(String.format(CONDITION_TEXT, i))) {
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(preferences.getString(
                    String.format(CONDITION_TEXT, i), ""));
            SimpleTemperature temp = new SimpleTemperature(weather.getUnitSystem());
            temp.setCurrent(preferences.getInt(String.format(CURRENT_TEMP, i),
                    Temperature.UNKNOWN), weather.getUnitSystem());
            temp.setLow(preferences.getInt(String.format(LOW_TEMP, i),
                    Temperature.UNKNOWN), weather.getUnitSystem());
            temp.setHigh(preferences.getInt(String.format(HIGH_TEMP, i),
                    Temperature.UNKNOWN), weather.getUnitSystem());
            condition.setTemperature(temp);
            condition.setHumidityText(preferences.getString(
                    String.format(HUMIDITY_TEXT, i), ""));
            condition.setWindText(preferences.getString(
                    String.format(WIND_TEXT, i), ""));
            conditions.add(condition);
            i++;
        }
        weather.setConditions(conditions);
        return weather;
    }
    
    /**
     *  Updates just "weather" preference to signal that the weather update is performed,
     *  but nothing is changed.
     */
    public void updateTime() {
        Editor editor = preferences.edit();
        editor.putLong(WEATHER, System.currentTimeMillis());   //just current time
        editor.commit();
    }
    
    void putOrRemove(Editor editor, String key, String value) {
        if (value == null || "".equals(value)) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
    }
    
    void putOrRemove(Editor editor, String key, int temp) {
        if (temp == Temperature.UNKNOWN) {
            editor.remove(key);
        } else {
            editor.putInt(key, temp);
        }
    }

}
