import javax.swing.*;

public class App {
    public static JFrame homeWindow;
    public static JFrame customerWindow;
    public static JFrame orderWindow;

    public static void main(String[] args) throws Exception {
        homeWindow = getMainWindow();

        homeWindow.setVisible(true);  
    }

    private static JFrame getMainWindow(){
        JFrame returnFrame = new JFrame();
        MainView mainView = new MainView();

        returnFrame.setTitle("Home");
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.add(mainView.GetPanel());
        returnFrame.pack();
        returnFrame.setLocationRelativeTo(null);

        return returnFrame;
    }

    public static JFrame getCustomerWindow(){
        JFrame returnFrame = new JFrame();
        CustomerView customerView = new CustomerView();

        returnFrame.setTitle("Customer");
        returnFrame.setSize(1050, 450);
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.add(customerView.GetPanel());
        returnFrame.setLocationRelativeTo(null);

        return returnFrame;
    }

    public static JFrame getOrderWindow(){
        JFrame returnFrame = new JFrame();
        OrderView orderView = new OrderView();

        returnFrame.setTitle("Customer");
        returnFrame.setSize(1050, 450);
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.add(orderView.GetPanel());
        returnFrame.setLocationRelativeTo(null);

        return returnFrame;
    }
}
