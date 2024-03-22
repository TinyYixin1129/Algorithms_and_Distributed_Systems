import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Activite4 extends JFrame {

    private final JTextField inputField;      //Periodic input box
    private final JTextField receptionField;  //Once input box
    private final JTextArea outputArea;       //Print box
    private String periodicMessage = "Default message";   //default message

    public Activite4(String args) {
        if (!args.isEmpty()) {
            periodicMessage = args;
        }
        setTitle("Activité 4");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());  //creat a global panel

        //set the top panel
        inputField = new JTextField();
        JButton changeButton = new JButton("Change periodic message");
        changeButton.addActionListener(e -> {
            changeButton.setEnabled(false);
            periodicMessage = inputField.getText().isEmpty() ? periodicMessage : inputField.getText();
            changeButton.setEnabled(true);
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(changeButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        //set the center output panel
        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        //output periodic message
        Timer timer = new Timer(1000, e -> {
            outputArea.append(periodicMessage + "\n");
            setScrollBarBottom(scrollPane);
            System.out.println(periodicMessage);
        });
        timer.start();

        //set the bottom panel
        receptionField = new JTextField();
        JButton receptionButton = new JButton("Send the message");
        receptionButton.addActionListener(e -> new Thread(() -> {   //开了新线程，对于主线程的话按钮这个动作就是一瞬间完成的所以gui不会被阻塞，而在子线程进行append
            receptionButton.setEnabled(false);
            timer.stop();
            //atomic
//            for (int i = 0; i < 25; i++) {
//                outputArea.append("*");
//                setScrollBarBottom(scrollPane);
//                System.err.print("*");
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException err) {
//                    System.out.println("Exception in atomicity: " + err.getMessage());
//                }
//            }
//            System.err.print("\n");
//            outputArea.append("\n");
            //atomic
            String receivedMessage = receptionField.getText();
            System.err.println("ERR: Received: " + receivedMessage);
            outputArea.append("ERR: Received: " + receivedMessage + "\n");
            setScrollBarBottom(scrollPane);
            timer.start();
            receptionButton.setEnabled(true);
        }).start());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(receptionField, BorderLayout.CENTER);
        bottomPanel.add(receptionButton, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);


        //finish setting
        setContentPane(panel);
        setVisible(true);

        /*
        //output periodic message
            while (true) {
                try {
                    outputArea.append(periodicMessage + "\n");
                    System.out.println(periodicMessage);
                    setScrollBarBottom(scrollPane);
                    Thread.sleep(2000);
                } catch (InterruptedException err) {
                    System.out.println("Exception : " + err.getMessage());
                }
            }
        */

        // Listen for standard input in a separate thread
        Thread inputListenerThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    timer.stop();
                    //atomic
//                    for (int i = 0; i < 25; i++) {
//                        outputArea.append("*");
//                        setScrollBarBottom(scrollPane);
//                        System.err.print("*");
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException err) {
//                            System.out.println("Exception in atomicity: " + err.getMessage());
//                        }
//                    }
//                    System.err.print("\n");
//                    outputArea.append("\n");
                    //atomic
                    System.err.println("ERR: Received: " + line);
                    outputArea.append("ERR: Received: " + line + "\n");
                    setScrollBarBottom(scrollPane);
                    timer.start();
                }
            } catch (IOException err) {
                System.out.println("Exception : " + err.getMessage());
            }
        });
        inputListenerThread.start();

    }

    private void setScrollBarBottom(JScrollPane scrollPane) {    //set the scroll bar at the bottom
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        boolean atBottom = verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() == verticalScrollBar.getMaximum();
        if (atBottom) {
            SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
        }
    }


    public static void main(String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        new Activite4(arg);
    }
}
