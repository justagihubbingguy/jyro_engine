package net.jyro.windows.gui.backends;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.RECT;

import java.util.Arrays;
import java.util.List;

public class PAINTSTRUCT extends Structure {
    public HDC hdc;
    public boolean fErase;
    public RECT rcPaint;
    public boolean fRestore;
    public boolean fIncUpdate;
    public byte[] rgbReserved = new byte[32];

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("hdc", "fErase", "rcPaint", "fRestore", "fIncUpdate", "rgbReserved");
    }
}
