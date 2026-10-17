import javax.swing.JApplet;
import java.awt.BorderLayout;

/**
 * 第3天入口：把 KeyboardPanel 嵌进 JApplet，供 appletviewer 运行。
 * 学习点：JApplet 生命周期、组合自定义面板
 */
public class KeyboardApplet extends JApplet {

    private KeyboardPanel keyboard;

    /** 初始化：加入键盘面板，并打开 MIDI 合成器 */
    @Override
    public void init() {
        keyboard = new KeyboardPanel();
        setLayout(new BorderLayout());
        add(keyboard, BorderLayout.CENTER);
        if (!keyboard.turnOn()) {
            showStatus("MIDI 合成器初始化失败");
        }
    }

    /** 销毁：关闭合成器，释放系统资源 */
    @Override
    public void destroy() {
        if (keyboard != null) {
            keyboard.turnOff();
        }
    }
}
