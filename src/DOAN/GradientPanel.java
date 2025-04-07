package DOAN;

import javax.swing.*;
import java.awt.*;

class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Màu tím sáng + ánh hồng
        Color color1 = new Color(136, 78, 160);   // Tím nhạt
        Color color2 = new Color(186, 104, 200);  // Hồng tím
        Color color3 = new Color(225, 159, 255);  // Ánh sáng hồng

        // Gradient từ trên xuống giữa
        GradientPaint gp1 = new GradientPaint(0, 0, color1, width / 2, height / 2, color2, true);
        g2d.setPaint(gp1);
        g2d.fillRect(0, 0, width, height);

        // Gradient từ giữa xuống dưới
        GradientPaint gp2 = new GradientPaint(width / 2, height / 2, color2, width, height, color3, true);
        g2d.setPaint(gp2);
        g2d.fillRect(0, 0, width, height);
    }
}
