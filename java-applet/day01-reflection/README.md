# Day01 - 图片倒影

用 Applet 实现图片倒影与水波纹动画。

## 学习点

- Applet 生命周期：`init` / `start` / `stop` / `paint`
- `Thread` + `Runnable` 做动画循环
- `MediaTracker` 加载图片
- `Graphics.copyArea` 做倒影与波浪

## 目录

```
day01-reflection/
├── ReflectionApplet.java   # 源码
├── res/                    # 页面 + 静态资源（编译后的 .class 也输出到这里）
│   ├── index.html
│   ├── lake.gif
│   └── …
└── README.md
```

## 编译与运行

```bash
javac -encoding UTF-8 -d res ReflectionApplet.java
appletviewer res/index.html
```
