import java.applet.Applet;
import java.awt.*;

/**
 * 第2天：图片翻折切换效果。
 * 学习点：Applet 参数、MediaTracker 批量加载、离屏缓冲、动画节奏
 */
public class FlipApplet extends Applet implements Runnable {

    Image images[];             // 待切换的多张图片
    MediaTracker imageTracker;  // 批量加载跟踪器
    int imageWidth, imageHeight;
    int currentImage;           // 当前显示的图片下标
    int totalImage;             // 图片总数
    Thread thread;              // 翻折动画线程
    int delay;                  // 两张图切换之间的停顿（毫秒）
    Image image;                // 离屏缓冲图（双缓冲，减少闪烁）
    Graphics graphics;          // 离屏缓冲的画笔

    /** 初始化：读参数、加载全部图片、创建离屏缓冲 */
    @Override
    public void init() {
        totalImage = 5;
        currentImage = 0;
        setBackground(Color.white);
        images = new Image[totalImage];
        imageTracker = new MediaTracker(this);

        // 从 HTML 读取 image1~image5
        String param;
        for (int i = 0; i < totalImage; i++) {
            param = getParameter("image" + (i + 1));
            images[i] = getImage(getCodeBase(), param);
            imageTracker.addImage(images[i], 0);
        }
        try {
            imageTracker.waitForID(0);              // 等全部加载完
        } catch (InterruptedException e) {
        }

        // 切换间隔，缺省 3 秒
        param = getParameter("delay");
        if (param == null) {
            delay = 3000;
        } else {
            delay = Integer.parseInt(param);
        }

        imageWidth = images[0].getWidth(this);
        imageHeight = images[0].getHeight(this);
        image = createImage(imageWidth, imageHeight);   // 离屏图
        graphics = image.getGraphics();
    }

    /** 启动翻折线程 */
    @Override
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /** 把离屏缓冲画到屏幕 */
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    /** 直接调用 paint，避免默认 update 清屏造成闪烁 */
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * 动画循环：
     * 1）当前图从正常高度压扁到 0（翻出去）
     * 2）切到下一张
     * 3）新图从 0 展开到正常高度（翻进来）
     * 4）停顿 delay 毫秒后重复
     */
    @Override
    public void run() {
        while (thread != null) {
            try {
                // 压扁：绘制高度逐渐变小
                for (int i = 0; i <= (imageHeight / 2); i++) {
                    Thread.sleep(30);
                    graphics.setColor(Color.white);
                    graphics.fillRect(0, 0, imageWidth, imageHeight);
                    graphics.drawImage(images[currentImage], 0, i, imageWidth, imageHeight - 2 * i, this);
                    repaint();
                }

                currentImage = (currentImage + 1) % totalImage;   // 下一张

                // 展开：绘制高度逐渐变大
                for (int i = 0; i <= (imageHeight / 2); i++) {
                    Thread.sleep(30);
                    graphics.setColor(Color.white);
                    graphics.fillRect(0, 0, imageWidth, imageHeight);
                    graphics.drawImage(images[currentImage], 0, (imageHeight / 2) - i, imageWidth, 2 * i, this);
                    repaint();
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }
}
