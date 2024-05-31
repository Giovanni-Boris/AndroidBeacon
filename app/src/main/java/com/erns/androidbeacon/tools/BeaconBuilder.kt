package com.erns.androidbeacon.tools

import android.bluetooth.le.AdvertiseData
import android.os.ParcelUuid
import java.nio.ByteBuffer

class BeaconBuilder {
    companion object {
        fun createBeaconData(ServiceUUid: ParcelUuid, uuidId: String): AdvertiseData {
            val manufacturerData = ByteBuffer.allocate(21)
            val uuid: ByteArray = BleTools.getIdAsByte(uuidId.replace("-", ""))

            manufacturerData.put(0, 0x02.toByte()) // Beacon Identifier
            manufacturerData.put(1, 0x13.toByte()) // Beacon Identifier
            for (i in 2..17) {
                manufacturerData.put(i, uuid[i - 2]) // adding the UUID
            }
            manufacturerData.put(18, 0x00.toByte()) // first byte of Major
            manufacturerData.put(19, 0x05.toByte()) // second byte of Major
            manufacturerData.put(20, 0x00.toByte()) // first minor
           // manufacturerData.put(21, 0x58.toByte()) // second minor
            //manufacturerData.put(22, 0x76.toByte()) // txPower

            return AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addManufacturerData(76, manufacturerData.array())
                //.addServiceUuid(ServiceUUid)
                .build()
        }
    }
}