# Day03 - MIDI 键盘

用 JApplet + Swing 面板模拟钢琴键盘，点击琴键发声。

## 学习点

- `JApplet` 作为入口，嵌入自定义 `JPanel`
- 鼠标按下/松开控制音符开关
- Java Sound：`Synthesizer` / `MidiChannel`
- 自定义 `paint` 绘制黑白键

## 目录

```
day03-keyboard/
├── KeyboardApplet.java     # Applet 入口
├── KeyboardPanel.java      # 键盘面板 + MIDI
├── res/
│   └── index.html
└── README.md
```

## 编译与运行

```bash
javac -encoding UTF-8 -d res KeyboardApplet.java KeyboardPanel.java
appletviewer res/index.html
```
