package logicaJuego;

import java.util.ArrayList;
import java.util.HashMap;

import elementos.Coordenada;
import elementos.Element;
import elementos.ElementType;
import elementos.Jugador;
import elementos.JugadorException;
import elementos.PlayerType;

public class Juego {

	private HashMap<Coordenada, Element> tablero;
	private ArrayList<Coordenada> coordenadaJugadores;
	private int jugadorJuega;
	private int dado;

	public Juego(PlayerType[] personaje) {
		super();
		int contador=0;
		coordenadaJugadores = new ArrayList<>();
		tablero = new HashMap<>();
		crearTablero();
		while(contador<Constantes.NUM_JUGADORES) {
			if(crearJugador(personaje[contador])) {
				contador++;
			}
		}
		
	}

	private boolean crearJugador(PlayerType tipo) {
		boolean crear = false;
		Jugador jugador = new Jugador(tipo);
		Coordenada coordenada = new Coordenada();
		while (coordenadaJugadores.contains(coordenada)) {
			coordenada = new Coordenada();
		}if(this.tablero.get(coordenada)==null) {
			coordenadaJugadores.add(coordenada);
			tablero.put(coordenada, jugador);
			crear = true;
		}
		
		
		return crear;
	}

	private void crearTablero() {
		crearDinero();
		crearGemas();
		crearPociones();
		crearRocas();
	}
	
	private void crearRocas() {
		int contador = 0;
		while (contador < Constantes.NUM_ROCAS) {
			Coordenada coor1 = new Coordenada();
			Element e = new Element(ElementType.ROCA);
			if (tablero.get(coor1) == null) {
				this.tablero.put(coor1, e);
				contador++;
			}

		}
	}

	private void crearDinero() {
		int contador = 0;
		while (contador < Constantes.NUM_DINERO) {
			Coordenada coordenada = new Coordenada();
			Element elemento = new Element(ElementType.DINERO);
			if (tablero.get(coordenada) == null) {
				this.tablero.put(coordenada, elemento);
				contador++;
			}

		}

	}

	public Element obtenerElementoTablero(Coordenada coord) {
		return this.tablero.get(coord);
	}

	public Coordenada obtenerCoordenadaJugadorJuega() {
		return this.coordenadaJugadores.get(jugadorJuega);
	}
	
	private void crearGemas() {
		int contador = 0;
		while (contador < Constantes.NUM_GEMAS) {
			Coordenada coor1 = new Coordenada();
			Element e = new Element(ElementType.GEMA);
			if (tablero.get(coor1) == null) {
				this.tablero.put(coor1, e);
				contador++;
			}

		}
	}

	private void crearPociones() {
		int contador = 0;
		while (contador < Constantes.NUM_POCIONES) {
			Coordenada coor1 = new Coordenada();
			Element e = new Element(ElementType.POCION);
			if (tablero.get(coor1) == null) {
				this.tablero.put(coor1, e);
				contador++;
			}

		}
	}

	public boolean isTerminado() {
		boolean terminar = false;
		boolean dinero = false;
		for (Element elemento : this.tablero.values()) {
			if (elemento instanceof Jugador) {
				if (((Jugador) elemento).getDinero() == Constantes.DINERO) {
					dinero = true;
				}
			}
		}
	if (this.coordenadaJugadores.size() == 1 || dinero) {
		terminar = true;
		}
		return terminar;
	}

	private void eliminarJugador(Coordenada coordenada) {
		this.coordenadaJugadores.remove(coordenada);
		this.tablero.remove(coordenada);
	}

	public String imprimeNombreJugadores() {
		int contador = 1;
		StringBuilder sb = new StringBuilder();
		
		for (Coordenada coordenada : this.coordenadaJugadores) {
		Jugador jugadores = (Jugador) tablero.get(coordenada);
			sb.append("El tipo del jugador es: " + jugadores.getNombre() + " y es el jugador numero: " + contador + "\n");
			contador++;
		}
		return sb.toString();
	}

	public int getValorDado() {
		return dado;
	}

	public int decrementaDado() {
		return this.dado--;
	}

	public void setDado() {
		this.dado = ((Jugador) tablero.get(coordenadaJugadores.get(jugadorJuega))).getVelocidadParaLuchar();
	}

	private void cambiaJugadorAPosicion(Coordenada coordenada) {
		Coordenada coor = coordenadaJugadores.get(jugadorJuega); 
		Jugador jug = (Jugador) tablero.get(coor);
		
		tablero.remove(coor); 
		coordenadaJugadores.remove(jugadorJuega); 
		coordenadaJugadores.add(jugadorJuega, coordenada); 
		tablero.put(coordenada, jug);
	}

	public String imprimeValoresJugadores() {
		int contador = 1;
		StringBuilder sb = new StringBuilder();
		
		for (Coordenada coordenada : this.coordenadaJugadores) {
			Jugador jugador = (Jugador) tablero.get(coordenada);
			sb.append("Jugador numero: " + contador 
						+ " Dinero: " + jugador.getDinero() 
						+ " Gemas: " + jugador.getGemas() 
						+ " Pociones: " + jugador.getPociones() + "\n");
			contador++;
		}
		return sb.toString();
	}

	private Coordenada getNextPosition (char direccion) throws JuegoException {
		if(direccion!='N' && direccion!='S' && direccion!='E' && direccion!='O') {
			throw new JuegoException("Error en la direcciÃ³n.");
		}
		Coordenada coor;
		try {
			coor = coordenadaJugadores.get(jugadorJuega).clone();
			if(direccion=='N') {
				coor.goUp();
			}else if(direccion=='S') {
				coor.goDown();
			}else if(direccion=='E') {
				coor.goRight();
			}else {
				coor.goLeft();
			}
		} catch (CloneNotSupportedException e) {
			throw new JuegoException(e.getMessage());
		}
		return coor;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(barra());
		res.append("     |");

		for (int fila = 0; fila < Constantes.TAMANNO; fila++) {
			for (int columna = 0; columna < Constantes.TAMANNO; columna++) {
				Coordenada coor = new Coordenada(columna, fila);

				if (this.tablero.get(coor) != null) {
					res.append(" " + this.tablero.get(coor).getType().getSymbol() + " ");
				} else {
					res.append("   ");
				}

				res.append("|");
			}
			res.append("\n");
			res.append(barra());
			res.append("     |");
		}
		res.delete(res.length() - 5, res.length());
		return res.toString();
	}

	public void proximoJugador() {
		if (this.jugadorJuega == this.coordenadaJugadores.size() - 1) {
			this.jugadorJuega = 0;
		} else {
			jugadorJuega++;
		}
	}

	public String getGanador() {
		StringBuilder resultado = new StringBuilder();
		if (this.coordenadaJugadores.size() == 1) {
			Jugador jugador = (Jugador) tablero.get(coordenadaJugadores.get(jugadorJuega-1));
			resultado.append(jugador.toString());
			
		} else {
			
			for (Element siguiente : tablero.values()) {
				if (siguiente instanceof Jugador) {
					Jugador jugador = ((Jugador) siguiente);
					if (jugador.getDinero() == Constantes.NUM_DINERO) {
						resultado.append(jugador);
					}
				}
			}
		}
		return resultado.toString();
	}

	public String getNombreJugadorQueJuega() {
		StringBuilder sb = new StringBuilder();
		Coordenada coor = this.coordenadaJugadores.get(jugadorJuega);
		
	
		Jugador jugador = (Jugador) this.tablero.get(coor);
		sb.append(jugador.getNombre());
		return sb.toString();
	}

	public int getMovimientoJugador() {
		Coordenada coor = this.coordenadaJugadores.get(jugadorJuega);
		Jugador jugador = (Jugador) this.tablero.get(coor);
		return jugador.getFuerzaParaLuchar();
	}

	private String barra() {
		StringBuilder resul = new StringBuilder();
		resul.append("     ");
		for (int i = 0; i < Constantes.TAMANNO * 4; i++) {
			resul.append("-");
		}
		resul.append("\n");
		return resul.toString();
	}

	public String movePlayer(char direction) throws JuegoException, JugadorException {
		String resul = "";
		Jugador jugador = (Jugador) this.tablero.get(this.coordenadaJugadores.get(jugadorJuega));

		Coordenada coordDestino = getNextPosition(direction);
		Element elemento = this.tablero.get(coordDestino);

		if (elemento != null) {
			if (elemento instanceof Jugador) {

				Jugador enemigo = (Jugador) elemento;
				int resultadoLucha = jugador.lucha(enemigo);
				switch (resultadoLucha) {
				case Constantes.EMPATE:
					resul = "Empate entre los jugadore";
					break;
				case Constantes.GANA_USA_POCIMA:
					resul = "El jugador " + jugador.getNombre() + " gana. Le quita una pócima al enemigo";
					break;
				case Constantes.GANA_DINERO:
					resul = "El jugador " + jugador.getNombre() + " gana. Le quita el dinero al enemigo";
					break;
				case Constantes.GANA_MUERE:
					resul = "El jugador " + jugador.getNombre() + " gana. El enemigo muere";
					this.eliminarJugador(coordDestino);
					break;
				case Constantes.PIERDE_USA_POCIMA:
					resul = "El enemigo " + enemigo.getNombre() + " gana. Le quita una pócima al jugador";
					break;
				case Constantes.PIERDE_DINERO:
					resul = "El enemigo " + enemigo.getNombre() + " gana. Le quita el dinero al jugador";
					break;
				case Constantes.PIERDE_MUERE:
					resul = "El enemigo " + enemigo.getNombre() + " gana. El jugador muere";
					this.eliminarJugador(this.coordenadaJugadores.get(jugadorJuega));
					dado = 0;
					this.jugadorJuega--;
					break;
				}
			} else if (elemento.getType() == ElementType.ROCA) {
				int resultadoRoca = jugador.encuentraRoca();
				switch (resultadoRoca) {
				case Constantes.ROMPE_ROCA_CON_GEMA:
					resul = "El jugador " + jugador.getNombre() + " rompe la roca con una gema";
					this.cambiaJugadorAPosicion(coordDestino);
					break;

				case Constantes.GANA_A_LA_ROCA:
					resul = "El jugador " + jugador.getNombre() + " gana a la roca";
					this.cambiaJugadorAPosicion(coordDestino);
					break;

				case Constantes.PIERDE_A_LA_ROCA:
					resul = "El jugador " + jugador.getNombre() + " pierde. No se mueve";
					break;
				}
			} else if (elemento.getType() == ElementType.GEMA) {
				jugador.encuentraGema();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.DINERO) {
				jugador.encuentraDinero();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.POCION) {
				jugador.encuentraPocion();
				this.cambiaJugadorAPosicion(coordDestino);

			}

		} else {
			this.cambiaJugadorAPosicion(coordDestino);
		}

		return resul;
	}

	
}


