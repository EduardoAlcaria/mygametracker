package org.example.mygametrackerjavafx.ProcessTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;

public class ProcessTracker {
    public void Check() {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        PsapiCustom psapi = PsapiCustom.INSTANCE;

        int maxProcesses = 1024;
        int[] processIds = new int[maxProcesses];
        WinDef.DWORDByReference bytesReturned = new WinDef.DWORDByReference();

        if (psapi.EnumProcesses(processIds, maxProcesses * 4, bytesReturned)) {
            int count = bytesReturned.getValue().intValue() / 4;
            for (int i = 0; i < count; i++) {
                int pid = processIds[i];
                WinNT.HANDLE processHandle = kernel32.OpenProcess(
                        WinNT.PROCESS_QUERY_INFORMATION | WinNT.PROCESS_VM_READ, false, pid);

                if (processHandle != null && !WinNT.INVALID_HANDLE_VALUE.equals(processHandle)) {
                    char[] name = new char[512];
                    if (psapi.GetModuleBaseNameW(processHandle, null, name, name.length) > 0) {
                        System.out.println("PID: " + pid + " - Name: " + Native.toString(name));
                    }
                    kernel32.CloseHandle(processHandle);
                }
            }
        } else {
            System.out.println("Failed to enumerate processes.");
        }
    }
}
