package com.skcraft.launcher.report;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.skcraft.launcher.Configuration;
import com.skcraft.launcher.Instance;
import com.skcraft.launcher.InstanceList;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.util.HttpRequest;
import com.skcraft.launcher.util.PasteUtils;
import lombok.extern.java.Log;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.List;

@Log
public class Report {

    public static void reportHW(String url, String identification, Configuration configuration, InstanceList i){
        try {
            HttpRequest.Form f = HttpRequest.Form.form();
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();

            f.add("TYPE","HW");
            f.add("IDENTIFY",identification);
            f.add("TOKEN",configuration.getAdminToken());
            f.add("CPU_NAME",hal.getProcessor().getProcessorIdentifier().getName());
            f.add("CPU_F_CORES",hal.getProcessor().getPhysicalProcessorCount()+"");
            f.add("CPU_L_CORES",hal.getProcessor().getLogicalProcessorCount()+"");
            f.add("MEM_TOTAL_M",hal.getMemory().getTotal()/1024/1024+"");
            f.add("JAVA_VERSION", ManagementFactory.getRuntimeMXBean().getSpecVersion());
            f.add("JAVA_VENDOR", ManagementFactory.getRuntimeMXBean().getSpecVendor());
            f.add("JAVA_NAME", ManagementFactory.getRuntimeMXBean().getSpecName());


            HttpRequest.post(new URL(url))
                    .bodyForm(f)
                    .execute()
                    .expectResponseCode(200);

        }catch (Exception e){
            log.info("some failed"+e.getStackTrace());
        }
    }

    public static void reportCR(String url, String identification,Configuration configuration, InstanceList i){
        try {
            HttpRequest.Form f2 = HttpRequest.Form.form();
            f2.add("TYPE","CR");
            f2.add("IDENTIFY",identification);
            f2.add("TOKEN",configuration.getAdminToken());
            int crid=0;
            List<Instance> il = i.getInstances();
            for (Instance instance:il ) {
                File folder =instance.getContentDir();
                File crashReports=new File(folder.getPath()+"/crash-reports");
                if(crashReports.exists()){
                    log.info("Crash report folder found: "+crashReports.getPath());
                    File[] reports = crashReports.listFiles();
                    for (File report:reports) {
                        if(report.length()<100000) {
                            String content = Files.toString(report, Charsets.UTF_8);
                            String out = PasteUtils.paste(content);
                            if(out!=null){
                                log.info("Crash uploaded to "+out+" and send to server database");
                                f2.add("CrashReport_"+crid+";"+ instance.getName(),out);
                                crid++;
                                report.delete();
                            }
                        }else {
                            report.delete();
                        }
                    }
                }
                if(crid>0){
                    HttpRequest.post(new URL(url))
                            .bodyForm(f2)
                            .execute()
                            .expectResponseCode(200);
                }
            }

        }catch (Exception e){
            log.info("some failed"+e.getStackTrace());
        }
    }
}
