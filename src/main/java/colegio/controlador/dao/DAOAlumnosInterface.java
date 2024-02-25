package colegio.controlador.dao;

import colegio.modelo.Alumno;

import java.util.List;

public interface DAOAlumnosInterface<T> {
    void crearTabla();
    List<T> anyadirLista(List<T> lista);
    boolean modificarTabla(T alumno);
    boolean insertarenTabla(T alumno);
    void eliminardeTabla(T alumno);
    T buscarTabla(T alumno);

    List<T> listaAprobados();
    List<T> listaSuspensos();
    List<T> ordenAlfabetico();
    Alumno buscarIdAlumno(long id);
    List<T> coincidenciaExactaNombre(String nombreRecibido);
    List<T> contienePalabraClaveNombre(String nombreRecibido);
    List<T> empiezaPorNombre(String nombreRecibido);
    List<T> terminaEnNombre(String nombreRecibido);
    List<T> coincidenciaExactaDni(String dniRecibido);
    List<T> contienePalabraClaveDni(String dniRecibido);
    List<T> empiezaPorDni(String dniRecibido);
    List<T> terminaEnDni(String dniRecibido);
    List<T> notaMediaAlum(double mediaRecibida);

}
