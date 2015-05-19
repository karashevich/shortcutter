package shortcutter;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

/**
 * Created by karashevich on 19/05/15.
 */

public class Shortcutter {

    public static final String CTRL = "CTRL";
    public static final String ALT = "ALT";
    public static final String SHIFT = "SHIFT";
    public static final String META = "META";

    public static void register(Component component, String shortcut, final Runnable runnable) throws WrongShortcutException {
        final KeyEvent defaultKeyEvent = new KeyEvent(component, 0, 0, 0, 0, ' ');

        final boolean ctrl = (shortcut.contains(CTRL));
        final boolean alt = (shortcut.contains(ALT));
        final boolean shift =  (shortcut.contains(SHIFT));
        final boolean meta = (shortcut.contains(META));

        final String mask = (ctrl? CTRL + "_": "") +
                (alt? ALT + "_": "") +
                (shift? SHIFT + "_": "") +
                (meta? META + "_": "");

        if (!validate(component, shortcut, mask)) throw new WrongShortcutException("Wrong format in \"" + shortcut + "\" shortcut. Please see KeyEvent constants to find right constants.");
        final String key = (mask.equals("") ? shortcut.substring(1) : shortcut.substring(mask.length()));

        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (!(ctrl ^ e.isControlDown()) && !(alt ^ e.isAltDown()) && !(shift ^ e.isShiftDown()) && !(meta ^ e.isMetaDown())) {

                    try {
                        final Field field = defaultKeyEvent.getClass().getField("VK_" + key);
                        int triggerKeyCode = (Integer) field.get(defaultKeyEvent);
                        if (e.getKeyCode() == triggerKeyCode) {
                            runnable.run();
                        }
                    } catch (NoSuchFieldException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    private static String calc_mask(String shortcut){
        final boolean ctrl = (shortcut.contains(CTRL));
        final boolean alt = (shortcut.contains(ALT));
        final boolean shift =  (shortcut.contains(SHIFT));
        final boolean meta = (shortcut.contains(META));

        return (ctrl? CTRL + "_": "") +
                (alt? ALT + "_": "") +
                (shift? SHIFT + "_": "") +
                (meta? META + "_": "");
    }

    public static boolean validate(Component component, @NotNull String shortcut, @Nullable String mask) {
        final KeyEvent defaultKeyEvent = new KeyEvent(component, 0, 0, 0, 0, ' ');
        if (mask == null) mask = "";
        if (mask.equals("")) {
            try {
                final Field field = defaultKeyEvent.getClass().getField("VK" + shortcut);
                return true;
            } catch (NoSuchFieldException e) {
                return false; //error in keycode
            }
        } else {
            if (mask.length() > shortcut.length()) return false;
            String key = shortcut.substring(mask.length());
            try {
                final Field field = defaultKeyEvent.getClass().getField("VK_" + key);
                return true;
            } catch (NoSuchFieldException e) {
                return false; //error in keycode
            }
        }
    }

//    public static void main(String[] args) {
//        Component c = new JFrame();
//        String shortcut;
//
//        shortcut = "_ENTER";
//        System.out.println("validate(" + shortcut +"): " + validate(c, shortcut, calc_mask(shortcut)) + ";");
//
//        shortcut = "SHIFT_ESC";
//        System.out.println("validate(" + shortcut +"): " + validate(c, shortcut, calc_mask(shortcut)) + ";");
//
//        shortcut = "CTRLE_SHIFT_R";
//        System.out.println("validate(" + shortcut +"): " + validate(c, shortcut, calc_mask(shortcut)) + ";");
//
//        shortcut = "CTRL_SHIFT_R";
//        System.out.println("validate(" + shortcut +"): " + validate(c, shortcut, calc_mask(shortcut)) + ";");
//
//
//    }
}
