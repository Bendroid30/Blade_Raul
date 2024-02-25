package colegio.controlador.dao;

import colegio.modelo.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;


public class AlumnoJPADAO implements DAOAlumnosInterface<Alumno> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void crearTabla() {

    }

    @Override
    public boolean modificarTabla(Alumno alumno) {
        abrirEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(alumno);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
        cerrarEntityManager();
        return true;
    }

    @Override
    public List<Alumno> anyadirLista(List lista) {
        abrirEntityManager();
        try {
            TypedQuery<Alumno> query = entityManager.createQuery("SELECT a FROM Alumno a", Alumno.class);
            for (Alumno alumno : query.getResultList()) {
                Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
                Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
            }
            List<Alumno> resultados = query.getResultList();

            // Agregar los resultados a la lista dada
            lista.addAll(resultados);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cerrarEntityManager();
        return lista;
    }

    @Override
    public boolean insertarenTabla(Alumno alumno) {
        abrirEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(alumno);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;

        }
        cerrarEntityManager();
        return true;
    }

    @Override
    public void eliminardeTabla(Alumno alumno) {
        abrirEntityManager();
        try {
            Alumno persona = entityManager.find(Alumno.class, alumno.getId());
            if (persona != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(persona);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
        cerrarEntityManager();
    }

    @Override
    public Alumno buscarTabla(Alumno alumno) {
        abrirEntityManager();
        try {
            Alumno persona = entityManager.find(Alumno.class, alumno.getId());
            entityManager.getTransaction().commit();
            cerrarEntityManager();
            return persona;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Alumno> listaAprobados() {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "LEFT JOIN FETCH a.listaNotas n", Alumno.class
        );
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();
        List<Alumno> result = new ArrayList<>();

        for (Alumno alumno : listaAlumnos) {
            double media = alumno.mediaNotas();
            if (media >=5) {
                result.add(alumno);
            }
        }

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return result;
    }

    @Override
    public List<Alumno> listaSuspensos() {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "LEFT JOIN FETCH a.listaNotas n", Alumno.class
        );
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();
        List<Alumno> result = new ArrayList<>();

        for (Alumno alumno : listaAlumnos) {
            double media = alumno.mediaNotas();
            if (media <5) {
                result.add(alumno);
            }
        }

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return result;
    }

    @Override
    public List<Alumno> ordenAlfabetico() {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "ORDER BY a.nombre", Alumno.class
        );
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public Alumno buscarIdAlumno(long id) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a where a.id = :id", Alumno.class
        );
        query.setParameter("id", id);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos.getFirst();
    }

    public void agregarNota(Alumno alumno, Double nota) {
        abrirEntityManager();
        try {
            entityManager.getTransaction().begin();

            List<Double> listaNotas = alumno.getListaNotas();
            listaNotas.add(nota);

            entityManager.merge(alumno);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            System.err.println("* ERROR AL: AGREGAR NOTA *");
        }
        cerrarEntityManager();
    }

    @Override
    public List<Alumno> coincidenciaExactaNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre = :nombre", Alumno.class
        );
        query.setParameter("nombre", nombreRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> contienePalabraClaveNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre", Alumno.class
        );
        query.setParameter("nombre", "%" + nombreRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> empiezaPorNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre",Alumno.class
        );
        query.setParameter("nombre", nombreRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> terminaEnNombre(String nombreRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.nombre LIKE :nombre", Alumno.class
        );
        query.setParameter("nombre", "%" + nombreRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> coincidenciaExactaDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI = :dni", Alumno.class
        );
        query.setParameter("dni", dniRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> contienePalabraClaveDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", "%" + dniRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> empiezaPorDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", dniRecibido + "%");
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;

    }

    @Override
    public List<Alumno> terminaEnDni(String dniRecibido) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "WHERE a.DNI LIKE :dni", Alumno.class
        );
        query.setParameter("dni", "%" + dniRecibido);
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return listaAlumnos;
    }

    @Override
    public List<Alumno> notaMediaAlum(double mediaRecibida) {
        abrirEntityManager();
        TypedQuery<Alumno> query = entityManager.createQuery(
                "SELECT a FROM Alumno a " +
                        "LEFT JOIN FETCH a.curso c " +
                        "LEFT JOIN FETCH a.listaNotas n", Alumno.class
        );
        for (Alumno alumno : query.getResultList()) {
            Hibernate.initialize(alumno.getListaNotas()); // fix lazy notas finder
            Hibernate.initialize(alumno.getCurso()); // fix lazy notas finder
        }
        List<Alumno> listaAlumnos = query.getResultList();
        List<Alumno> result = new ArrayList<>();

        for (Alumno alumno : listaAlumnos) {
            double media = alumno.mediaNotas();
            if (media == mediaRecibida) {
                result.add(alumno);
            }
        }

        System.out.println("Tablas Alumnos listadas");
        cerrarEntityManager();
        return result;
    }

    public void cerrarEntityManager() {
        entityManager.close();
    }

    public void abrirEntityManager() {
        entityManager = ControladorDAOJPA.getEntityManagerFactory().createEntityManager();
    }
}

