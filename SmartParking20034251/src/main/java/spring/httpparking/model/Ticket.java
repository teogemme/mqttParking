package spring.httpparking.model;

public class Ticket {

	private int ID;
	private int idParcheggio;
	private boolean pagato;
	
	public Ticket() {
		this.ID = -1;
		this.pagato = true;
		this.idParcheggio=-1;
	}

	public Ticket(int idParcheggio) {
		this.ID = -1;
		this.pagato = true;
		this.idParcheggio=idParcheggio;
	}

	public Ticket(int ID,int idParcheggio,int stato) {
		this.ID = ID;
		if(stato==1) this.pagato = true;
		this.idParcheggio=idParcheggio;
	}

	public int getIdTicket() {
		return ID;
	}

	public int getIdParcheggio() {
		return idParcheggio;
	}
	
	public boolean isPagato() {
        return pagato;
    }

    public void markAsPaid() {
        pagato = true;
    }
}
