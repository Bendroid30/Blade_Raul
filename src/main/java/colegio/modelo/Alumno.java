package colegio.modelo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "personas")
@DiscriminatorColumn(name = "rol_intituto")
public class Alumno{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "dni", unique = true, length = 9)
	private String DNI;
	@Column(name = "telefono", length = 9)
	private String tLF;
	@Column(name = "edad")
	private int edad;
	@Column(name = "curso")
	private String curso;


	public Alumno(String nombre, String curso, String dNI, String tLF, int edad) {
		this.nombre=nombre;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
		this.curso = curso;
	}

	public Alumno(long id, String nombre, String curso, String dNI, String tLF, int edad) {
		this.nombre=nombre;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
		this.id=id;
		this.curso = curso;
	}

	public Alumno(long id) {
		this.nombre=null;
		this.DNI=null;
		this.tLF=null;
		this.edad=0;
		this.id=id;
	}

	public Alumno(long id, String nombre, String dNI, int edad, String tLF, String curso) {
		this.nombre=nombre;
		this.DNI=dNI;
		this.tLF=tLF;
		this.edad=edad;
		setCurso(curso);
		this.id=id;
	}

	public Alumno() {

	}


	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String DNI) {
		this.DNI = DNI;
	}

	public String gettLF() {
		return tLF;
	}

	public void settLF(String tLF) {
		this.tLF = tLF;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public List<Double> getListaNotas() {
		return listaNotas;
	}

	public void setListaNotas(List<Double> listaNotas) {
		this.listaNotas = listaNotas;
	}

	public void agregarNota(double nota) {
		if (nota <= 10 && nota >= 0) {
			this.listaNotas.add(nota);
		} else {
			System.err.println("Nota insertada inválida");
		}
	}

	public void eliminarNotas() {
		this.listaNotas.clear();
	}

	public double mediaNotas() {
		double sumaTotal = 0;
		for (Double nota : this.listaNotas) {
			sumaTotal += nota;
		}
		return sumaTotal / this.listaNotas.size();
	}

	@Override
	public String toString() {
		return "Alumno [curso=" + curso + ", getNombre()=" + getNombre() + ", getDNI()="
				+ getDNI() + ", gettLF()=" + gettLF() + ", getEdad()=" + getEdad() + "]";
	}

}
