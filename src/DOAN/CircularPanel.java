package DOAN;

import javax.swing.*;
import java.awt.*;

//====== Lớp tạo khung tròn chứa chữ "ADMIN" ======
class CirclePanel extends JPanel {
 private String text;

 public CirclePanel(String text) {
     this.text = text;
     setPreferredSize(new Dimension(150, 150)); // Đảm bảo hình vuông
     setMaximumSize(new Dimension(150, 150)); // Giữ kích thước không bị kéo
     setOpaque(false);
 }

 @Override
 protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     Graphics2D g2d = (Graphics2D) g;
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

     int diameter = Math.min(getWidth(), getHeight()); // Luôn giữ hình tròn
     int x = (getWidth() - diameter) / 2;
     int y = (getHeight() - diameter) / 2;

     // Vẽ hình tròn
     g2d.setColor(new Color(0, 102, 204));
     g2d.fillOval(x, y, diameter, diameter);

     // Vẽ viền trắng
     g2d.setColor(Color.WHITE);
     g2d.setStroke(new BasicStroke(3));
     g2d.drawOval(x, y, diameter, diameter);

     // Chữ ADMIN
     g2d.setColor(Color.WHITE);
     g2d.setFont(new Font("Arial", Font.BOLD, 18));
     FontMetrics fm = g2d.getFontMetrics();
     int textX = x + (diameter - fm.stringWidth(text)) / 2;
     int textY = y + (diameter + fm.getAscent()) / 2 - 5;
     g2d.drawString(text, textX, textY);
 }
}

