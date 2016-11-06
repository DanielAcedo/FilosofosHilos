package com.danielacedo.psp;

import java.util.Random;

class Palillo{
	private int numero;
	private boolean enUso;
	
	public Palillo (int x){
		numero = x;
		enUso = false;
	}
	
	public synchronized void coger(){
		while(enUso){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		enUso = true;
	}
	
	public synchronized void soltar(){
		enUso = false;
		this.notifyAll();
	}
}

class Filosofo extends Thread{
	private Cena cena;
	private int pizq, pder;
	private int numero; //posicion en la mesa
	private int veces; //veces que va a cenar
	
	public Filosofo(int posicion, int numeroCena, Cena cena){
		numero = posicion;
		veces = numeroCena;
		this.cena = cena;
		this.pizq = posicion;
		this.pder = (posicion+1) % cena.comensales;
	}
	@Override
	public void run(){
		for(int i = 0; i < veces; i++){
			pensar();
			cogerPalillos();
			comer();
			soltarPalillos();
		}
	}
	
	public void pensar(){
		System.out.println("Filosofo "+numero+" está pensando...");
		try {
			sleep(1+new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void comer(){
		System.out.println("Filosofo "+numero+" está comiendo...");
		try {
			sleep(1+new Random().nextInt(2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void cogerPalillos(){
		cena.cogerPalilloIzq(pizq);
		System.out.println("Filosofo "+ numero +" coge el palillo "+pizq);
		cena.cogerPalilloDer(pder);
		System.out.println("Filosofo "+ numero +" coge el palillo "+pder);
		
	}
	
	public void soltarPalillos(){
		cena.soltarPalilloIzq(pizq);
		System.out.println("Filosofo "+ numero +" suelta el palillo "+pizq);
		cena.soltarPalilloDer(pder);
		System.out.println("Filosofo "+ numero +" suelta el palillo "+pder);
	}
}

class Cena{
	Palillo palillos[];
	int comensales;
	
	public Cena(int invitados){
		comensales = invitados;
		palillos = new Palillo[invitados];
		
		for(int i = 0; i<invitados; i++){
			palillos[i] = new Palillo(i);
		}
	}
	
	public Palillo cogerPalillo(int x){
		return palillos[x];
	}
	
	public void cogerPalilloDer(int x){
		palillos[x].coger();
	}
	
	public void cogerPalilloIzq(int x){
		palillos[x].coger();
	}
	
	public void soltarPalilloDer(int x){
		palillos[x].soltar();
	}
	
	public void soltarPalilloIzq(int x){
		palillos[x].soltar();
	}
}


public class SimulacionCena {

	public static void main(String[] args) {
		
		if(args.length != 2){
			System.err.println("Debes indicar dos parametros: Numero de filosofos y numero de cenas");
			System.exit(-1);
		}
		
		int numComensales = Integer.parseInt(args[0]);
		int numCenas = Integer.parseInt(args[1]);
		
		Cena cena = new Cena (numComensales);
		
		for (int i = 0; i < numComensales; i++){
			new Filosofo(i, numCenas, cena).start();
		}
	}

}
