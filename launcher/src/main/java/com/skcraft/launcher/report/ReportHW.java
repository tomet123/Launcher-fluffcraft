package com.skcraft.launcher.report;

import com.skcraft.launcher.util.HttpRequest;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.net.URL;

public class ReportHW {

    public static void report(){
        try {
            HttpRequest.Form f = HttpRequest.Form.form();
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();

            f.add("CPU_NAME",hal.getProcessor().getProcessorIdentifier().getName());
            f.add("CPU_F_CORES",hal.getProcessor().getPhysicalProcessorCount()+"");
            f.add("CPU_L_CORES",hal.getProcessor().getLogicalProcessorCount()+"");
            f.add("MEM_TOTAL_M",hal.getMemory().getTotal()/1024/1024+"");

            HttpRequest.post(new URL("https://minecore.cz/launcher/report.php"))
                    .bodyForm(f)
                    .execute()
                    .expectResponseCode(200);
        }catch (Exception e){

        }
    }
}