package cc.mashroom.hedgehog.util;

import  android.Manifest;
import  android.content.Context;
import  android.content.pm.PackageManager;
import  android.location.Location;
import  android.location.LocationManager;
import  android.net.ConnectivityManager;
import  android.net.NetworkInfo;

import  java.net.NetworkInterface;
import  java.net.SocketException;
import  java.util.Enumeration;
import  java.util.LinkedList;
import  java.util.List;

import  cc.mashroom.util.ObjectUtils;
import  cc.mashroom.util.StringUtils;

public  class  NetworkUtils
{
    public  static  String  getMac()
    {
        try
        {
            for( Enumeration<NetworkInterface>  networkInterfaces = NetworkInterface.getNetworkInterfaces();networkInterfaces.hasMoreElements(); )
            {
                NetworkInterface  networkInterface = networkInterfaces.nextElement();

                if( networkInterface != null && networkInterface.getHardwareAddress() != null )
                {
                    List<String>  macBytes = new LinkedList<String>();

                    for( byte  macByte : networkInterface.getHardwareAddress() )
                    {
                        macBytes.add( String.format("%02X",macByte) );
                    }

                    return  StringUtils.join( macBytes,":" );
                }
            }
        }
        catch(  SocketException  e )
        {
            e.printStackTrace();
        }

        return   null;
    }

    public  static  Location  getLocation( Context  context )
    {
        if( context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            LocationManager  locationManager = ObjectUtils.cast( context.getSystemService(Context.LOCATION_SERVICE) );

            return  locationManager.getLastKnownLocation( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER );
        }

        return   null;
    }

    public  static  boolean  isNetworkAvailable(    Context  context )
    {
        if( context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED )
        {
            NetworkInfo  networkInfo = ObjectUtils.cast(context.getSystemService(Context.CONNECTIVITY_SERVICE),ConnectivityManager.class).getActiveNetworkInfo();

            return  networkInfo != null  && networkInfo.isConnected();
        }

        return  false;
    }
}
