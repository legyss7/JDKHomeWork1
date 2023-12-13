package org.example.client;

import org.example.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.*;


public class ClientGUI extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private static final String IP_ADDERS = "127.0.0.1";
    private static final String PORT = "8189";
    private static final String LOGIN = "Ol";
    private static final String PASSWORD = "123456";


    JPanel panelSetupData, panelSetup, panelSent,
            panelLogUsers, panelUsers, panelClient;
    JTextField fieldIPAddress, fieldPort, fieldLogin, fieldMessageSent;
    JPasswordField passwordField;
    JButton buttonConnect, buttonSend;
    JTextArea logMessage, logUsers;

    String message;
    private boolean flagConnection = false;

    Date date;

    public ClientGUI(ServerWindow serverWindow) {
        date = new Date();


        initialFormClients();


        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flagConnection) {
                    if (checkConnect(serverWindow)) {
                        if (checkAuth(serverWindow)) {
                            message = readLog();
                            System.out.printf(message);
                            logMessage.setText(message);
                            message = date + "\t" + fieldLogin.getText()
                                    + " >> " + "connected\n";
                            logUsers.setText(fieldLogin.getText());
                            logMessage.append(message);
                            saveLog(message);
                            System.out.printf(message);
                            buttonConnect.setText("Disconnected");
                            flagConnection = true;
                        } else {
                            message = "Authorisation Error!\n";
                            System.out.println(message);
                        }
                    } else {
                        message = "No connection!\n";
                        System.out.println(message);
                    }
                } else {
                    buttonConnect.setText("Connected");
                    flagConnection = false;
                    message = date + "\t" + fieldLogin.getText()
                            + " >> " + "disconnect\n";
                    logMessage.append(message);
                    System.out.printf(message);
                    saveLog(message);
                }
            }
        });


        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(flagConnection);
            }
        });

        fieldIPAddress.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fieldIPAddress.getText().equals(IP_ADDERS)) {
                    fieldIPAddress.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fieldIPAddress.getText().isEmpty()) {
                    fieldIPAddress.setText(IP_ADDERS);
                }
            }
        });

        fieldPort.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fieldPort.getText().equals(PORT)) {
                    fieldPort.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fieldPort.getText().isEmpty()) {
                    fieldPort.setText(PORT);
                }
            }
        });


        fieldLogin.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fieldLogin.getText().equals(LOGIN)) {
                    fieldLogin.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fieldLogin.getText().isEmpty()) {
                    fieldLogin.setText(LOGIN);
                }
            }
        });

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Arrays.equals(passwordField.getPassword(), PASSWORD.toCharArray())) {
                    passwordField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(PASSWORD);
                }
            }
        });


    }

            private void saveLog(String string) {
                try (FileWriter writer = new FileWriter("log.txt", true)) {
                    writer.write(string);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            private String readLog() {
                StringBuilder str = new StringBuilder();
                try (FileReader fileReader = new FileReader("log.txt")) {
                    int i;
                    while ((i = fileReader.read()) != -1) {
                        str.append((char) i);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return e.getMessage();
                }
                return str.toString();
            }


            private void sendMessage(boolean flagConnection) {
                if (flagConnection) {
                    message = date + "\t" + fieldLogin.getText()
                            + " >> " + fieldMessageSent.getText() + "\n";
                    System.out.printf(message);
                    logMessage.append(message);
                    saveLog(message);
                    fieldMessageSent.setText("");
                }
            }


            private boolean checkConnect(ServerWindow serverWindow) {
                return Objects.equals(fieldIPAddress.getText(), serverWindow.getIP())
                        && Objects.equals(fieldPort.getText(), serverWindow.getPort());
            }

            private boolean checkAuth(ServerWindow serverWindow) {
                return serverWindow.getAuth(fieldLogin.getText(), passwordField.getPassword());
            }

            private void initialFormClients() {
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
                setSize(WIDTH, HEIGHT);
                setTitle("Chat Client");

                panelSetupData = new JPanel(new GridLayout(4, 2));
                panelSetupData.add(new Label("Server IP Data"));
                fieldIPAddress = new JTextField("127.0.0.1");
                panelSetupData.add(fieldIPAddress);
                panelSetupData.add(new Label("Port"));
                fieldPort = new JTextField("8189");
                panelSetupData.add(fieldPort);
                panelSetupData.add(new Label(LOGIN));
                fieldLogin = new JTextField("Ol");
                panelSetupData.add(fieldLogin);
                panelSetupData.add(new Label("Password"));
                passwordField = new JPasswordField("123456");
                panelSetupData.add(passwordField);

                panelSetup = new JPanel(new GridLayout(1, 3));
                panelSetup.setLayout(new BorderLayout());
                panelSetup.add(panelSetupData, BorderLayout.CENTER);
                buttonConnect = new JButton("Connected");
                panelSetup.add(buttonConnect, BorderLayout.EAST);

                panelSent = new JPanel();
                panelSent.setLayout(new BorderLayout());
                fieldMessageSent = new JTextField();
                panelSent.add(fieldMessageSent, BorderLayout.CENTER);
                buttonSend = new JButton("Send");
                panelSent.add(buttonSend, BorderLayout.EAST);

                panelLogUsers = new JPanel();
                logMessage = new JTextArea("Message history");
                panelLogUsers.add(logMessage);

                panelUsers = new JPanel();
                panelUsers.setLayout(new BorderLayout());
                panelUsers.add(panelSetup, BorderLayout.NORTH);
                panelUsers.add(new JScrollPane(panelLogUsers));
                panelUsers.add(panelSent, BorderLayout.SOUTH);

                panelClient = new JPanel();
                panelClient.setLayout(new BorderLayout());
                logUsers = new JTextArea("Users");
                logUsers.setPreferredSize(new Dimension(WIDTH / 10, HEIGHT));
                panelClient.add(panelUsers, BorderLayout.CENTER);
                panelClient.add(logUsers, BorderLayout.WEST);
                add(panelClient);
                setVisible(true);
            }
        }