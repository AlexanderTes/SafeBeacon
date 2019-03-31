package com.example.safebeacon;

public interface bluetoothDetection extends Hardware {
    String BTaddress = "";

    String getAddress();
    void setAddress(String address);
}
