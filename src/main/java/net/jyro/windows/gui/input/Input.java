package net.jyro.windows.gui.input;

public abstract class Input {

    public static class KeyManager {
        private final boolean[] keys = new boolean[256];

        public void handleKeyDown(int vkCode) {
            if(vkCode >= 0 && vkCode < keys.length) keys[vkCode] = true;
        }

        public void handleKeyUp(int vkCode) {
            if(vkCode >= 0 && vkCode < keys.length) keys[vkCode] = false;
        }

        public boolean isKeyDown(int vkCode) {
            if(vkCode < 0 || vkCode >= keys.length) return false;
            return keys[vkCode];
        }
    }
}
