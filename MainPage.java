import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MainPage extends JFrame implements ActionListener
{
    private JLabel lblStart;
    private JButton btnStart;
    private Container cont;
    private FlowLayout layout;
    //each guest access the system
    private static int accessCount = 0;
    public MainPage()
    {
        super("LUMINA HOTEL BOOKING SYSTEM");
        accessCount++;
        layout = new FlowLayout(FlowLayout.CENTER, 10, 30);
        cont = getContentPane();
        cont.setLayout(layout);
        cont.setBackground(new Color(80, 12, 35));
        ImageIcon logo = new ImageIcon("hotel-logo.png");
        Image image = logo.getImage().getScaledInstance(250, 250,
                Image.SCALE_SMOOTH);
        ImageIcon scaledLogo = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledLogo);
        lblStart = new JLabel("Let's start your relaxation with us.");
        lblStart.setFont(new Font("Arial", Font.ITALIC, 22));
        lblStart.setForeground(Color.WHITE);
        lblStart.setHorizontalAlignment(SwingConstants.CENTER);
        lblStart.setPreferredSize(new Dimension(500, 40));
        btnStart = new JButton("Enter System");
        btnStart.setFont(new Font("Arial", Font.BOLD, 16));
        btnStart.setPreferredSize(new Dimension(150, 40));
        cont.add(imageLabel);
        cont.add(lblStart);
        cont.add(btnStart);
        btnStart.setActionCommand("openBooking");
        btnStart.addActionListener(this);
        setSize(550, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        if ("openBooking".equals(e.getActionCommand()))
        {
            navigateBooking();
        }
    }

    private void navigateBooking() {
        new RoomSelectionPage();
        dispose();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new MainPage());
    }
}