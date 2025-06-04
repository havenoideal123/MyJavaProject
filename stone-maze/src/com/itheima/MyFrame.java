package com.itheima;

import javax.swing.*;
import java.awt.event.*;


public class MyFrame extends JFrame {
    private static String IMGPATH= "stone-maze/src/image/";
    //准备一个数组存储图片二维位置
    private int[][] imageArray = new int[][] {
            {0,1,2,3,},
            {4,5,6,7},
            {8,9,10,11},
            {12,13,14,15}
    };
    private int[][] winArray = new int[][] {
            {0,1,2,3,},
            {4,5,6,7},
            {8,9,10,11},
            {12,13,14,15}
    };

    //定义两个整数变量，记录当前空白块的位置
    private int blankX;
    private int blankY;
    private int count;



    //创建窗体
    public MyFrame() {
    InitFrame();
    //初始化界面
    //打乱图片顺序
    initRandomArray();
    initImage();

    //初始化系统菜单，添加菜单项
    initMenu();

    //给当前窗口绑定上下左右的按键事件
    initKey();
    this.setVisible(true);
    }
    private void initImage() {
        //先清空窗口上的所有涂层
        this.getContentPane().removeAll();


        //给窗口添加一个统计步数的窗口
        JLabel context = new JLabel("步数：" + count);
        context.setBounds(5, 0, 100, 20);
        this.add(context);

        //判断是否赢了游戏
        if (isWin()) {
            JLabel  win = new JLabel(new ImageIcon(IMGPATH + "win.png"));
            win.setBounds(100, 100, 266, 88);
            this.add(win);
        }

        //展示二维数组的图片，依此扑满4*4
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                //获取图片名称
                String imageName = imageArray[i][j] + ".png";

                //创建JLable对象，设置图片
                JLabel label = new JLabel(new ImageIcon(IMGPATH + imageName));
                label.setBounds(20+j * 100, 70+i * 100, 100, 100);
                //添加到窗体中
                this.add(label);

            }
        }
        //设置背景图片
        JLabel background = new JLabel(new ImageIcon(IMGPATH + "background.png"));
        background.setBounds(0, 0, 440, 484);
        this.add(background);

        //刷新涂层，重新绘制
        this.repaint();
    }
    private void InitFrame() {
      this.setTitle("石头迷阵");
      this.setSize(455, 555);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.setVisible(true);



    }

    private void initMenu() {
        //创建菜单条
        JMenuBar menuBar = new JMenuBar();
        //创建菜单
        JMenu menu = new JMenu("系统");
        //创建菜单项
        JMenuItem menuItem1 = new JMenuItem("重新开始");
        menu.add(menuItem1);
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initRandomArray();
                count=0;
                initImage();
            }
        });
        JMenuItem menuItem = new JMenuItem("退出");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //添加菜单条中
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void initRandomArray() {
        //打乱图片顺序
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                //随机生成一个数，将随机数和当前图片位置进行交换，确保图片位置乱序
                int random = (int) (Math.random() * 4);
                int temp = imageArray[i][j];
                imageArray[i][j] = imageArray[i][random];
                imageArray[i][random] = temp;
            }
        }

        //获取空白块的位置
        OUT:
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                if (imageArray[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                    break OUT;
                }
            }
        }
    }

    private void initKey() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //获取按键的键值
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        switchAndMov(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        switchAndMov(Direction.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        switchAndMov(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        switchAndMov(Direction.RIGHT);
                        break;
                }
            }
        });
    }
    private void switchAndMov(Direction r){
        //判断方向
        switch (r) {
            case UP:
                //判断是否行是否<3
                if(blankX<imageArray.length-1){
                    int temp = imageArray[blankX][blankY];
                    imageArray[blankX][blankY] = imageArray[blankX+1][blankY];
                    imageArray[blankX+1][blankY] = temp;
                    //更新空白块的位置
                    blankX++;
                    count++;

                }
                break;
            case DOWN:
                if (blankX>0){
                    int temp = imageArray[blankX][blankY];
                    imageArray[blankX][blankY] = imageArray[blankX-1][blankY];
                    imageArray[blankX-1][blankY] = temp;
                    //更新空白块的位置
                    blankX--;
                    count++;
                }
                break;
            case LEFT:
                if (blankY<imageArray.length-1){
                    int temp = imageArray[blankX][blankY];
                    imageArray[blankX][blankY] = imageArray[blankX][blankY+1];
                    imageArray[blankX][blankY+1] = temp;
                    //更新空白块的位置
                    blankY++;
                    count++;
                }
                break;
            case RIGHT:
                if (blankY>0){
                    int temp = imageArray[blankX][blankY];
                    imageArray[blankX][blankY] = imageArray[blankX][blankY-1];
                    imageArray[blankX][blankY-1] = temp;
                    //更新空白块的位置
                    blankY--;
                    count++;
                }
                break;
            default:
                break;
        }
        initImage();
    }

    private boolean isWin(){
        //判断游戏赢与条件，遍历数组，判断数组是否与winArray数组相同，相同返回true，否则返回false
        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                if (imageArray[i][j] != winArray[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
