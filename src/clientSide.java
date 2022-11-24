import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class clientSide extends JFrame{
    private JPanel mainPanel;
    private JTextField textBox;
    private JButton sendButton;
    private  JTextArea chatArea;
    private JButton sendFileButton;
    private  static Socket socket;
    private static InputStreamReader inStream;
    private  static BufferedWriter buffWriter;
    private static  BufferedReader buffReader;



    /**
     * @param title gets the title so that the frame can update it when the program is ran.
     *              Function: Creates the server side UI to be used throughout the program
     *
     */
    public clientSide(String title){
        super(title); //gets the title of the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        //when the send button is pressed, retreive the message that was in the text box and send the message.
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



    /**
     * Method: sendMessage
     * Function: Sends a message to the client
     * @param message : Message to send to the client
     */
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

    /**
     * Method: closeConnection
     * Function: Closes the connection of all the thing that the server uses, and throws and exception if it does not work
     * @param socket  Used to close the socket connection
     * @param buffReader Used to close the reader Connection
     * @param buffWriter Used to close the writer
     */
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


    /**
     * Main Method that handles the runtime of the program
     * Function: Will load the JFrame so that the user can type out to the screen and send messages.
     * Will attempt to connect to the server so that the client and send messages to the server and can receive
     * messages to the server.
     */
    public static void main(String[] args){
        clientSide mainFrame =  new clientSide("Client Side App");
        mainFrame.setVisible(true);
        try {

            //Attempts to establish a connection to the server, throws an exception if not possible.
            try {
                socket = new Socket("localhost",12345);
                buffReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            }catch(IOException e ) {
                e.printStackTrace();
                closeConnection(socket, buffReader,buffWriter);
            }

            //While the message from the server is not "null", update the receive the message from the client and update the chat box.
            String fromServer = "";
            while(!fromServer.equals("null")) {
                fromServer = buffReader.readLine();
                mainFrame.chatArea.setText(mainFrame.chatArea.getText().trim()+ "\n Server:\t" + fromServer );
            }




        } catch(IOException e){
            e.printStackTrace();
        }


    }


}

