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
		System.out.println("Palillo "+numero+ " cogido");
	}
	
	public synchronized void soltar(){
		enUso = false;
		System.out.println("Palillo "+numero+ " soltado");
		this.notify();
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
		this.pizq = cena.cogerPalilloIzq(numero);
		this.pder = cena.cogerPalilloDer(numero);
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
			System.out.println("Filosofo "+numero+" está satisfecho...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void cogerPalillos(){
		System.out.println("Filosofo "+numero+" trata de coger sus palillos");
		
		if(numero%2==0){
			cena.cogerPalillo(pizq).coger();
			cena.cogerPalillo(pder).coger();
		}else{
			cena.cogerPalillo(pder).coger();
			cena.cogerPalillo(pizq).coger();
		}
	}
	
	public void soltarPalillos(){
		System.out.println("Filosofo "+numero+" suelta sus palillos");
		cena.cogerPalillo(pizq).soltar();
		cena.cogerPalillo(pder).soltar();
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
	
	public int cogerPalilloDer(int x){
		return (x+1) % comensales;
	}
	
	public int cogerPalilloIzq(int x){
		return x;
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
