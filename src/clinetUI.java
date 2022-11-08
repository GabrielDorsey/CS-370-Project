import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class clinetUI extends JFrame{
    private JPanel mainPanel;
    private JTextField textBox;
    private JButton sendButton;
    private  JTextArea chatArea;
    private  static Socket socket;
    private static InputStreamReader inStream;
    private  static BufferedWriter buffWriter;
    private static  BufferedReader buffReader;




    public clinetUI(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textBox.getText();
                if(!message.isEmpty()){
                    chatArea.setText(chatArea.getText().trim()+ "\n Client:\t" + message );
                    sendMessage(message);

                }
                else {
                    System.out.println("Could not retrive");
                }
            }
        });
    }

    public void sendMessage(String message){
        try{
            buffWriter.write(message);
            buffWriter.newLine();
            buffWriter.flush();

        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("Could Not Send Message.");
        }
    }

    public static void closeConnection(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){
        try{
            if(socket!= null){
                socket.close();
            }
            if(buffReader != null) {
                buffReader.close();
            }
            if(buffWriter!= null){
                buffWriter.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        clinetUI mainFrame =  new clinetUI("Client Side App");
        mainFrame.setVisible(true);




        try {
            try {
                socket = new Socket("localhost",12345);
                buffReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            }catch(IOException e ) {
                e.printStackTrace();
                closeConnection(socket, buffReader,buffWriter);
            }
            String fromServer = "";
            while(!fromServer.equals("exit")) {
                fromServer = buffReader.readLine();
                mainFrame.chatArea.setText(mainFrame.chatArea.getText().trim()+ "\n Server:\t" + fromServer );
            }




        } catch(IOException e){
            e.printStackTrace();
        }


    }


}

