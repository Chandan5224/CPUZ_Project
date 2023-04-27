package com.example.cpu_zproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cpu_zproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DeviceInfo div = new DeviceInfo(getApplicationContext());
        binding.manufacturer.setText(String.format("Manufacturer : %s", div.getManufacturer()));
        binding.modelName.setText(String.format("Model Name : %s", div.getModelName()));
        binding.modelNumber.setText(String.format("Model Number : %s", div.getModelNumber()));
        binding.ram.setText(String.format("Ram : %s GB", div.getTotalRAM()));
        binding.storage.setText(String.format("Storage : %s GB", div.getAvailableStorage()));
        binding.battery.setText(String.format("Battery : %s %%", div.getBatteryLevel()));
        binding.androidVersion.setText(String.format("Android Version : %s", div.getAndroidVersion()));
//        binding.cameraMegaPixel.setText(String.format("Camera Mega Pixel : %s", div.getCameraMegaPixels()));
//        binding.cameraAperture.setText(String.format("Camera Aperture : %s", div.camera(2)));
        binding.processorInfo.setText(String.format("Processor Info : %s", div.getProcessorInfo()));
        binding.gpuInfo.setText(String.format("GPU : %s", div.getGPUInfo()));
//        binding.imeiInfo.setText(String.format("Processor Info : %s", div.getIMEI()));

    }


}