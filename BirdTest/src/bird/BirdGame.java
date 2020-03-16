package bird;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
public class BirdGame extends Panel {

    BufferedImage bg;
    BufferedImage startImage;
    BufferedImage gameOverImage;

    Ground ground;

    Column column1,column2;

    Bird bird;

    int score;

    // 游戏状态
    int state;
    // 状态常量
    public static final int START = 0; //开始
    public static final int RUNNING = 1; //运行
    public static final int GAME_OVER = 2; //结束

    public BirdGame() throws Exception {
        bg = ImageIO.read(getClass().getResource("/res/bg.png"));
        startImage = ImageIO.read(getClass().getResource("/res/start.png"));
        gameOverImage = ImageIO.read(getClass().getResource("/res/gameover.png"));

        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        bird = new Bird();

        score = 0;
        state = START;
    }

    public void paint(Graphics g){
        g.drawImage(bg,0,0,null);
        g.drawImage(ground.image,ground.x,ground.y,null);
        g.drawImage(column1.image,column1.x-column1.width/2,column1.y-column1.height/2,null);
        g.drawImage(column2.image,column2.x-column2.width/2,column2.y-column2.height/2,null);

        Graphics2D g2 = (Graphics2D)g;
        g2.rotate(-bird.alpha,bird.x,bird.y);
        g.drawImage(bird.image,bird.x-bird.width/2,bird.y-bird.height/2,null);
        g2.rotate(bird.alpha,bird.x,bird.y);

        Font f = new Font(Font.SANS_SERIF,Font.BOLD,40);
        g.setFont(f);
        g.drawString("" + score, 40, 60);
        g.setColor(Color.WHITE);
        g.drawString("" + score, 40 - 3, 60 - 3);

        // 绘制开始与结束界面
        switch (state) {
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(gameOverImage, 0, 0, null);
                break;
        }
    }

    public void action() throws Exception {

        MouseListener l = new MouseAdapter() {
            // 鼠标按下事件
            public void mousePressed(MouseEvent e) {
                try {
                    switch (state) {
                        case START:
                            // 在开始状态，按下鼠标则转为运行状态。
                            state = RUNNING;
                            break;
                        case RUNNING:
                            // 在运行状态，按下鼠标则小鸟向上飞行。
                            bird.flappy();
                            break;
                        case GAME_OVER:
                            // 在结束状态，按下鼠标则重置数据，再次转为开始态。
                            column1 = new Column(1);
                            column2 = new Column(2);
                            bird = new Bird();
                            score = 0;
                            state = START;
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        addMouseListener(l);


        while(true){
            switch (state){
                case START:
                    bird.fly();
                    ground.step();
                    break;
                case RUNNING:
                    ground.step();
                    column1.step();
                    column2.step();
                    bird.fly();
                    bird.step();
                    if(bird.x == column1.x||bird.x == column2.x){
                        score++;
                    }
                    if(bird.hit(ground)||bird.hit(column1)||bird.hit(column2)){
                        state = GAME_OVER;
                    }
                    break;
            }
            repaint();
            Thread.sleep(800/60);
        }
    }



    public static void main(String[] args) throws Exception {
        JFrame jf = new JFrame();
        BirdGame bd = new BirdGame();
        jf.add(bd);
        jf.setSize(430,670);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        bd.action();
    }
}
