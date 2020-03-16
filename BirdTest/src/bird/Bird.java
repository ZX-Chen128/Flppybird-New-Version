package bird;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Bird {
    BufferedImage image;
    int x,y;
    int width,height;
    int size;
    // 重力加速度
    double g;
    // 位移的间隔时间
    double t;
    // 最初上抛速度
    double v0;
    // 当前上抛速度
    double speed;
    // 经过时间t之后的位移
    double s;
    // 小鸟的倾角（弧度）
    double alpha;

    BufferedImage[] images;
    int index;

    public Bird() throws IOException {
        image = ImageIO.read(getClass().getResource("/res/0.png"));
        width = image.getWidth();
        height = image.getHeight();
        x = 132;
        y = 280;
        size = 40;

        // 初始化位移参数
        g = 4;
        v0 = 20;
        t = 0.25;
        speed = v0;
        s = 0;
        alpha = 0;

        images = new BufferedImage[8];
        for(int i = 0;i < 8;i ++){
            images[i] = ImageIO.read(getClass().getResource("/res/" + i + ".png"));
        }

        index = 0;

    }

    public void fly(){
        index++;
        image = images[(index/12)%8];
    }

    public void step(){
        double v0 = speed;
        s = v0 * t + g * t * t / 2;
        y = y - (int) s;
        double v = v0 - g * t;
        speed = v;
        alpha = Math.atan(s / 8);
    }

    // 向上飞行
    public void flappy() {
        // 重置速度
        speed = v0;
    }

    public boolean hit(Ground ground) {
        boolean hit = y + size / 2 > ground.y;
        if (hit) {
            y = ground.y - size / 2;
            alpha = -3.14159265358979323 / 2;
        }
        return hit;
    }

    public boolean hit(Column column) {
        // 先检测是否在柱子的范围内
        if (x > column.x - column.width / 2 - size / 2
                && x < column.x + column.width / 2 + size / 2) {
            // 再检测是否在柱子的缝隙中
            if (y > column.y - column.gap / 2 + size / 2
                    && y < column.y + column.gap / 2 - size / 2) {
                return false;
            }
            return true;
        }
        return false;
    }
}


