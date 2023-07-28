package spring.httpparking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import spring.httpparking.model.Parcheggio;
import spring.httpparking.model.Ticket;

@Component
public class Database {

	private static final String DATABASE_URL = "jdbc:sqlite:mqttParking-main/SmartParking20034251/src/main/java/spring/httpparking/database/parcheggi.db";

    public int NearestParcheggioID(){
        ArrayList<Integer> parcheggi = new ArrayList<>();
         try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID FROM PARCHEGGI WHERE stato=1")) {

            while (rs.next()) {
            int idParcheggio = rs.getInt("ID");
            
                // Creazione dell'oggetto Parcheggio e aggiunta alla lista
                
                parcheggi.add(idParcheggio);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei parcheggi dal database: " + e.getMessage());
        }
        double doubleRandomNumber = Math.random() * parcheggi.size();
        int b=(int)doubleRandomNumber;
        int c=parcheggi.get(b);
        return c;
    }
    
    
    public List<Parcheggio> getAllParcheggi() {
    	List<Parcheggio> parcheggi = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM PARCHEGGI")) {

            while (rs.next()) {
            int idParcheggio = rs.getInt("ID");
            String nomeParcheggio = rs.getString("nomeparcheggio");
            int postiTotali = rs.getInt("numPosti");
            int postiDisponibili = rs.getInt("postidisponibili");
            int isOpen = rs.getInt("stato");

                // Creazione dell'oggetto Parcheggio e aggiunta alla lista
                Parcheggio parcheggio = new Parcheggio(idParcheggio,nomeParcheggio, postiTotali, postiDisponibili, isOpen);
                parcheggi.add(parcheggio);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei parcheggi dal database: " + e.getMessage());
        }
        return parcheggi;
    }

    public ArrayList<Ticket> getAllTicketparcheggio(int idP) {
    	ArrayList<Ticket> ticket = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TICKET WHERE ID_parcheggio='"+idP+"'")) {

            while (rs.next()) {
            int IdTicket = rs.getInt("ID");
            int idParcheggio = rs.getInt("ID_parcheggio");
            int stato = rs.getInt("stato");

                // Creazione dell'oggetto Parcheggio e aggiunta alla lista
                Ticket t = new Ticket(IdTicket,idParcheggio,stato);
                ticket.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei ticket dal database: " + e.getMessage());
        }
        return ticket;
    }

    public Parcheggio getParcheggioById(Integer parkingid) {
    	//ArrayList<Parcheggio> parcheggi = null;
        Parcheggio parcheggio=new Parcheggio();
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from PARCHEGGI where id='"+parkingid+"'")) {
                
                while (rs.next()) {
                    int idParcheggio = rs.getInt("ID");
                    String nomeParcheggio = rs.getString("nomeparcheggio");
                    int postiTotali = rs.getInt("numPosti");
                    int postiDisponibili = rs.getInt("postidisponibili");
                    int isOpen = rs.getInt("stato");
    
                    // Creazione dell'oggetto Parcheggio e aggiunta alla lista
                    parcheggio = new Parcheggio(idParcheggio,nomeParcheggio, postiTotali, postiDisponibili, isOpen);
                }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei parcheggi dal database: " + e.getMessage());
        }
        return parcheggio;
    }



    // aggiunge nuovo parcheggio
    public void insertUpdateParcheggio(Parcheggio p) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = conn.createStatement()) {
                System.out.println(p.getNomeP());
                System.out.println("\n\n\n"+p.toString());
                int a=0;
                if(p.isOpen()==true) a=1;
                if(p.getIdParcheggio()==-1){
                    String sql = "INSERT INTO PARCHEGGI (nomeparcheggio, numPosti, postidisponibili, stato) " +
                    "VALUES ('" + p.getNomeP() + "', " + p.getNumPostiTot() + ", " +
                    p.getNumPostiAvailable() + ", " + a + ")";

            System.out.println(sql);
            stmt.executeUpdate(sql);
            System.out.println("Parcheggio inserito correttamente nel database.");
                }
                else{
                    String sql = "UPDATE PARCHEGGI SET nomeparcheggio='" + p.getNomeP() + "', numPosti='" + p.getNumPostiTot() + "', postidisponibili='" + p.getNumPostiAvailable() + "', stato='" + a  +
                    "' WHERE ID='" + p.getIdParcheggio()+"'";

                    stmt.executeUpdate(sql);
                    System.out.println("Parcheggio aggiornato correttamente nel database.");
                }
        	
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del parcheggio nel database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // aggiunge nuovo parcheggio
    public void insertUpdateTicket(Ticket t) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
            Statement stmt = conn.createStatement()) {
                int stato=0;
                if(t.isPagato()==true) stato=1;
                if(t.getIdTicket()==-1){
                    String sql = "INSERT INTO TICKET (ID_parcheggio, stato) " +
                    "VALUES (" + t.getIdParcheggio() + ", " + stato + ")";

            stmt.executeUpdate(sql);
            System.out.println("ticket inserito correttamente nel database.");
                }
                else{
                    String sql = "UPDATE TICKET SET ID_parcheggio='" + t.getIdParcheggio() + "', stato='" + stato +"' WHERE ID='" + t.getIdTicket()+"'";

                
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                    System.out.println("ticket aggiornato correttamente nel database.");
                }
        	
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del ticket nel database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // elimina nuovo parcheggio
    public void deleteParcheggioFromDB(int idParcheggio) {
    	try (Connection conn = DriverManager.getConnection(DATABASE_URL);
                Statement stmt = conn.createStatement()) {
                String sql = "DELETE FROM PARCHEGGI WHERE ID ='"+idParcheggio+"'";
                stmt.executeUpdate(sql);
                System.out.println("Parcheggio cancellato correttamente nel database.");
            } catch (SQLException e) {
                System.err.println("Errore durante la cancellazione del parcheggio nel database: " + e.getMessage());
            }
    }

    // elimina nuovo parcheggio
    public void rimuoviTicket(int idP) {
        ArrayList<Ticket> ticket = new ArrayList<>();
    	try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TICKET WHERE ID_parcheggio='"+idP+"'")) {

            while (rs.next()) {
            int IdTicket = rs.getInt("ID");
            int idParcheggio = rs.getInt("ID_parcheggio");
            int stato = rs.getInt("stato");

                // Creazione dell'oggetto Parcheggio e aggiunta alla lista
                Ticket t = new Ticket(IdTicket,idParcheggio,stato);
                ticket.add(t);
            }
            cancellaticket(ticket.get(0).getIdTicket());
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei ticket dal database, la cancellazione non è stata effettuata" + e.getMessage());
        }
    }

    private void cancellaticket(int id){
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
                Statement stmt = conn.createStatement()) {
                String sql = "DELETE FROM TICKET WHERE ID='"+id+"'";
                stmt.executeUpdate(sql);
                System.out.println("Ticket cancellato correttamente nel database.");
            } catch (SQLException e) {
                System.err.println("Errore durante la cancellazione del ticket nel database: " + e.getMessage());
                e.printStackTrace();
            }
    }


	public void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
        	Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS parcheggi (id TEXT PRIMARY KEY, nome_parcheggio TEXT, posti_totali INTEGER, posti_disponibili INTEGER, is_open INTEGER)";
            statement.executeUpdate(createTableQuery);
            System.out.println("Tabella parcheggi creata correttamente.");
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione della tabella: " + e.getMessage());
        }
    }
}

