package com.erns.androidbeacon

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import com.erns.androidbeacon.tools.AdvertiserSettingsHelper
import com.erns.androidbeacon.tools.BeaconBuilder


class Transmitter(private val context: Context) {
    private val TAG = "Transmitter"
    fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "ACCESS_FINE_LOCATION permission denied!")
            return false
        }
        return true
    }
    fun startAdvertiser(formatUuid: String) {
        val Service_UUID = ParcelUuid.fromString(formatUuid)
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        var adapter = bluetoothManager.adapter

        if (!checkLocationPermission()) {
            Log.e(TAG, "BLUETOOTH_CONNECT denied!")
            return
        }

        Log.e(TAG, " adapter.leMaximumAdvertisingDataLength "+ adapter.leMaximumAdvertisingDataLength)

        adapter.name="EEL"

        val advertiser = adapter.bluetoothLeAdvertiser

        /*if (!adapter.isLe2MPhySupported) {
            Log.e(TAG, "2M PHY not supported!")
            return
        }

        if (!adapter.isLeExtendedAdvertisingSupported) {
            Log.e(TAG, "LE Extended Advertising not supported!")
            return
        }*/
        val beaconData = BeaconBuilder.createBeaconData(Service_UUID, formatUuid)

        val settingsHelper = AdvertiserSettingsHelper()
        val settings = settingsHelper.createSettings(connectable = false, timeoutMillis = 0)

        if (advertiser != null) {
            advertiser.stopAdvertising(callbackClose)
            advertiser.startAdvertising(settings, beaconData , callback)
        } else {
            Log.d(TAG, "advertiser is null")
        }


    }


    private val callback = object : AdvertiseCallback() {

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "Advertising successfully started")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)

            Log.d(TAG, "Advertising failed, errorCode: $errorCode")

            when (errorCode) {
                ADVERTISE_FAILED_ALREADY_STARTED -> Log.d(TAG, "ADVERTISE_FAILED_ALREADY_STARTED")
                ADVERTISE_FAILED_DATA_TOO_LARGE -> Log.d(TAG, "ADVERTISE_FAILED_DATA_TOO_LARGE")
                ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> Log.d(
                    TAG,
                    "ADVERTISE_FAILED_FEATURE_UNSUPPORTED"
                )

                ADVERTISE_FAILED_INTERNAL_ERROR -> Log.d(TAG, "ADVERTISE_FAILED_INTERNAL_ERROR")
                ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> Log.d(
                    TAG,
                    "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS"
                )

                else -> Log.d(TAG, "Unhandled error: $errorCode")
            }
            //                sendFailureIntent(errorCode);
//                stopSelf();
        }
    }

    private val callbackClose = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d(TAG, "Advertising successfully close")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)

            Log.d(TAG, "Advertising failed, errorCode: $errorCode")
        }
    }

}

