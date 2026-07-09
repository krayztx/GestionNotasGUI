package controller;

import model.Nota;
import model.dao.NotaDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para la gestión de notas.
 * Aplica las validaciones de negocio y calcula el promedio final ponderado
 * (50% exámenes, 40% prácticas, 10% actitudinal) antes de guardar.
 */
public class NotaController {

    private NotaDAO notaDAO;

    public NotaController() {
        this.notaDAO = new NotaDAO();
    }

    // ========== CREATE ==========
    public boolean registrarNota(String codigoEstudiante, String nombres, String apellidos, String curso,
                                  double examen1, double examen2,
                                  double practica1, double practica2, double practica3, double practica4,
                                  double notaActitudinal, String fechaRegistro, String observaciones) {
        try {
            validarDatosBasicos(codigoEstudiante, nombres, apellidos, curso);
            validarRangoNota(examen1, "Examen 1");
            validarRangoNota(examen2, "Examen 2");
            validarRangoNota(practica1, "Práctica 1");
            validarRangoNota(practica2, "Práctica 2");
            validarRangoNota(practica3, "Práctica 3");
            validarRangoNota(practica4, "Práctica 4");
            validarRangoNota(notaActitudinal, "Nota actitudinal");

            Nota n = new Nota(
                    codigoEstudiante.trim().toUpperCase(),
                    nombres.trim(),
                    apellidos.trim(),
                    curso.trim(),
                    examen1, examen2,
                    practica1, practica2, practica3, practica4,
                    notaActitudinal,
                    fechaRegistro.trim(),
                    observaciones != null ? observaciones.trim() : null
            );
            // El promedio final ya se calculó en el constructor de Nota,
            // pero lo recalculamos aquí explícitamente por claridad.
            n.setPromedioFinal(n.calcularPromedioFinal());

            return notaDAO.insertar(n);

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la nota: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    // ========== READ ==========
    public List<Nota> listarNotas() {
        try {
            return notaDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar notas: " + e.getMessage());
        }
    }

    public Nota buscarNota(int id) {
        try {
            return notaDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la nota: " + e.getMessage());
        }
    }

    public List<Nota> buscarPorEstudiante(String texto) {
        try {
            return notaDAO.buscarPorEstudiante(texto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar: " + e.getMessage());
        }
    }

    public List<Nota> listarDesaprobados() {
        try {
            return notaDAO.listarDesaprobados();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar desaprobados: " + e.getMessage());
        }
    }

    public double promedioGeneralPorCurso(String curso) {
        try {
            return notaDAO.promedioGeneralPorCurso(curso);
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular el promedio del curso: " + e.getMessage());
        }
    }

    // ========== UPDATE ==========
    public boolean actualizarNota(int id, String codigoEstudiante, String nombres, String apellidos, String curso,
                                   double examen1, double examen2,
                                   double practica1, double practica2, double practica3, double practica4,
                                   double notaActitudinal, String fechaRegistro, String observaciones) {
        try {
            Nota existente = notaDAO.buscarPorId(id);
            if (existente == null) {
                throw new RuntimeException("Registro no encontrado");
            }

            if (codigoEstudiante != null && !codigoEstudiante.trim().isEmpty()) {
                existente.setCodigoEstudiante(codigoEstudiante.trim().toUpperCase());
            }
            if (nombres != null && !nombres.trim().isEmpty()) {
                existente.setNombres(nombres.trim());
            }
            if (apellidos != null && !apellidos.trim().isEmpty()) {
                existente.setApellidos(apellidos.trim());
            }
            if (curso != null && !curso.trim().isEmpty()) {
                existente.setCurso(curso.trim());
            }

            if (examen1 >= 0) { validarRangoNota(examen1, "Examen 1"); existente.setExamen1(examen1); }
            if (examen2 >= 0) { validarRangoNota(examen2, "Examen 2"); existente.setExamen2(examen2); }
            if (practica1 >= 0) { validarRangoNota(practica1, "Práctica 1"); existente.setPractica1(practica1); }
            if (practica2 >= 0) { validarRangoNota(practica2, "Práctica 2"); existente.setPractica2(practica2); }
            if (practica3 >= 0) { validarRangoNota(practica3, "Práctica 3"); existente.setPractica3(practica3); }
            if (practica4 >= 0) { validarRangoNota(practica4, "Práctica 4"); existente.setPractica4(practica4); }
            if (notaActitudinal >= 0) { validarRangoNota(notaActitudinal, "Nota actitudinal"); existente.setNotaActitudinal(notaActitudinal); }

            if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
                existente.setFechaRegistro(fechaRegistro.trim());
            }
            if (observaciones != null) {
                existente.setObservaciones(observaciones.trim());
            }

            // Recalcular el promedio final con los valores actualizados
            existente.setPromedioFinal(existente.calcularPromedioFinal());

            return notaDAO.actualizar(existente);

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar: " + e.getMessage());
        }
    }

    // ========== DELETE ==========
    public boolean eliminarNota(int id) {
        try {
            Nota existente = notaDAO.buscarPorId(id);
            if (existente == null) {
                throw new RuntimeException("Registro no encontrado");
            }
            return notaDAO.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar: " + e.getMessage());
        }
    }

    public int contarRegistros() {
        try {
            return notaDAO.contarRegistros();
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar registros: " + e.getMessage());
        }
    }

    // ========== Validaciones internas ==========
    private void validarDatosBasicos(String codigoEstudiante, String nombres, String apellidos, String curso) {
        if (codigoEstudiante == null || codigoEstudiante.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de estudiante es obligatorio");
        }
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (curso == null || curso.trim().isEmpty()) {
            throw new IllegalArgumentException("El curso es obligatorio");
        }
    }

    private void validarRangoNota(double nota, String nombreCampo) {
        if (nota < 0 || nota > 20) {
            throw new IllegalArgumentException(nombreCampo + " debe estar entre 0 y 20");
        }
    }
}
