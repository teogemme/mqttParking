package spring.httpparking.model;


public class Parcheggio {
	
	public int idParcheggio;
	public String NomeParcheggio;
	public int numPostiDisponibili;
	public int numPostiTot;	
	public boolean isOpen;
	
	public Parcheggio() {
		this.idParcheggio =-1;
		this.numPostiDisponibili = -1;;
		this.numPostiTot = -1;
		this.isOpen = false;
	}


	public Parcheggio(int numPostiTot, String nomeParcheggio, int numPostiDisponibili) {
		this.idParcheggio = -1;
		this.numPostiDisponibili = numPostiTot;
		this.numPostiTot = numPostiTot;
		this.isOpen = true;	
		
	}
	
	

	public Parcheggio(int idParcheggio,String nome, int postiTotali, int postiDisponibili, int isOpen) {
		this.idParcheggio = idParcheggio;
		this.NomeParcheggio=nome;
		this.numPostiTot = postiTotali;
		this.numPostiDisponibili = postiDisponibili;
		if(isOpen==1) this.isOpen = true;
		else this.isOpen = false;
	}
	//get
	public int getIdParcheggio() {
		return idParcheggio;
	}

	public String getNomeP() {
		return NomeParcheggio;
	}
	
	public int getNumPostiAvailable() {
		return numPostiDisponibili;
	}

	public int getNumPostiTot() {
		return numPostiTot;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public boolean isAvailable() {
		return numPostiDisponibili > 0;
	}


	//set
	public void setOpened() {
		isOpen = true;
	}


	public void setNomeP(String nome) {
		NomeParcheggio = nome;
	}
	
	public void setClosed() {
		isOpen = false;
	}


	//funzioni
	public void addMacchina()
	{
		if (isAvailable()) numPostiDisponibili--;
	}


    public void uscitaMacchina() {
            numPostiDisponibili++;
        
    }

}
