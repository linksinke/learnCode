/*
 * Portions of KeyboardPanel's source code were excerpted from Sun's MidiSynth.java
 * source file. I've included Sun's original copyright and license, to be fair
 * to Sun.
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL
 * SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or
 * in the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * 第3天：MIDI 钢琴键盘面板（Swing 组件）。
 * 学习点：鼠标事件、自定义绘制、Java Sound MIDI API
 */
public class KeyboardPanel extends JPanel {

    public final static Color KEY_BLUE = new Color(204, 204, 255); // 按下时的高亮色
    public final static int KEY_HEIGHT = 80, KEY_WIDTH = 16;       // 白键尺寸

    private Key theKey;                 // 当前按下的键
    private MidiChannel channel;        // MIDI 通道（发声用）
    private Synthesizer synthesizer;    // MIDI 合成器

    private Vector blackKeys = new Vector(); // 黑键
    private Vector whiteKeys = new Vector(); // 白键
    private Vector keys = new Vector();      // 全部键（点选命中时先查黑键）

    public KeyboardPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(42 * KEY_WIDTH + 1, KEY_HEIGHT + 1));

        int transpose = 24;                     // 音高偏移（MIDI note）
        int[] whiteIDs = {0, 2, 4, 5, 7, 9, 11}; // 一个八度内白键相对音高

        // 生成 6 组白键（每组 7 个）
        for (int i = 0, x = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++, x += KEY_WIDTH) {
                int keyNum = i * 12 + whiteIDs[j] + transpose;
                whiteKeys.add(new Key(x, 0, KEY_WIDTH, KEY_HEIGHT, keyNum));
            }
        }

        // 生成黑键：位置插在白键之间，高度约为白键一半
        for (int i = 0, x = 0; i < 6; i++, x += KEY_WIDTH) {
            int keyNum = i * 12 + transpose;

            blackKeys.add(new Key((x += KEY_WIDTH) - 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2, keyNum + 1));
            blackKeys.add(new Key((x += KEY_WIDTH) - 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2, keyNum + 3));
            x += KEY_WIDTH;

            blackKeys.add(new Key((x += KEY_WIDTH) - 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2, keyNum + 6));
            blackKeys.add(new Key((x += KEY_WIDTH) - 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2, keyNum + 8));
            blackKeys.add(new Key((x += KEY_WIDTH) - 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2, keyNum + 10));
        }

        // 黑键放前面，点击重叠区域时优先命中黑键
        keys.addAll(blackKeys);
        keys.addAll(whiteKeys);

        // 鼠标按下发声，松开/移出停止
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                theKey = getKey(e.getPoint());
                if (theKey != null) {
                    theKey.on();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (theKey != null) {
                    theKey.off();
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标拖出面板时也要停音，并清空引用避免重复处理
                if (theKey != null) {
                    theKey.off();
                    repaint();
                    theKey = null;
                }
            }

            /** 根据点击坐标命中琴键 */
            public Key getKey(Point point) {
                for (int i = 0; i < keys.size(); i++) {
                    if (((Key) keys.get(i)).contains(point)) {
                        return (Key) keys.get(i);
                    }
                }
                return null;
            }
        });
    }

    /** 切换乐器音色（MIDI program） */
    public boolean chooseInstrument(int instrumentID) {
        if (channel == null) {
            return false;
        }
        channel.programChange(instrumentID);
        return true;
    }

    /** 列出已加载的乐器名称 */
    public String[] getInstruments() {
        if (synthesizer == null) {
            return null;
        }
        Instrument[] instruments = synthesizer.getLoadedInstruments();
        String[] ins = new String[instruments.length];
        for (int i = 0; i < instruments.length; i++) {
            ins[i] = instruments[i].toString();
        }
        return ins;
    }

    /** 先画白键，再画黑键（黑键盖在上面） */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();

        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);

        g2.setColor(Color.white);
        g2.fillRect(0, 0, 42 * KEY_WIDTH, KEY_HEIGHT);

        for (int i = 0; i < whiteKeys.size(); i++) {
            Key key = (Key) whiteKeys.get(i);
            if (key.isNoteOn()) {
                g2.setColor(KEY_BLUE);
                g2.fill(key);
            }
            g2.setColor(Color.black);
            g2.draw(key);
        }

        for (int i = 0; i < blackKeys.size(); i++) {
            Key key = (Key) blackKeys.get(i);
            if (key.isNoteOn()) {
                g2.setColor(KEY_BLUE);
                g2.fill(key);
                g2.setColor(Color.black);
                g2.draw(key);
            } else {
                g2.setColor(Color.black);
                g2.fill(key);
            }
        }
    }

    /** 关闭合成器并卸载音色 */
    public void turnOff() {
        if (synthesizer == null) {
            return;
        }
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.close();
        synthesizer = null;
    }

    /** 打开默认合成器，加载音色，绑定第 0 通道 */
    public boolean turnOn() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    return false;
                }
                synthesizer.open();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
        MidiChannel[] midiChannels = synthesizer.getChannels();

        if (midiChannels.length == 0 || midiChannels[0] == null) {
            synthesizer.close();
            synthesizer = null;
            return false;
        }

        channel = midiChannels[0];
        return true;
    }

    /** 单键：既是矩形区域，也能通过 MIDI 发声/停声 */
    class Key extends Rectangle {
        final static int ON = 0, OFF = 1;
        final static int VELOCITY = 64; // 力度（音量）

        int kNum;               // MIDI 音符号（60 = 中央 C）
        int noteState = OFF;    // 当前是否按下

        public Key(int x, int y, int width, int height, int num) {
            super(x, y, width, height);
            kNum = num;
        }

        public boolean isNoteOn() {
            return noteState == ON;
        }

        /** 松开：停声 */
        public void off() {
            setNoteState(OFF);
            if (channel != null) {
                channel.noteOff(kNum, VELOCITY);
            }
        }

        /** 按下：发声 */
        public void on() {
            setNoteState(ON);
            if (channel != null) {
                channel.noteOn(kNum, VELOCITY);
            }
        }

        public void setNoteState(int state) {
            noteState = state;
        }
    }
}
