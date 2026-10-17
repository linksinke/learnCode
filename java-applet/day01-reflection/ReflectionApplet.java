import java.applet.Applet;
import java.awt.*;

/**
 * 第1天：图片倒影（水波纹）效果。
 * 学习点：Applet 生命周期、Thread、MediaTracker、Graphics.copyArea
 */
public class ReflectionApplet extends Applet implements Runnable {

    Thread lakeThread;          // 动画线程
    Graphics graphics;          // Applet 画笔
    Graphics waveGraphics;      // 波浪图的画笔
    Image image;                // 原图
    Image waveImage;            // 预生成的倒影波浪图（含多帧）
    int currentImage;           // 当前波浪帧（0~11）
    int imageWidth, imageHeight;
    boolean imageLoaded;        // 图片是否已加载成功
    String imageName;           // HTML 传入的图片路径

    /** 初始化：只读一次 HTML 参数 */
    @Override
    public void init() {
        imageName = getParameter("image");
    }

    /** 启动：创建并启动动画线程 */
    @Override
    public void start() {
        if (lakeThread == null) {
            lakeThread = new Thread(this);
            lakeThread.start();
        }
    }

    /** 线程入口：先加载图片并生成波浪帧，再循环刷新 */
    @Override
    public void run() {
        currentImage = 0;
        if (!imageLoaded) {
            repaint();
            graphics = getGraphics();
            MediaTracker mediatracker = new MediaTracker(this);
            // 相对 HTML 所在目录加载图片
            image = getImage(getDocumentBase(), imageName);
            mediatracker.addImage(image, 0);
            try {
                mediatracker.waitForAll();              // 阻塞直到加载完成
                imageLoaded = !mediatracker.isErrorAny();
            } catch (InterruptedException ex) {
            }
            if (!imageLoaded) {
                stop();
                graphics.drawString("加载图片错误", 10, 40);
                return;
            }
            imageWidth = image.getWidth(this);
            imageHeight = image.getHeight(this);
            createAnimation();                          // 预生成倒影 + 波浪
        }
        repaint();
        try {
            while (true) {
                repaint();
                currentImage++;
                if (currentImage == 12) {               // 12 帧循环
                    currentImage = 0;
                }
                Thread.sleep(50);                       // 控制动画速度
            }
        } catch (InterruptedException ex) {
            stop();
        }
    }

    @Override
    public void paint(Graphics g) {
        update(g);
    }

    /** 绘制：先画倒影波浪，再画原图盖在上面 */
    @Override
    public void update(Graphics g) {
        if (waveImage != null) {
            // 左右两段拼接，实现波浪横向滚动
            g.drawImage(waveImage, -currentImage * imageWidth, imageHeight - 1, this);
            g.drawImage(waveImage, (12 - currentImage) * imageWidth, imageHeight - 1, this);
        }
        g.drawImage(image, 0, 1, this);
    }

    /** 生成倒影图，并预计算 12 帧波浪效果 */
    public void createAnimation() {
        Image img = createImage(imageWidth, imageHeight);
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, this);
        // 逐行拷贝，把原图上下翻转成倒影
        for (int i = 0; i < imageHeight; i++) {
            g.copyArea(0, imageHeight - 1 - i, imageWidth, 1, 0, -imageHeight + 1 + (i * 2));
        }

        // 横向拼 13 份倒影，供滚动裁剪使用
        waveImage = createImage(13 * imageWidth, imageHeight);
        waveGraphics = waveImage.getGraphics();
        waveGraphics.drawImage(img, 12 * imageWidth, 0, this);
        for (int j = 0; j < 12; j++) {
            makeWaves(waveGraphics, j);
        }
        g.drawImage(image, 0, 0, this);
    }

    /** 对第 i 帧做正弦错位拷贝，模拟水面波动 */
    public void makeWaves(Graphics g, int i) {
        double d = (6.2831853071795862 * i) / 12;       // 相位：2π * i / 12
        int j = (12 - i) * imageWidth;
        int waveHeight = imageHeight / 16;
        for (int l = 0; l < imageHeight; l++) {
            int k = (int) ((waveHeight * (l + 28) * Math.sin(waveHeight * (imageHeight - l) / (l + 1) + d)) / imageHeight);
            if (l < -k) {
                g.copyArea(12 * imageWidth, l, imageWidth, 1, -j, 0);
            } else {
                g.copyArea(12 * imageWidth, l + k, imageWidth, 1, -j, -k);
            }
        }
    }
}
