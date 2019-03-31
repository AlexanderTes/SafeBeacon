package com.example.safebeacon;

 interface userInterface extends googleMap{

         User getUser();
         Report getReport();
         void openReportInterface();
         void openBluetoothInterface();
}
