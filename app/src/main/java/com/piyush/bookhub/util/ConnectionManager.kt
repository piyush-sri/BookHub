package com.piyush.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager { // contains information about the connectivity status of the device
    fun checkConnectivity(context: Context): Boolean{
     val  connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager    // this will give us information about the currently active status
       val activeNetwork : NetworkInfo? = connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        }
        else{
            return false
        }

    }
}