package colegio.controlador;

import colegio.controlador.dao.AlumnoJPADAO;
import colegio.modelo.Alumno;
import colegio.modelo.AlumnoExisteException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorAlumnos {
    private List<Alumno> listaAlumnos = new ArrayList<Alumno>();
    //    private DAOSQLAlumnos DAOSQLAlumnos =new DAOSQLAlumnos();
    private AlumnoJPADAO alumnoJPADAO =new AlumnoJPADAO();
    public void crearTabla(){
        alumnoJPADAO.crearTabla();
    }

    public void agregar(Alumno alumno) throws AlumnoExisteException {
        boolean copia = false;
        for (Alumno alumnoEscogido : listaAlumnos) {
            if (alumnoEscogido.getDNI().equalsIgnoreCase(alumno.getDNI())) {
                copia = true;
                break;
            }
        }
        if (!copia) {
//            DAOSQLAlumnos.insertarenTabla(alumno);
//            for (Double nota : alumno.getListaNotas()) {
//                DAOSQLAlumnos.insertNota(alumno,nota);
//            }
//            listaAlumnos.clear();
//            DAOSQLAlumnos.anyadirLista(listaAlumnos);
            alumnoJPADAO.insertarenTabla(alumno);
            listaAlumnos.clear();
            alumnoJPADAO.anyadirLista(listaAlumnos);


        } else {
            //Lanzar excepción AlumnoExiste
            throw new AlumnoExisteException("EL ALUMNO YA SE ENCUENTRA EN LA LISTA");
        }

    }

    public Alumno buscar(Alumno alumno) {
        return alumnoJPADAO.buscarTabla(alumno);

    }

    public void eliminar(Alumno alumno) {
        boolean copiaNoExiste = false;
        for (Alumno alumnoEscogido : listaAlumnos) {
            if (alumnoEscogido.getId()==(alumno.getId())) {
                alumnoJPADAO.eliminardeTabla(alumnoEscogido);
                listaAlumnos.remove(alumnoEscogido);
                copiaNoExiste = true;
                break;

            }
        }
        if (copiaNoExiste) {
            System.out.println("ALUMNO ELIMINADO CON EXITO");
        } else {
            System.out.println("ALUMNO NO SE ENCUENTRA EN LA BASE DE DATOS");
        }
    }

    public List<Alumno> listar(List<Alumno> lista) {
//        System.out.println("Lista de alumnos");
//        for (Alumno alumnoEscogido : listaAlumnos) {
//            System.out.println(alumnoEscogido.toString());
//        }
        listaAlumnos.clear();
        alumnoJPADAO.anyadirLista(listaAlumnos);
        return lista;
    }

    public List<Alumno> getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    public List<Alumno> listaAprobados(){
        return alumnoJPADAO.listaAprobados();
    }

    public List<Alumno> listaSuspensos(){
        return alumnoJPADAO.listaSuspensos();
    }

    public void agregarNotaAlumno(Alumno alumno, double nota) {
        for (Alumno alumnoEscogido : listaAlumnos) {
            if (alumnoEscogido.equals(alumno)) {
                alumnoJPADAO.agregarNota(alumno,nota);
            } else {
                System.out.println("La persona indicada no se encuentra en la lista");
            }
        }

    }

    public List<Alumno> coincidenciaExactaNombre(String nombreRecibido){
        return alumnoJPADAO.coincidenciaExactaNombre(nombreRecibido);
    }
    public List<Alumno> contienePalabraClaveNombre(String nombreRecibido){
        return alumnoJPADAO.contienePalabraClaveNombre(nombreRecibido);
    }
    public List<Alumno> empiezaPorNombre(String nombreRecibido){
        return alumnoJPADAO.empiezaPorNombre(nombreRecibido);
    }
    public List<Alumno> terminaEnNombre(String nombreRecibido){
        return alumnoJPADAO.terminaEnNombre(nombreRecibido);
    }
    public List<Alumno> coincidenciaExactaDni(String dniRecibido){
        return alumnoJPADAO.coincidenciaExactaDni(dniRecibido);
    }
    public List<Alumno> contienePalabraClaveDni(String dniRecibido){
        return alumnoJPADAO.contienePalabraClaveDni(dniRecibido);
    }
    public List<Alumno> empiezaPorDni(String dniRecibido){
        return alumnoJPADAO.empiezaPorDni(dniRecibido);
    }
    public List<Alumno> terminaEnDni(String dniRecibido){
        return alumnoJPADAO.terminaEnDni(dniRecibido);
    }
    public List<Alumno> notaMediaAlum(double mediaRecibida){
        return alumnoJPADAO.notaMediaAlum(mediaRecibida);
    }

    //Blade metodos
    @GET("/api/alumnos")
    public void apiListaAlumnos(Response response) {
        response.json(listar(listaAlumnos));
    }

    @GET("/api/alumno/get/:id")
    public void apiGetAlumnoId(Response response, @PathParam long id) {
        Optional<Alumno> alumno = Optional.ofNullable(alumnoJPADAO.buscarIdAlumno(id));
        if (alumno.isPresent()) {
            response.status(200);
            response.json(alumno.get());
        } else
            response.status(400);
    }

    @POST("/api/alumno/insert")
    public void apiAddAlumno(@Body String body, Response response) {
        Alumno alumno = new Gson().fromJson(body, new TypeToken<Alumno>() {
        }.getType());
        if (alumnoJPADAO.insertarenTabla(alumno))
            response.status(200);
        else
            response.status(400);
    }

    @PUT("/api/alumno/update/:nombre")
    public void editarAlumno(@PathParam String nombre, @Body String body, Response response) {
        Alumno alumno = new Gson().fromJson(body, new TypeToken<Alumno>() {
        }.getType());
        if (alumnoJPADAO.modificarTabla(alumno))
            response.status(200);
        else
            response.status(400);
    }

    @DELETE("/api/alumno/delete/:id")
    public void eliminarAlumno(@PathParam long id, Response response) {
        if (alumnoDao.eliminarAlumno(id))
            response.status(200);
        else
            response.status(400);
    }

    public AlumnoJPADAO getAlumnoJPADAO() {
        return alumnoJPADAO;
    }



    public void setAlumnoJPADAO(AlumnoJPADAO alumnoJPADAO) {
        this.alumnoJPADAO = alumnoJPADAO;
    }
}
