package com.erns.androidbeacon.tools

import android.bluetooth.le.AdvertiseSettings

class AdvertiserSettingsHelper {
    fun createSettings(connectable: Boolean, timeoutMillis: Int): AdvertiseSettings {
        return AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
            .setConnectable(connectable)
            .setTimeout(timeoutMillis)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .build()
    }
}