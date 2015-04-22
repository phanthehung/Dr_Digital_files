package com.khoisang.khoisanglibary.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class NetwordUtil {
	public static void disableConnectionReuseIfNecessary() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static String getIPAddress(boolean useIPv4) throws SocketException {
		List<NetworkInterface> interfaces = Collections.list(NetworkInterface
				.getNetworkInterfaces());
		for (NetworkInterface intf : interfaces) {
			List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
			for (InetAddress addr : addrs) {
				if (addr.isLoopbackAddress() == false) {
					String sAddr = addr.getHostAddress().toUpperCase(
							Locale.getDefault());
					boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
					if (useIPv4) {
						if (isIPv4)
							return sAddr;
					} else {
						if (!isIPv4) {
							int delim = sAddr.indexOf('%'); // drop ip6 port
															// suffix
							return delim < 0 ? sAddr : sAddr
									.substring(0, delim);
						}
					}
				} // End if address is loopback
			} // End if address
		} // End for
		return null;
	}
}
