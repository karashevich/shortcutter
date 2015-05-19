package shortcutter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ShortcutterDemo extends JFrame{

    private JPanel panel;
    private Dimension dimension = new Dimension(400, 300);

    public ShortcutterDemo() throws WrongShortcutException {
        super("Shortcutter Demo");
        this.setSize(dimension);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel(new BorderLayout());
        panel.setAlignmentX(CENTER_ALIGNMENT);
        panel.setSize(dimension);
        panel.setFocusable(true);
        panel.requestFocus();
        panel.setToolTipText("Press shortcut here while this window is active");
        final JLabel jLabel = new JLabel("Press shortcut here...");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(jLabel);

        panel.setVisible(true);


        Shortcutter.register(panel, "SHIFT_K", new Runnable() {
            @Override
            public void run() {
                System.out.println("action triggered");
            }
        });

        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);

    }

    public static void main(String[] args) throws WrongShortcutException {
        new ShortcutterDemo();
    }

}

