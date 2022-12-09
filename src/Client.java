import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe singleton che rappresenta l'applicazione dal punto di vista del client.
 */
public class Client {
    /**
     * Oggetto della classe socket necessario per stabilire la connessione
     * con il server.
     */
    private Socket socket = null;
    /**
     * Stream di output che permette di inviare dei messaggi al server.
     */
    private ObjectOutputStream out = null;
    /**
     * Stream di input che permette di leggere i messaggi ricevuti dal server.
     */
    private ObjectInputStream in = null;
    /**
     * @param address indirizzo del server a cui il client si deve connettere.
     * @param port numero di porta a cui il server accetta la connessione.
     * @throws IOException eccezione che viene sollevata in caso di errori con
     * operazione di input/output.
     * @throws ClassNotFoundException eccezione che viene sollevata in caso di errori con
     * la ricerca della classe.
     */
    private Client (String address, int port) throws IOException, ClassNotFoundException{

        System.out.println("Requesting connection...");
        socket = new Socket(address, port);
        System.out.println(socket);
        System.out.println("Connection accepted.");
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
        //talking();
    }
    /**
     * Unica istanza della classe singleton Client.
     */
    private static Client client;

    /**
     * Inizializzazione dell'unica istanza possibile di Client.
     */
    public static void connectClient() {
        try {
            client = new Client("127.0.0.1", 2025);
        } catch (IOException e) {
            client = null;
        } catch (ClassNotFoundException e) {
            client = null;
        }
    }

    /**
     * Metodo che restituisce l'unica istanza della classe Client.
     */
    public static Client getIstance() {
        return client;
    }

    /**
     * Metodo che rappresenta il corpo principale dell'applicazione.
     * Chiede all'utente una modalità di caricamento del trainingSet e un esempio
     * e sulla base di questi effettua la predizione, comunicando al server le informazioni
     * ricavate dall'utente e stampando a video i risultati elaborati dal server.
     * @throws IOException eccezioni che vengono sollevate in caso di errore con gli stream di
     * input e gli stream di output.
     * @throws ClassNotFoundException eccezioni che vengono sollevate in caso di errore nella
     * ricerca della classe.
     */
    /**private void talking() throws IOException, ClassNotFoundException {
        int decision = 0;
        String menu = "";
        do {
            do {
                System.out.println("Load KNN from file [1]");
                System.out.println("Load KNN from binary file  [2]");
                System.out.println("Load KNN from database  [3]");
                decision = Keyboard.readInt();
                if (decision < 1 || decision > 3) {
                    System.out.println("Invalid decision.");
                }
            } while(decision <1 || decision > 3);
            String risposta="";
            do {
                out.writeObject(decision);
                String tableName="";
                System.out.println("Table name (without extension):");
                tableName=Keyboard.readString();
                out.writeObject(tableName);
                risposta=(String)in.readObject();
                if(risposta.contains("@ERROR")) {
                    System.out.println("Error!\nTable name inexistent.");
                }
            } while (risposta.contains("@ERROR"));

            System.out.println("KNN loaded on the server");
            // predict
            String c = "";
            do {
                //out.writeObject(4);	//serve?
                boolean flag = true; //reading example
                do {
                    risposta = (String) (in.readObject());
                    if(!risposta.contains("@ENDEXAMPLE")) {
                        // sto leggendo l'esempio
                        String msg=(String)(in.readObject());
                        if(risposta.equals("@READSTRING"))  //leggo una stringa
                        {
                            System.out.println(msg);
                            out.writeObject(Keyboard.readString());
                        }
                        else //leggo un numero
                        {
                            double x = 0.0;
                            do {
                                System.out.println(msg);
                                x = Keyboard.readDouble();
                            }
                            while(new Double(x).equals(Double.NaN));
                            out.writeObject(x);
                        }
                    }
                    else flag = false;	//l'esempio da leggere è terminato
                } while (flag);
                //sto leggendo  k
                risposta = (String) (in.readObject());
                int k = 0;
                do {
                    System.out.print(risposta);
                    k = Keyboard.readInt();
                    if (k < 1) {
                        System.out.println("K invalid.");
                    }
                } while (k < 1);
                out.writeObject(k);
                //aspetto la predizione
                System.out.println("Prediction:" + in.readObject());
                do {
                    System.out.println("Do you want to repeat the prediction? Y/N");
                    c = Keyboard.readString().toLowerCase();
                    if(!c.equals("y") && !c.equals("n")) {
                        System.out.println("Invalid answer.");
                    }
                } while (!c.equals("y") && !c.equals("n"));
                out.writeObject(c);
            } while (c.equals("y"));
            do {
                System.out.println("Do you want to repeat the execution with an other KNN object? (Y/N)");
                menu = Keyboard.readString().toLowerCase();
                if(!menu.equals("y") && !menu.equals("n")) {
                    System.out.println("Invalid answer.");
                }
            } while (!menu.equals("y") && !menu.equals("n"));
            out.writeObject(menu);
        }
        while(menu.equals("y"));
    }

    /**
     * Main del programma lato client.
     * @param args argomenti che vengono passati al programma.
     * Tali argomenti devono consistere nell'indirizzo IP del server e nel numero di porta
     * del server a cui connettersi.
     */
    public static void main(String[] args){

        InetAddress addr;
        try {
            addr = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println(e.toString());
            return;
        }

        Client c;
        try {
            c=new Client(args[0], new Integer(args[1]));

        }  catch (IOException e) {
            System.out.println(e.toString());
            return;
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
            return;
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
            return;
        }
    }
}
