package com.mxterminal.console;

import java.util.Hashtable;

// !!! OUCH KLUDGE

public abstract class ConsoleDefProps {

    static public final int PROP_NAME    = 0;
    static public final int PROP_VALUE   = 1;
    static public Hashtable  oldPropNames      = new Hashtable();
    static {
        oldPropNames.put("rv", "rev-video");
        oldPropNames.put("aw", "autowrap");
        oldPropNames.put("rw", "rev-autowrap");
        oldPropNames.put("im", "insert-mode");
        oldPropNames.put("al", "auto-linefeed");
        oldPropNames.put("sk", "repos-input");
        oldPropNames.put("si", "repos-output");
        oldPropNames.put("vi", "visible-cursor");
        oldPropNames.put("le", "local-echo");
        oldPropNames.put("vb", "visual-bell");
        oldPropNames.put("ct", "map-ctrl-space");
        oldPropNames.put("dc", "80x132-toggle");
        oldPropNames.put("da", "80x132-enable");
        oldPropNames.put("lp", "local-pgkeys");
        oldPropNames.put("sc", "copy-crnl");
        oldPropNames.put("ad", "ascii-line");
        oldPropNames.put("cs", "copy-select");
        oldPropNames.put("fn", "font-name");
        oldPropNames.put("fs", "font-size");
        oldPropNames.put("gm", "geometry");
        oldPropNames.put("te", "term-type");
        oldPropNames.put("sl", "save-lines");
        oldPropNames.put("sb", "scrollbar");
        oldPropNames.put("bg", "bg-color");
        oldPropNames.put("fg", "fg-color");
        oldPropNames.put("cc", "cursor-color");
        oldPropNames.put("bs", "backspace-send");
        oldPropNames.put("de", "delete-send");
        oldPropNames.put("sd", "select-delim");
        oldPropNames.put("pb", "paste-button");
    }

    public static String backwardCompatProp(String key) {
        String newName = (String)oldPropNames.get(key);
        if(newName != null) {
            key = newName;
        }
        return key;
    }

    public static String[] systemFonts;
    
//    public static String fontList() {
//        if(systemFonts == null)
//            systemFonts = com.ds.core.gui.GUI.getFontList();
//        String list = "";
//        for(int i = 0; i < systemFonts.length; i++) {
//            list += systemFonts[i];
//            if(i < systemFonts.length - 1)
//                list += ", ";
//        }
//        return list;
//    }

//    public static String defaultFont() {
//        if(fontExists("monospaced"))
//            return "Monospaced";
//        if(fontExists("courier"))
//            return "Courier";
//        if(fontExists("dialoginput"))
//            return "DialogInput";
//        return systemFonts[0];
//    }

//    public static boolean fontExists(String font) {
//        int i;
//        try {
//            if(systemFonts == null)
//                systemFonts = com.ds.core.gui.GUI.getFontList();
//            for(i = 0; i < systemFonts.length; i++) {
//                if(systemFonts[i].equalsIgnoreCase(font))
//                    break;
//            }
//            if(i == systemFonts.length)
//                return false;
//        } catch (Error e) {
//            // There is no display so we just fudge the result
//            // This gets things working when we run IESTTerm in console mode
//            // without a window system available.
//            return true;
//        }
//        return true;
//    }
}

