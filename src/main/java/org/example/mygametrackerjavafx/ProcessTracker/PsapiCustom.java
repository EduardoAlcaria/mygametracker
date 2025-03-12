package org.example.mygametrackerjavafx.ProcessTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface PsapiCustom extends com.sun.jna.Library {
    PsapiCustom INSTANCE = Native.load("Psapi", PsapiCustom.class, W32APIOptions.UNICODE_OPTIONS);

    int GetModuleBaseNameW(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, char[] lpBaseName, int size);

    boolean EnumProcesses(int[] pProcessIds, int cb, WinDef.DWORDByReference pBytesReturned);
}
