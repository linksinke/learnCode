# Day02 - 图片翻折

用 Applet 实现多图翻折切换动画。

## 学习点

- `<param>` 向 Applet 传参
- `MediaTracker` 批量加载多张图
- 离屏缓冲（`createImage` + `Graphics`）减少闪烁
- 通过改变绘制高度模拟翻折

## 目录

```
day02-flip/
├── FlipApplet.java         # 源码
├── res/                    # 页面 + 静态资源
│   ├── index.html
│   ├── image0.jpg … image4.jpg
│   └── …
└── README.md
```

## 编译与运行

```bash
javac -encoding UTF-8 -d res FlipApplet.java
appletviewer res/index.html
```
