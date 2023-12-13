package org.example.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;


public class ServerWindow extends JFrame {
    private static final int POS_X =500;
    private static final int POS_Y = 500;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    JPanel panel, panelButton;
    JButton buttonStart, buttonStop;
    JTextArea log;
    boolean isServerWorking;

    private final HashMap<String, char[]> mapUsers = new HashMap<>();
    private String ipAddress;
    private String port;

    public ServerWindow() {
        mapUsers.put("Ol", "123456".toCharArray());
        mapUsers.put("Ok", "123".toCharArray());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        buttonStart = new JButton("Start");
        buttonStop = new JButton("Stop");
        panelButton = new JPanel(new GridLayout(1, 2));
        panelButton.add(buttonStart);
        panelButton.add(buttonStop);

        log = new JTextArea();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelButton, BorderLayout.NORTH);
        panel.add(log, BorderLayout.SOUTH);
        add(panel);

        isServerWorking = false;

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerWorking) {
                    isServerWorking = true;
                    initialServer();
                } else {
                    log.append("The server is already running!\n");
                }
            }

        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking) {
                    isServerWorking = false;
                    log.append("Server stopped.\n");
                } else {
                    log.append("The server has already stopped!\n");
                }
            }
        });

        setVisible(true);
    }

    private void initialServer() {
        log.append("Server started.\n");
        ipAddress = "127.0.0.1";
        port = "8189";
        log.append("IP Address " + ipAddress + "\n");
        log.append("Port " + port + "\n");
    }

    public String getIP() {
        if(isServerWorking) return this.ipAddress;
        return null;
    }

    public String getPort() {
        if(isServerWorking) return this.port;
        return null;
    }

    public Boolean getAuth(String login, char[] password) {
        return Arrays.equals(mapUsers.get(login), password);
    }
}
